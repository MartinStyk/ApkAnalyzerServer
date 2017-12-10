class PermissionsController < ApplicationController

  before_action :authenticate_admin
  before_action :set_app_record
  before_action :set_app_record_permission, only: [:show]

  # GET /app_records/id/permission
  def index
    json_response(@app_record.permissions)
  end

  # GET /app_records/id/permission/id
  def show
    json_response(@permission)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_permission
    @permission = @app_record.permissions.find_by!(id: params[:id]) if @app_record
  end

end
