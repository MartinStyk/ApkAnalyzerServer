class UsageController < ApplicationController
  before_action :authenticate

  before_action :get_usage_data

  def index
    json_response(@result)
  end

  def get_usage_data
    service = UsageStatisticsService.new
    @result = {}
    @result[:app_records] = service.app_record_count
    @result[:different_devices] = service.different_devices_count
    @result[:different_apps] = service.different_apps_count
    @result[:assets] = service.assets_count
    @result[:drawables] = service.drawables_count
    @result[:layouts] = service.layouts_count
    @result[:other_files] = service.other_files_count
    @result[:permissions] = service.permissions_count
    @result[:features] = service.features_count
  end

end