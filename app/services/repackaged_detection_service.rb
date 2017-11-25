class RepackagedDetectionService


  def detection(app_record)

    # filter initial dataset based on metrics which are hard to obfuscate
    candidates = AppRecord.where(:number_xmls => (app_record.number_xmls * 0.8).round..(app_record.number_xmls * 1.2).round,
                                 :number_xmls_with_different_name => (app_record.number_xmls_with_different_name * 0.8).round..(app_record.number_xmls_with_different_name * 1.2).round,
                                 :number_pngs => (app_record.number_pngs * 0.8).round..(app_record.number_pngs * 1.2).round,
                                 :number_pngs_with_different_name => (app_record.number_pngs_with_different_name * 0.8).round..(app_record.number_pngs_with_different_name * 1.2).round,
                                 :number_files_total => (app_record.number_files_total * 0.8).round..(app_record.number_files_total * 1.2).round,
                                 :total_number_of_classes => (app_record.total_number_of_classes * 0.8).round..(app_record.total_number_of_classes * 1.2).round,
                                 :total_number_of_classes_without_inner_classes => (app_record.total_number_of_classes_without_inner_classes * 0.8).round..(app_record.total_number_of_classes_without_inner_classes * 1.2).round)
                     .pluck(:id, :number_pngs, :number_layouts, :number_menus)


    candidates = AppRecord.all
                     .pluck(:id, :number_pngs, :number_layouts, :number_menus)

    # 8.times do
    #   candidates = candidates + candidates
    # end

    # among the results, similar apps based on
    repackaged_ids = []
    candidates.each do |candidate|
      #TODO hanlde when similarity_* is 0

      similarity_drawables = drawable_intersect_query(app_record.id, candidate[0]) / drawable_union_query(app_record.id, candidate[0]).to_f
      # similarity_layouts = [app_record.number_layouts, candidate[2]].min > 0 ?
      #                          Layout.where(:app_record_id => candidate[0]).select('file_hash').merge(Layout.where(:app_record_id => app_record.id).select('file_hash')).size.size / [app_record.number_layouts, candidate[2]].min.to_f : 0
      # similarity_menus = [app_record.number_menus, candidate[3]].min > 0 ?
      #                        Menu.where(:app_record_id => candidate[0]).select('file_hash').merge(Menu.where(:app_record_id => app_record.id).select('file_hash')).size / [app_record.number_menus, candidate[3]].min.to_f : 0

      if similarity_drawables > 0.8 #value taken according to paper
        repackaged_ids << candidate[0]
      end
    end

    #
    repackaged = AppRecord.find(repackaged_ids)
    signatures = {}
    repackaged.each do |record|
      signatures[record.cert_md5] = [] if signatures[record.cert_md5].nil?
      signatures[record.cert_md5] << record.id
      #TODO handle counts from upload_record
    end
    signatures.sort_by {|key, value| value.size}.reverse.to_h

    signatures
  end


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
  end

  def drawable_union_query(id_1, id_2)
    Drawable.where(:app_record_id => [id_1, id_2]).distinct('app_hash').count
  end

end