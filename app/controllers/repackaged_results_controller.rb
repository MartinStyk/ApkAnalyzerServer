class RepackagedResultsController < ApplicationController

  before_action :authenticate_admin

  # GET /repackaged_results
  def index
    if filter_params[:status].nil?
      @repackaged_detection_results = RepackagedDetectionResult.all
    else
      @repackaged_detection_results = RepackagedDetectionResult.where(:status => filter_params[:status])
    end
    json_response(@repackaged_detection_results)
  end

  # GET /repackaged_results/id
  def show
    json_response(RepackagedDetectionResult.find(params[:id]))
  end

  def filter_params
    params.permit(
        :status
    )
  end
end
