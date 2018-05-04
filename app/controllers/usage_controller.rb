class UsageController < ApplicationController

  before_action :authenticate_admin
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
    # @result[:different_package_names] = service.different_package_names_count
    # @result[:different_package_name_and_version_count] = service.different_package_name_and_version_count
    # @result[:filtered_drawables_count] = service.filtered_drawables_count
    @result[:library_drawable_hashes_count] = service.library_drawable_hashes_count

    @result[:permissions] = service.permissions_count
  end

end