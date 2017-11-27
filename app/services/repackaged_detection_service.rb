class RepackagedDetectionService


  def detection(app_record)

    # filter initial dataset based on metrics which are hard to obfuscate
    candidate_ids = AppRecord.where(:number_xmls => (app_record.number_xmls * 0.8).round..(app_record.number_xmls * 1.2).round,
                                    :number_xmls_with_different_name => (app_record.number_xmls_with_different_name * 0.8).round..(app_record.number_xmls_with_different_name * 1.2).round,
                                    :number_pngs => (app_record.number_pngs * 0.8).round..(app_record.number_pngs * 1.2).round,
                                    :number_pngs_with_different_name => (app_record.number_pngs_with_different_name * 0.8).round..(app_record.number_pngs_with_different_name * 1.2).round,
                                    :number_files_total => (app_record.number_files_total * 0.8).round..(app_record.number_files_total * 1.2).round,
                                    :total_number_of_classes => (app_record.total_number_of_classes * 0.8).round..(app_record.total_number_of_classes * 1.2).round,
                                    :total_number_of_classes_without_inner_classes => (app_record.total_number_of_classes_without_inner_classes * 0.8).round..(app_record.total_number_of_classes_without_inner_classes * 1.2).round)
                        .pluck(:id)

    # find similar apps based on drawable similarity
    repackaged_ids = []
    candidate_ids.each do |candidate_id|
      similarity_ratio_drawables = drawable_intersect_query(app_record.id, candidate_id) / drawable_union_query(app_record.id, candidate_id).to_f

      if similarity_ratio_drawables > 0.8
        repackaged_ids << candidate_id
        SimilarAppRecord.find_or_create_by!(app_record: app_record, app_record_similar_id: candidate_id, score: similarity_ratio_drawables)
      end
    end

    # create hash signature -> number of apps
    repackaged = AppRecord.find(repackaged_ids)
    signatures = {}
    repackaged.each do |record|
      signatures[record.cert_md5] = [] if signatures[record.cert_md5].nil?
      signatures[record.cert_md5] << UploadRecord.where(app_record_id: record.id).count
    end
    signatures.sort_by {|key, value| value.size}.reverse.to_h

    signatures
  end


  private

  def drawable_intersect_query(id_1, id_2)
    result = ActiveRecord::Base.connection.execute(" SELECT count(*)
    FROM (
             SELECT file_hash
    FROM drawables
    WHERE app_record_id = #{id_1}
    INTERSECT
    SELECT file_hash
    FROM drawables
    WHERE app_record_id = #{id_2}
    );")
    result[0][0]

    # res_2 = (Drawable.where(app_record_id: id_1) & (Drawable.where(app_record_id: id_1))).size
  end

  def drawable_union_query(id_1, id_2)
    Drawable.where(app_record_id: [id_1, id_2]).distinct('app_hash').count
  end

end