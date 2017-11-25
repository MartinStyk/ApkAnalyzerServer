class RepackagedController < ApplicationController
  def show
    result = RepackagedDetectionService.new.detection(AppRecord.find(params[:id]))
    json_response result
  end

end
