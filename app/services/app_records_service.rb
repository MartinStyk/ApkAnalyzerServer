class AppRecordsService

  def save_with_duplicate_check(app_record_params, params)

    if duplicate_check(app_record_params[:app_hash], app_record_params[:android_id])
      return
    end

    AppRecord.transaction do
      @app_record = AppRecord.create!(app_record_params)

      assets = []
      params[:asset_hashes]&.each do |hash|
        assets << Asset.new(:file_hash => hash)
      end
      @app_record.assets.import assets, validate: false

      drawables = []
      params[:drawable_hashes]&.each do |hash|
        drawables << Drawable.new(:file_hash => hash)
      end
      @app_record.drawables.import drawables, validate: false

      layouts = []
      params[:layout_hashes]&.each do |hash|
        layouts << Layout.new(:file_hash => hash)
      end
      @app_record.layouts.import layouts, validate: false

      other_files = []
      params[:other_hashes]&.each do |hash|
        other_files << OtherFile.new(:file_hash => hash)
      end
      @app_record.other_files.import other_files, validate: false

      permissions = []
      params[:used_permissions]&.each do |name|
        permissions << Permission.new(:name => name)
      end
      @app_record.permissions.import permissions, validate: false

      @app_record
    end
  end

  def duplicate_check(app_hash, android_id)
    AppRecord.exists?(app_hash: app_hash, android_id: android_id)
  end
end