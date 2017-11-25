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

    # candidates = AppRecord.all
    #                  .pluck(:id, :number_pngs, :number_layouts, :number_menus)

    # among the results, similar apps based on
    repackaged_ids = []
    candidates.each do |candidate|
      #TODO hanlde when similarity_* is 0
      similarity_drawables = [app_record.number_menus, candidate[1]].min > 0 ? Drawable.where(:app_record_id => candidate[0]).where(:app_record_id => app_record.id).count / [app_record.number_pngs, candidate[1]].min : 0
      similarity_layouts = [app_record.number_menus, candidate[2]].min > 0 ? Layout.where(:app_record_id => candidate[0]).where(:app_record_id => app_record.id).count / [app_record.number_layouts, candidate[2]].min : 0
      similarity_menus = [app_record.number_menus, candidate[3]].min > 0 ? Menu.where(:app_record_id => candidate[0]).where(:app_record_id => app_record.id).count / [app_record.number_menus, candidate[3]].min : 0

      if similarity_drawables + similarity_layouts + similarity_menus > 0.623 * 3 #value taken according to paper
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

end