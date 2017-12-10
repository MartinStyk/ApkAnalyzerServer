class AppRecordsController < ApplicationController

  before_action :authenticate_admin, except: :create
  before_action :authenticate_device, only: :create

  # GET /app_records
  def index
    @app_records = query_params[:package_name].nil? ? AppRecord.all : AppRecord.where(package_name: query_params[:package_name])
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
                                                                  menu_params, drawable_params, layout_params)

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
        :apk_size,
        :min_sdk_version,
        :target_sdk_version,
        :sign_algorithm,
        :public_key_md5,
        :cert_md5,
        :serial_number,

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
        :manifest_hash,

        :number_drawables,
        :number_layouts,
        :number_menus,
        :number_files_total,
        :number_pngs,
        :number_pngs_with_different_name,
        :number_xmls,
        :number_xmls_with_different_name,

        :pngs_aggregated_hash,
        :layouts_aggregated_hash,
        :menus_aggregated_hash,

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

        :classes_aggregated_hash,
        :total_number_of_classes,
        :total_number_of_classes_without_inner_classes
    )
  end

  def upload_record_params
    params.require(:analysis_mode)
    params.require(:android_id)
    params.require(:source)
    params.permit(:analysis_mode, :android_id, :source)
  end

  def permission_params
    array = params[:permissions]
    array.nil? ? [] : array.map {|name| Permission.new(:name => name)}
  end

  def feature_params
    array = params[:features]
    array.nil? ? [] : array.map {|name| Feature.new(:name => name)}
  end

  def menu_params
    array = params[:menu_hashes]
    array.nil? ? [] : array.map {|name| Menu.new(:file_hash => name)}
  end

  def drawable_params
    array = params[:png_hashes]
    array.nil? ? [] : array.map {|name| Drawable.new(:file_hash => name)}
  end

  def layout_params
    array = params[:layout_hashes]
    array.nil? ? [] : array.map {|name| Layout.new(:file_hash => name)}
  end

  def query_params
    # whitelist params
    params.permit(
        :package_name,
    )
  end

end
