class UsageStatisticsService

  # Total number of uploaded apps
  def app_record_count
    UploadRecord.all.count
  end

  # Number of distinct uploaded apps
  def different_apps_count
    AppRecord.all.count;
  end

  def different_package_names_count
    AppRecord.names.count;
  end

  def different_package_name_and_version_count
    AppRecord.names_and_versions.count;
  end

  def different_devices_count
    UploadRecord.select("DISTINCT(upload_records.android_id)").count;
  end

  def permissions_count
    Permission.all.count;
  end

  def filtered_drawables_count
    FilteredDrawable.all.count;
  end

  def library_drawable_hashes_count
    LibraryDrawableHash.all.count;
  end

end