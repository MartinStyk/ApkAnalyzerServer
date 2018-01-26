class RepackagedDetectionController < ApplicationController

  before_action :authenticate_admin, only: :show
  before_action :authenticate_device, only: :index

  before_action :initialize_service

  before_action :init_app_record_by_id, only: :show
  before_action :init_app_record_by_hash, only: :index

  def show
    result = @repackaged_service.detection(@app_record)
    json_response result
  end

  def index
    result = @repackaged_service.detection(@app_record)
    json_response result
  end

  private

  def initialize_service
    @repackaged_service = RepackagedDetectionService.new
  end

  def init_app_record_by_id
    @app_record = AppRecord.find(params[:id])
  end

  def init_app_record_by_hash
    @app_record = AppRecord.find_by_app_hash(params[:app_hash])
  end

end
