class RepackagedDetectionService

  # Look at repackaged detection results
  # In case the detection was run in last 7 days, return its results
  # Otherwise, run full detection
  def detection_with_caching(app_record)

    existing = RepackagedDetectionResult.find_by(app_record_id: app_record.id)

    if !existing.nil? && existing.created_at > DateTime.now - 7
      handle_cached_results(app_record, existing)
    else
      detection(app_record)
    end
  end


  # Full detection
  def detection(app_record)
    @query_service = RepackagedDetectionQueriesService.new

    # filter initial dataset based on metrics which are hard to obfuscate
    @candidate_ids_certificate = @query_service.filter_initial_dataset app_record

    # find similar apps based on drawable similarity
    @repackaged_ids_certificate = find_similar app_record

    # create hash signature -> number of apps
    @signatures_number_of_apps = signature_to_number_of_apps

    # create hash signature -> ids of apps
    @signatures_of_apps = signature_to_apps

    # decide the result of detection and compute similarity metrics
    evaluate app_record

    # create response
    respond_and_save_results app_record
  end

  private

  # Process cached results and compute requered response parameters
  def handle_cached_results(app_record, repackaged_detection_result)

    # find cached results of similar apps
    @repackaged_ids_certificate = app_record.similar_records.pluck(:id, :package_name, :certificate_hash)
    @repackaged_ids_certificate << [app_record.id, app_record.package_name, app_record.certificate_hash]

    # create hash signature -> number of apps
    @signatures_number_of_apps = signature_to_number_of_apps

    # create response
    respond_and_update_cached repackaged_detection_result
  end

  def signature_to_number_of_apps
    signatures = Hash.new(0)
    @repackaged_ids_certificate.each do |id, package_name, certificate|
      signatures[certificate] += UploadRecord.where(app_record_id: id).count
    end

    signatures.sort_by {|_key, value| value}.reverse.to_h
  end

  def signature_to_apps
    signatures = Hash.new()
    @repackaged_ids_certificate.each do |id, package_name, certificate|
      signatures[certificate] = [] if signatures[certificate].nil?
      signatures[certificate] << [id, package_name]
    end

    signatures
  end

  def find_similar(app_record)
    similarity_threshold = 0.8

    repackaged_ids_certificate = []
    repackaged_ids_certificate << [app_record.id, app_record.package_name, app_record.certificate_hash]

    @candidate_ids_certificate.each do |candidate_id, package_name, candidate_certificate_hash|
      similarity_ratio_drawables = @query_service.compute_similarity(app_record.id, candidate_id)

      if similarity_ratio_drawables > similarity_threshold
        repackaged_ids_certificate << [candidate_id, package_name, candidate_certificate_hash]
        SimilarAppRecord.find_or_create_by!(app_record: app_record, app_record_similar_id: candidate_id, score: similarity_ratio_drawables)
      end
    end

    repackaged_ids_certificate
  end

  def evaluate(app_record)
    #decide detection status
    if @signatures_number_of_apps.values.sum < 5
      @status = :insufficient_data
    else
      if @signatures_number_of_apps.keys[0] == app_record.certificate_hash &&
          (@signatures_number_of_apps.keys[1].nil? || @signatures_number_of_apps.values[0] > 1.3 * @signatures_number_of_apps.values[1])
        @status = :ok
      else
        @status = :nok
      end
    end

    #compute metrics
    @total_repackaged_apps = @signatures_number_of_apps.values.sum.to_f
    @total_different_repackaged_apps = @signatures_of_apps.values.count
    @percentage_same_signature = @signatures_number_of_apps[app_record.certificate_hash] / @total_repackaged_apps * 100
    @percentage_majority_signature = @signatures_number_of_apps.values[0] / @total_repackaged_apps * 100
  end

  def respond_and_save_results(app_record)

    response = {}
    response[:app_record_id] = app_record.id
    response[:status] = @status
    response[:total_similar_apps] = @total_repackaged_apps
    response[:total_different_similar_apps] = @total_different_repackaged_apps
    response[:percentage_majority_signature] = @percentage_majority_signature.round(2)
    response[:percentage_same_signature] = @percentage_same_signature.round(2)
    response[:created_at] = DateTime.now

    RepackagedDetectionResult.transaction do
      detection_result = RepackagedDetectionResult.find_or_create_by!(app_record_id: app_record.id)
      response[:total_detections] = detection_result.total_detections + 1
      detection_result.update(response)
    end

    # additional response data which are not saved
    response[:pairwise_evaluated_apps] = @candidate_ids_certificate.size
    response[:signatures_number_of_apps] = @signatures_number_of_apps
    response[:signatures_of_apps] = @signatures_of_apps
    response[:similarity_scores] = SimilarAppRecord.where(app_record_id: app_record.id).to_a

    response
  end

  def respond_and_update_cached(result)
    # update detection number
    result.total_detections += 1
    result.save

    response = {}
    response[:app_record_id] = result.app_record_id
    response[:status] = result.status
    response[:total_similar_apps] = result.total_similar_apps
    response[:total_different_similar_apps] = result.total_different_similar_apps
    response[:percentage_majority_signature] = result.percentage_majority_signature.round(2)
    response[:percentage_same_signature] = result.percentage_same_signature.round(2)
    response[:total_detections] = result.total_detections + 1
    response[:created_at] = result.created_at

    response[:signatures_number_of_apps] = @signatures_number_of_apps

    response
  end


end
