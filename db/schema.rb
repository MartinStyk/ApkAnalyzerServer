# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20171121131030) do

  create_table "app_records", force: :cascade do |t|
    t.string "android_id"
    t.integer "app_hash"
    t.string "analysis_mode"
    t.string "package_name"
    t.string "application_name"
    t.string "version_name"
    t.integer "version_code"
    t.string "source"
    t.integer "apk_size"
    t.integer "min_sdk_version"
    t.integer "target_sdk_version"
    t.string "sign_algorithm"
    t.string "public_key_md5"
    t.string "cert_md5"
    t.integer "serial_number"
    t.string "issuer_name"
    t.string "issuer_organization"
    t.string "issuer_country"
    t.string "subject_name"
    t.string "subject_organization"
    t.string "subject_country"
    t.integer "number_activities"
    t.integer "activities_aggregated_hash"
    t.integer "number_services"
    t.integer "services_aggregated_hash"
    t.integer "number_content_providers"
    t.integer "providers_aggregated_hash"
    t.integer "number_broadcast_receivers"
    t.integer "receivers_aggregated_hash"
    t.integer "number_defined_permissions"
    t.integer "defined_permissions_aggregated_hash"
    t.integer "number_used_permissions"
    t.integer "used_permissions_aggregated_hash"
    t.integer "number_features"
    t.integer "features_aggregated_hash"
    t.string "dex_hash"
    t.string "arsc_hash"
    t.integer "number_drawables"
    t.integer "number_layouts"
    t.integer "number_assets"
    t.integer "number_others"
    t.integer "drawables_aggregated_hash"
    t.integer "layouts_aggregated_hash"
    t.integer "assets_aggregated_hash"
    t.integer "other_aggregated_hash"
    t.integer "number_different_drawables"
    t.integer "number_different_layouts"
    t.integer "png_drawables"
    t.integer "nine_patch_drawables"
    t.integer "jpg_drawables"
    t.integer "gif_drawables"
    t.integer "xml_drawables"
    t.integer "ldpi_drawables"
    t.integer "mdpi_drawables"
    t.integer "hdpi_drawables"
    t.integer "xhdpi_drawables"
    t.integer "xxhdpi_drawables"
    t.integer "xxxhdpi_drawables"
    t.integer "nodpi_drawables"
    t.integer "tvdpi_drawables"
    t.integer "unspecified_dpi_drawables"
    t.integer "package_classes_aggregated_hash"
    t.integer "number_package_classes"
    t.integer "other_classes_aggregated_hash"
    t.integer "number_other_classes"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "assets", force: :cascade do |t|
    t.string "file_hash"
    t.integer "app_record_id"
    t.index ["app_record_id"], name: "index_assets_on_app_record_id"
  end

  create_table "drawables", force: :cascade do |t|
    t.string "file_hash"
    t.integer "app_record_id"
    t.index ["app_record_id"], name: "index_drawables_on_app_record_id"
  end

  create_table "layouts", force: :cascade do |t|
    t.string "file_hash"
    t.integer "app_record_id"
    t.index ["app_record_id"], name: "index_layouts_on_app_record_id"
  end

  create_table "other_files", force: :cascade do |t|
    t.string "file_hash"
    t.integer "app_record_id"
    t.index ["app_record_id"], name: "index_other_files_on_app_record_id"
  end

  create_table "permissions", force: :cascade do |t|
    t.string "name"
    t.integer "app_record_id"
    t.index ["app_record_id"], name: "index_permissions_on_app_record_id"
  end

end