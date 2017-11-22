class LayoutsController < ApplicationController
  before_action :set_app_record
  before_action :set_app_record_layout, only: [:show]

  # GET /app_records/id/layouts
  def index
    json_response(@app_record.layouts)
  end

  # GET /app_records/id/layouts/id
  def show
    json_response(@layout)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_layout
    @layout = @app_record.layouts.find_by!(id: params[:id]) if @app_record
  end

end
