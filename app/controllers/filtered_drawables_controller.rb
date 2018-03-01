class FilteredDrawablesController < ApplicationController

  before_action :authenticate_admin
  before_action :set_app_record
  before_action :set_app_record_filtered_drawable, only: [:show]

  # GET /app_records/id/filtered_drawables
  def index
    json_response(@app_record.drawables)
  end

  # GET /app_records/id/filtered_drawables/id
  def show
    json_response(@drawable)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_filtered_drawable
    @drawable = @app_record.filtered_drawables.find_by!(id: params[:id]) if @app_record
  end

end
