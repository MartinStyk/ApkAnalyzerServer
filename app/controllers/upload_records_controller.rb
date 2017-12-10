class UploadRecordsController < ApplicationController

  before_action :authenticate_admin
  before_action :set_app_record
  before_action :set_app_record_uploads, only: [:show]

  # GET /app_records/id/features
  def index
    json_response(@app_record.upload_records)
  end

  # GET /app_records/id/features/id
  def show
    json_response(@upload_records)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_uploads
    @upload_records = @app_record.features.find_by!(id: params[:id]) if @app_record
  end

end
