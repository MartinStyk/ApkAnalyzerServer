class RepackagedDetectionController < ApplicationController
  before_action :authenticate_admin, only: :show
  before_action :authenticate_device, only: :index

  before_action :initialize_service

  before_action :init_app_record_by_id, only: :show
  before_action :init_app_record_by_hash, only: :index

  def show
    run_detection params[:cache]
  end

  def index
    run_detection params[:cache]
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

  def run_detection(cache)
    result = if cache.nil? || cache == "true"
               @repackaged_service.detection_with_caching(@app_record)
             else
               @repackaged_service.detection(@app_record)
             end
    json_response result
  end
end
