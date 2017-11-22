class FeaturesController < ApplicationController
  before_action :set_app_record
  before_action :set_app_record_feature, only: [:show]

  # GET /app_records/id/features
  def index
    json_response(@app_record.features)
  end

  # GET /app_records/id/features/id
  def show
    json_response(@feature)
  end

  def set_app_record
    @app_record = AppRecord.find(params[:app_record_id])
  end

  def set_app_record_feature
    @feature = @app_record.features.find_by!(id: params[:id]) if @app_record
  end

end
