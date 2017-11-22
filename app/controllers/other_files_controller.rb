class OtherFilesController < ApplicationController
  before_action :set_app_record
  before_action :set_app_record_file, only: [:show]

  # GET /app_records/id/other_files
  def index
    json_response(@app_record.other_files)
  end

  # GET /app_records/id/other_files/id
  def show
    json_response(@file)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_file
    @file = @app_record.other_files.find_by!(id: params[:id]) if @app_record
  end

end
