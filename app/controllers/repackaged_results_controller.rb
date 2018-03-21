class RepackagedResultsController < ApplicationController
  before_action :authenticate_admin
  before_action :get_statistics, only: :statistics

  # GET /repackaged_results
  def index
    if filter_params[:status].nil?
      @repackaged_detection_results = RepackagedDetectionResult.all
    else
      @repackaged_detection_results = RepackagedDetectionResult.where(status: filter_params[:status])
    end
    json_response(@repackaged_detection_results)
  end

  # GET /repackaged_results/id
  def show
    json_response(RepackagedDetectionResult.find(params[:id]))
  end

  # GET /repackaged_results/statistics
  def statistics
    json_response(@stats)
  end

  def filter_params
    params.permit(
        :status
    )
  end

  def get_statistics
    service = RepackagedResultsStatisticsService.new
    @stats = {}
    @stats[:executed_detections] = service.executed_detections
    @stats[:detection_result_ok] = service.detection_result_ok
    @stats[:detections_nok] = service.detection_result_nok
    @stats[:detections_insufficient_data] = service.detection_result_insufficient_data
    @stats[:top_executed_detections] = service.top_executed_detections
    @stats[:top_executed_detections_ok] = service.top_executed_detections(:ok)
    @stats[:top_executed_detections_nok] = service.top_executed_detections(:nok)
    @stats[:top_executed_detections_insufficient_data] = service.top_executed_detections(:insufficient_data)
  end
end
