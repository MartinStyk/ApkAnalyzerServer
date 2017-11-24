class AppRecordsService
  def save_with_duplicate_check(app_record_params, upload_record_params,
                                permission_params, feature_params,
                                asset_params, drawable_params, layout_params,
                                other_file_params)

    # if duplicate_check(app_record_params[:app_hash], app_record_params[:android_id])
    #   return
    # end

    AppRecord.transaction do
      @app_record = AppRecord.find_or_create_by!(app_record_params)
      @upload_record = @app_record.upload_records.create!(upload_record_params)

      @app_record.assets.import asset_params, validate: false
      @app_record.drawables.import drawable_params, validate: false
      @app_record.layouts.import layout_params, validate: false
      @app_record.other_files.import other_file_params, validate: false
      @app_record.permissions.import permission_params, validate: false
      @app_record.features.import feature_params, validate: false

      @app_record
    end
  end

  def duplicate_check(app_hash, android_id)
    AppRecord.exists?(app_hash: app_hash, android_id: android_id)
  end
end