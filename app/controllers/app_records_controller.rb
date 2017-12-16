class AppRecordsController < ApplicationController

  before_action :authenticate_admin, except: :create
  before_action :authenticate_device, only: :create

  before_action :check_apk_analyzer_version, only: :create


  # GET /app_records
  def index
    @app_records = AppRecord.filter query_params
    json_response(@app_records)
  end

  # GET /app_records/id
  def show
    json_response(AppRecord.find(params[:id]))
  end

  # GET /app_records/names
  def names
    json_response(AppRecord.names)
  end

  # GET /app_records/names_versions
  def names_versions
    json_response(AppRecord.names_and_versions)
  end


  # POST /app_records
  def create
    @app_record = AppRecordsService.new.save_with_duplicate_check(app_record_params, upload_record_params,
                                                                  permission_params, drawable_params, layout_params)

    if @app_record.nil?
      json_response("", :conflict)
      return
    end

    json_response(@app_record, :created)
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

  def drawable_params
    array = params[:png_hashes]
    array.nil? ? [] : array.map {|name| Drawable.new(:file_hash => name)}
  end

  def layout_params
    array = params[:layout_hashes]
    array.nil? ? [] : array.map {|name| Layout.new(:file_hash => name)}
  end

  def apk_analyzer_version
    params.require(:apk_analyzer_version)
    params[:apk_analyzer_version]
  end

  def query_params
    # whitelist params
    params.permit(
        :package_name,
        :version_name,
        :version_code,
        :certificate_hash,
        :android_id
    )
  end

  def app_record_params
    # whitelist params
    params.require(:app_hash)
    params.require(:package_name)
    params.require(:application_name)
    params.require(:version_name)
    params.require(:version_code)
    params.require(:apk_size)
    params.require(:sign_algorithm)
    params.require(:public_key_md5)
    params.require(:certificate_hash)
    params.require(:serial_number)

    params.require(:number_activities)
    params.require(:activities_aggregated_hash)
    params.require(:number_services)
    params.require(:services_aggregated_hash)
    params.require(:number_content_providers)
    params.require(:providers_aggregated_hash)
    params.require(:number_broadcast_receivers)
    params.require(:receivers_aggregated_hash)

    params.require(:number_defined_permissions)
    params.require(:defined_permissions_aggregated_hash)

    params.require(:number_used_permissions)
    params.require(:used_permissions_aggregated_hash)

    params.require(:number_features)
    params.require(:features_aggregated_hash)

    params.require(:dex_hash)
    params.require(:arsc_hash)
    params.require(:manifest_hash)

    params.require(:number_drawables)
    params.require(:number_layouts)
    params.require(:number_menus)
    params.require(:number_files_total)
    params.require(:number_pngs)
    params.require(:number_pngs_with_different_name)
    params.require(:number_xmls)
    params.require(:number_xmls_with_different_name)

    params.require(:pngs_aggregated_hash)
    params.require(:layouts_aggregated_hash)
    params.require(:menus_aggregated_hash)

    params.require(:number_different_drawables)
    params.require(:number_different_layouts)
    params.require(:png_drawables)
    params.require(:nine_patch_drawables)
    params.require(:jpg_drawables)
    params.require(:gif_drawables)
    params.require(:xml_drawables)
    params.require(:ldpi_drawables)
    params.require(:mdpi_drawables)
    params.require(:hdpi_drawables)
    params.require(:xhdpi_drawables)
    params.require(:xxhdpi_drawables)
    params.require(:xxxhdpi_drawables)
    params.require(:nodpi_drawables)
    params.require(:tvdpi_drawables)
    params.require(:unspecified_dpi_drawables)

    params.require(:classes_aggregated_hash)
    params.require(:total_number_of_classes)
    params.require(:total_number_of_classes_without_inner_classes)

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
        :certificate_hash,
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

  private

  # check version of mobile client sending data
  # this is the place to halt upload action if android client is not compatible with server
  def check_apk_analyzer_version
    if apk_analyzer_version < 12
      json_response('Unsupported client version', :bad_request)
    end
  end

end
