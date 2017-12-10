class RepackagedDetectionController < ApplicationController

#  before_action :authenticate_admin, only: :show
#  before_action :authenticate_device, only: :create

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
