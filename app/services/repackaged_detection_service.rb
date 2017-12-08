class RepackagedDetectionService

  def detection(app_record)
    @query_service = RepackagedDetectionQueriesService.new

    # filter initial dataset based on metrics which are hard to obfuscate
    candidate_ids = @query_service.filter_initial_dataset app_record

    # find similar apps based on drawable similarity
    repackaged = find_similar  app_record, candidate_ids

    # create hash signature -> number of apps
    signature_to_number_of_apps repackaged
  end

  private
  
  def signature_to_number_of_apps(repackaged)
    signatures = {}
    repackaged.each do |record|
      signatures[record.cert_md5] = [] if signatures[record.cert_md5].nil?
      signatures[record.cert_md5] << UploadRecord.where(app_record_id: record.id).count
    end
    signatures.sort_by {|key, value| value.size}.reverse.to_h

    signatures
  end

  def find_similar(app_record, candidate_ids)
    repackaged_ids = []
    candidate_ids.each do |candidate_id|
      similarity_ratio_drawables = @query_service.drawable_intersect_query(app_record.id, candidate_id) / @query_service.drawable_union_query(app_record.id, candidate_id).to_f

      if similarity_ratio_drawables > 0.8
        repackaged_ids << candidate_id
        SimilarAppRecord.find_or_create_by!(app_record: app_record, app_record_similar_id: candidate_id, score: similarity_ratio_drawables)
      end
    end

    AppRecord.find(repackaged_ids)
  end

end