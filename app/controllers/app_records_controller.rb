class AppRecordsController < ApplicationController
  before_action :authenticate, except: :create


  # GET /app_records
  def index
    @app_records = AppRecord.all
    json_response(@app_records)
  end

  # GET /app_records/id
  def show
    json_response(AppRecord.find(params[:id]))
  end

  # POST /app_records
  def create
    @app_record = AppRecordsService.new.save_with_duplicate_check(app_record_params, upload_record_params,
                                                                  permission_params, feature_params,
                                                                  asset_params, drawable_params, layout_params,
                                                                  other_file_params)

    if @app_record.nil?
      json_response("", :conflict)
      return
    end

    json_response(@app_record, :created)
  end

  def app_record_params
    # whitelist params
    params.permit(
        :app_hash,
        :package_name,
        :application_name,
        :version_name,
        :version_code,
        :source,
        :apk_size,
        :min_sdk_version,
        :target_sdk_version,
        :sign_algorithm,
        :public_key_md5,
        :cert_md5,
        :serial_number,
        :issuer_name,
        :issuer_organization,
        :issuer_country,
        :subject_name,
        :subject_organization,
        :subject_country,
        :number_activities,
        :activities_aggregated_hash,
        :number_services,
        :services_aggregated_hash,
        :number_content_providers,
        :providers_aggregated_hash,
        :number_broadcast_receivers,
        :receivers_aggregated_hash,
        :number_defined_permissions,
        :defined_permissions_aggregated_hash,
        :number_used_permissions,
        :used_permissions_aggregated_hash,
        :number_features,
        :features_aggregated_hash,
        :dex_hash,
        :arsc_hash,
        :number_drawables,
        :number_layouts,
        :number_assets,
        :number_others,
        :drawables_aggregated_hash,
        :layouts_aggregated_hash,
        :assets_aggregated_hash,
        :other_aggregated_hash,
        :number_different_drawables,
        :number_different_layouts,
        :png_drawables,
        :nine_patch_drawables,
        :jpg_drawables,
        :gif_drawables,
        :xml_drawables,
        :ldpi_drawables,
        :mdpi_drawables,
        :hdpi_drawables,
        :xhdpi_drawables,
        :xxhdpi_drawables,
        :xxxhdpi_drawables,
        :nodpi_drawables,
        :tvdpi_drawables,
        :unspecified_dpi_drawables,
        :package_classes_aggregated_hash,
        :number_package_classes,
        :other_classes_aggregated_hash,
        :number_other_classes,
        :package_classes,
    )
  end

  def upload_record_params
    params.require(:analysis_mode)
    params.require(:android_id)
    params.permit(:analysis_mode, :android_id)
  end

  def permission_params
    array = params[:permissions]
    array.nil? ? [] : array.map {|name| Permission.new(:name => name)}
  end

  def feature_params
    array = params[:features]
    array.nil? ? [] : array.map {|name| Feature.new(:name => name)}
  end

  def asset_params
    array = params[:asset_hashes]
    array.nil? ? [] : array.map {|name| Asset.new(:file_hash => name)}
  end

  def drawable_params
    array = params[:drawable_hashes]
    array.nil? ? [] : array.map {|name| Drawable.new(:file_hash => name)}
  end

  def layout_params
    array = params[:layout_hashes]
    array.nil? ? [] : array.map {|name| Layout.new(:file_hash => name)}
  end

  def other_file_params
    array = params[:other_hashes]
    array.nil? ? [] : array.map {|name| OtherFile.new(:file_hash => name)}
  end


end
