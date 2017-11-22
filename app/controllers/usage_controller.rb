class UsageController < ApplicationController
  before_action :set_app_record_count
  before_action :set_different_devices
  before_action :set_different_apps
  before_action :set_result_object

  def index
      json_response(@result)
  end


  def set_app_record_count
    @app_records = AppRecord.all.count
  end

  def set_different_devices
    @different_devices = AppRecord.select("DISTINCT(app_records.android_id)").count;
  end

  def set_different_apps
    @different_apps = AppRecord.select("DISTINCT(app_records.app_hash)").count;
  end

  def set_result_object
    @result = {}
    @result[:app_records] = @app_records
    @result[:different_devices] = @different_devices
    @result[:different_apps] = @different_apps
  end

end