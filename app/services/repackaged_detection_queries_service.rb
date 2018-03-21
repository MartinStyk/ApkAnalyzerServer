class RepackagedDetectionQueriesService


  # filter initial dataset based on metrics which are hard to obfuscate
  # returns array of arrays [id, certificate_hash] of such app records
  def filter_initial_dataset(app_record)

    delta = 0.2

    AppRecord.where(:number_xmls => (app_record.number_xmls * (1 - delta)).round..(app_record.number_xmls * (1 + delta)).round,
                    :number_xmls_with_different_name => (app_record.number_xmls_with_different_name * (1 - delta)).round..(app_record.number_xmls_with_different_name * (1 + delta)).round,
                    :number_drawables_by_extension => (app_record.number_drawables_by_extension * (1 - delta)).round..(app_record.number_drawables_by_extension * (1 + delta)).round,
                    :number_pngs_with_different_name => (app_record.number_pngs_with_different_name * (1 - delta)).round..(app_record.number_pngs_with_different_name * (1 + delta)).round,
                    :number_files_total => (app_record.number_files_total * (1 - delta)).round..(app_record.number_files_total * (1 + delta)).round,
                    :total_number_of_classes => (app_record.total_number_of_classes * (1 - delta)).round..(app_record.total_number_of_classes * (1 + delta)).round,
                    :total_number_of_classes_without_inner_classes => (app_record.total_number_of_classes_without_inner_classes * (1 - delta)).round..(app_record.total_number_of_classes_without_inner_classes * (1 + delta)).round)
        .pluck(:id, :package_name, :certificate_hash)
        .last(400)
  end

  def drawable_intersect_query(id_1, id_2)
    result = ActiveRecord::Base.connection.execute(" SELECT count(file_hash)
    FROM (
             SELECT file_hash
    FROM filtered_drawables
    WHERE app_record_id = #{id_1}
    INTERSECT
    SELECT file_hash
    FROM filtered_drawables
    WHERE app_record_id = #{id_2}
    ) AS foo;")

    # this works with sqlite
    # result[0][0]

    # this works with postgres
    result.getvalue(0, 0)

    # this works as in memory check
    # (Drawable.where(app_record_id: id_1) & (Drawable.where(app_record_id: id_1))).size
  end

  def drawable_union_query(id_1, id_2)
    FilteredDrawable.select("DISTINCT file_hash").where(app_record_id: [id_1, id_2]).count
  end

end