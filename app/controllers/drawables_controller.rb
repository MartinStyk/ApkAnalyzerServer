class DrawablesController < ApplicationController
  before_action :set_app_record
  before_action :set_app_record_drawable, only: [:show]

  # GET /app_records/id/drawables
  def index
    json_response(@app_record.drawables)
  end

  # GET /app_records/id/permission/id
  def show
    json_response(@drawable)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_drawable
    @drawable = @app_record.drawables.find_by!(id: params[:id]) if @app_record
  end

end
