class RepackagedController < ApplicationController

  before_action :initialize_service

  def show
    result = @repackaged_service.detection(AppRecord.find(params[:id]))
    json_response result
  end


  private

  def initialize_service
    @repackaged_service = RepackagedDetectionService.new
  end
end
