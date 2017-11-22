class AssetsController < ApplicationController
  before_action :set_app_record
  before_action :set_app_record_asset, only: [:show]

  # GET /app_records/id/permission
  def index
    json_response(@app_record.assets)
  end

  # GET /app_records/id/asset/id
  def show
    json_response(@asset)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_asset
    @asset = @app_record.assets.find_by!(id: params[:id]) if @app_record
  end

end
