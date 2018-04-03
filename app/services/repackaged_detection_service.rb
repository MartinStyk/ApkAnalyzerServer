class RepackagedDetectionService

  # Look at repackaged detection results
  # In case the detection was run in last 7 days, return its results
  # Otherwise, run full detection
  def detection_with_caching(app_record)

    existing = RepackagedDetectionResult.find_by(app_record_id: app_record.id)

    cached_data_available = !existing.nil? && existing.created_at > DateTime.now - 14

    detection(app_record, cached_data_available, existing)
  end


  # Run detection
  # Compute group of similar apps, decide detection status and return results
  def detection(app_record, use_cached = false, existing_detection_result)

    # find group of similar apps and create hash app id -> certificate
    @repackaged_ids_certificate = find_similar_apps(app_record, use_cached, existing_detection_result)

    # create hash signature -> number of apps
    @signatures_number_of_apps = signature_to_number_of_apps

    # create hash signature -> ids of apps
    @signatures_of_apps = signature_to_apps

    # decide the result of detection and compute similarity metrics
    evaluate app_record

    # create response
    respond_and_save_results app_record, use_cached
  end

  private

  # Finds the cluster of similar apps
  # If cached results are available and valid (fresh), return it
  # If cached results are available but not fresh, compute similarity of apps added after detection result was created
  # If cached results are not available, run full detection
  def find_similar_apps(app_record, use_cached = false, existing_detection_result)

    repackaged_ids_certificate = [[app_record.id, app_record.package_name, app_record.certificate_hash]]

    if use_cached

      # find cached results of similar apps
      repackaged_ids_certificate = repackaged_ids_certificate + app_record.similar_records.pluck(:id, :package_name, :certificate_hash)

    else

      @query_service = RepackagedDetectionQueriesService.new

      if existing_detection_result.nil?

        # no cached results - compute it hard way
        @candidate_ids_certificate = @query_service.filter_initial_dataset app_record
        repackaged_ids_certificate = repackaged_ids_certificate + drawable_similarity_check(app_record)

      else

        # compute pairwise similarity only for apps newer than most recent detection of this app
        @candidate_ids_certificate = @query_service.filter_initial_dataset app_record, existing_detection_result[:created_at]
        repackaged_ids_certificate = repackaged_ids_certificate + app_record.similar_records.pluck(:id, :package_name, :certificate_hash) + drawable_similarity_check(app_record)
      end

    end

    repackaged_ids_certificate
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

  def drawable_similarity_check(app_record)
    similarity_threshold = 0.8

    repackaged_ids_certificate = []

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

  def respond_and_save_results(app_record, uses_cached = false)
    response = {}
    response[:app_record_id] = app_record.id
    response[:status] = @status
    response[:total_similar_apps] = @total_repackaged_apps
    response[:total_different_similar_apps] = @total_different_repackaged_apps
    response[:percentage_majority_signature] = @percentage_majority_signature.round(2)
    response[:percentage_same_signature] = @percentage_same_signature.round(2)

    RepackagedDetectionResult.transaction do
      detection_result = RepackagedDetectionResult.find_or_create_by!(app_record_id: app_record.id)
      response[:created_at] = uses_cached ? detection_result.created_at : DateTime.now
      response[:total_detections] = detection_result.total_detections + 1
      detection_result.update(response)
    end

    # additional response data which are not saved
    response[:pairwise_evaluated_apps] = @candidate_ids_certificate.nil? ? 0 : @candidate_ids_certificate.size
    response[:signatures_number_of_apps] = @signatures_number_of_apps
    response[:signatures_of_apps] = @signatures_of_apps
    response[:similarity_scores] = SimilarAppRecord.where(app_record_id: app_record.id).to_a

    response
  end

end
