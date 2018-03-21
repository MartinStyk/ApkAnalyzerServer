## Routes                       
                       
                       Prefix Verb URI Pattern                                                  Controller#Action
            names_app_records GET  /app_records/names(.:format)                                 app_records#names
   names_versions_app_records GET  /app_records/names_versions(.:format)                        app_records#names_versions
    app_record_upload_records GET  /app_records/:app_record_id/upload_records(.:format)         upload_records#index
     app_record_upload_record GET  /app_records/:app_record_id/upload_records/:id(.:format)     upload_records#show
       app_record_permissions GET  /app_records/:app_record_id/permissions(.:format)            permissions#index
        app_record_permission GET  /app_records/:app_record_id/permissions/:id(.:format)        permissions#show
app_record_filtered_drawables GET  /app_records/:app_record_id/filtered_drawables(.:format)     filtered_drawables#index
 app_record_filtered_drawable GET  /app_records/:app_record_id/filtered_drawables/:id(.:format) filtered_drawables#show
                  app_records GET  /app_records(.:format)                                       app_records#index
                              POST /app_records(.:format)                                       app_records#create
                   app_record GET  /app_records/:id(.:format)                                   app_records#show
   repackaged_detection_index GET  /repackaged_detection(.:format)                              repackaged_detection#index
         repackaged_detection GET  /repackaged_detection/:id(.:format)                          repackaged_detection#show
statistics_repackaged_results GET  /repackaged_results/statistics(.:format)                     repackaged_results#statistics
           repackaged_results GET  /repackaged_results(.:format)                                repackaged_results#index
            repackaged_result GET  /repackaged_results/:id(.:format)                            repackaged_results#show
                  usage_index GET  /usage(.:format)                                             usage#index
              available_index GET  /available(.:format)                                         available#index
