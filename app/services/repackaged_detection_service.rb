class RepackagedDetectionService
  def detection(app_record)
    @query_service = RepackagedDetectionQueriesService.new

    # filter initial dataset based on metrics which are hard to obfuscate
    @candidate_ids_certificate = @query_service.filter_initial_dataset app_record

    # find similar apps based on drawable similarity
    @repackaged_ids_certificate = find_similar app_record

    # create hash signature -> number of apps
    @signatures_number_of_apps = signature_to_number_of_apps

    # create hash signature -> ids of apps
    @signatures_ids_of_apps = signature_to_ids_of_apps

    # decide the result of detection and compute similarity metrics
    evaluate app_record

    # create response
    respond app_record
  end

  private

  def signature_to_number_of_apps
    signatures = Hash.new(0)
    @repackaged_ids_certificate.each do |id, certificate|
      signatures[certificate] += UploadRecord.where(app_record_id: id).count
    end

    signatures.sort_by {|_key, value| value.size}.reverse.to_h
    signatures
  end

  def signature_to_ids_of_apps
    signatures = Hash.new()
    @repackaged_ids_certificate.each do |id, certificate|
      signatures[certificate] = [] if signatures[certificate].nil?
      signatures[certificate] << id
    end

    signatures
  end

  def find_similar(app_record)
    repackaged_ids_certificate = []
    @candidate_ids_certificate.each do |candidate_id, candidate_cert_md5|
      similarity_ratio_drawables = @query_service.drawable_intersect_query(app_record.id, candidate_id) / @query_service.drawable_union_query(app_record.id, candidate_id).to_f

      if similarity_ratio_drawables > 0.8
        repackaged_ids_certificate << [candidate_id, candidate_cert_md5]
        SimilarAppRecord.find_or_create_by!(app_record: app_record, app_record_similar_id: candidate_id, score: similarity_ratio_drawables)
      end
    end

    repackaged_ids_certificate
  end

  def evaluate(app_record)
    #decide detection status
    if @signatures_number_of_apps.values.sum < 5
      @status = 'Not Sufficient Data'
    else
      if @signatures_number_of_apps.keys[0] == app_record.cert_md5 &&
          (@signatures_number_of_apps.keys[1].nil? || @signatures_number_of_apps.keys[0] > 1.3 * @signatures_number_of_apps.keys[1])
        @status = 'OK'
      else
        @status = 'NOK'
      end
    end

    #compute metrics
    sum_repackaged_apps = @signatures_number_of_apps.values.sum.to_f
    @percentage_same_signature = @signatures_number_of_apps[app_record.cert_md5] / sum_repackaged_apps * 100
    @percentage_majority_signature = @signatures_number_of_apps.values[0] / sum_repackaged_apps * 100
  end

  def respond(app_record)
    response = {}
    response[:app_record_id] = app_record.id
    response[:status] = @status
    response[:percentage_majority_signature] = @percentage_majority_signature
    response[:percentage_same_signature] = @percentage_same_signature
    response[:signatures_number_of_apps] = @signatures_number_of_apps
    response[:signatures_ids_of_apps] = @signatures_ids_of_apps
    response[:similarity_scores] = SimilarAppRecord.find_by_app_record_id app_record.id

    response
  end

end
