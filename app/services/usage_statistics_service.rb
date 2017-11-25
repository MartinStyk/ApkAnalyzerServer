class UsageStatisticsService

  def app_record_count
    AppRecord.all.count
  end

  def different_devices_count
    AppRecord.select("DISTINCT(app_records.android_id)").count;
  end

  def different_apps_count
    AppRecord.select("DISTINCT(app_records.app_hash)").count;
  end

  def permissions_count
    Permission.all.count;
  end

  def features_count
    Feature.all.count;
  end

  def assets_count
    Menu.all.count;
  end

  def layouts_count
    Layout.all.count;
  end

  def drawables_count
    Drawable.all.count;
  end

  def other_files_count
    OtherFile.all.count;
  end

end