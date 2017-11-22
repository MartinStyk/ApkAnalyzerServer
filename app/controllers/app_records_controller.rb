class AppRecordsController < ApplicationController

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
    @app_record = AppRecordsService.new.save_with_duplicate_check(app_record_params, params)

    if @app_record.nil?
      json_response("", :conflict)
      return
    end

    json_response(@app_record, :created)
  end

  def app_record_params
    # whitelist params
    params.permit(
        :android_id,
        :app_hash,
        :analysis_mode,
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
        :asset_hashes,
        :drawable_hashes,
        :feature_names,
        :layout_hashes,
        :other_hashes,
        :package_classes,
    )
        # .require(:android_id)
        # .require(:android_id)
        # .require(:package_name)
        # .require(:version_code)
        # .require(:apk_size)
        # .require(:cert_md5)
        # .require(:number_activities)
        # .require(:number_services)
        # .require(:number_content_providers)
        # .require(:number_broadcast_receivers)
        # .require(:number_defined_permissions)
        # .require(:number_used_permissions)
        # .require(:number_features)
        # .require(:number_drawables)
        # .require(:number_layouts)
        # .require(:number_assets)
        # .require(:number_others)
        # .require(:number_package_classes)
        # .require(:number_other_classes)

  end

end
