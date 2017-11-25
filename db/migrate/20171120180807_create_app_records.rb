class CreateAppRecords < ActiveRecord::Migration[5.1]
  def change
    create_table :app_records do |t|
      t.integer :app_hash
      t.string :package_name
      t.string :application_name
      t.string :version_name
      t.integer :version_code
      t.integer :apk_size
      t.integer :min_sdk_version
      t.integer :target_sdk_version
      t.string :sign_algorithm
      t.string :public_key_md5
      t.string :cert_md5
      t.integer :serial_number

      t.integer :number_activities
      t.integer :activities_aggregated_hash
      t.integer :number_services
      t.integer :services_aggregated_hash
      t.integer :number_content_providers
      t.integer :providers_aggregated_hash
      t.integer :number_broadcast_receivers
      t.integer :receivers_aggregated_hash

      t.integer :number_defined_permissions
      t.integer :defined_permissions_aggregated_hash

      t.integer :number_used_permissions
      t.integer :used_permissions_aggregated_hash

      t.integer :number_features
      t.integer :features_aggregated_hash

      t.string :dex_hash
      t.string :arsc_hash
      t.string :manifest_hash

      t.integer :number_drawables
      t.integer :number_layouts
      t.integer :number_menus
      t.integer :number_files_total
      t.integer :number_pngs
      t.integer :number_pngs_with_different_name
      t.integer :number_xmls
      t.integer :number_xmls_with_different_name

      t.integer :pngs_aggregated_hash
      t.integer :layouts_aggregated_hash
      t.integer :menus_aggregated_hash

      t.integer :number_different_drawables
      t.integer :number_different_layouts
      t.integer :png_drawables
      t.integer :nine_patch_drawables
      t.integer :jpg_drawables
      t.integer :gif_drawables
      t.integer :xml_drawables
      t.integer :ldpi_drawables
      t.integer :mdpi_drawables
      t.integer :hdpi_drawables
      t.integer :xhdpi_drawables
      t.integer :xxhdpi_drawables
      t.integer :xxxhdpi_drawables
      t.integer :nodpi_drawables
      t.integer :tvdpi_drawables
      t.integer :unspecified_dpi_drawables

      t.integer :classes_aggregated_hash
      t.integer :total_number_of_classes
      t.integer :total_number_of_classes_without_inner_classes

      t.index :app_hash
    end
  end
end
