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

ActiveRecord::Schema.define(version: 20180403135230) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "app_records", force: :cascade do |t|
    t.integer "app_hash"
    t.string "package_name"
    t.string "application_name"
    t.string "version_name"
    t.integer "version_code"
    t.integer "apk_size"
    t.integer "min_sdk_version"
    t.integer "target_sdk_version"
    t.string "sign_algorithm"
    t.string "public_key_md5"
    t.string "certificate_hash"
    t.integer "serial_number"
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
    t.string "manifest_hash"
    t.integer "number_drawables_by_folder"
    t.integer "number_layouts"
    t.integer "number_menus"
    t.integer "number_files_total"
    t.integer "number_drawables_by_extension"
    t.integer "number_pngs_with_different_name"
    t.integer "number_xmls"
    t.integer "number_xmls_with_different_name"
    t.integer "drawables_aggregated_hash"
    t.integer "layouts_aggregated_hash"
    t.integer "menus_aggregated_hash"
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
    t.integer "classes_aggregated_hash"
    t.integer "total_number_of_classes"
    t.integer "total_number_of_classes_without_inner_classes"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["app_hash"], name: "index_app_records_on_app_hash"
  end

  create_table "filtered_drawables", force: :cascade do |t|
    t.string "file_hash"
    t.string "file_name"
    t.bigint "app_record_id"
    t.index ["app_record_id"], name: "index_filtered_drawables_on_app_record_id"
    t.index ["file_hash"], name: "index_filtered_drawables_on_file_hash"
  end

  create_table "library_drawable_hashes", force: :cascade do |t|
    t.string "file_hash"
    t.index ["file_hash"], name: "index_library_drawable_hashes_on_file_hash", unique: true
  end

  create_table "permissions", force: :cascade do |t|
    t.string "name"
    t.bigint "app_record_id"
    t.index ["app_record_id"], name: "index_permissions_on_app_record_id"
  end

  create_table "repackaged_detection_results", force: :cascade do |t|
    t.bigint "app_record_id"
    t.bigint "upload_record_id"
    t.integer "status", default: 0
    t.integer "total_similar_apps"
    t.integer "total_different_similar_apps"
    t.float "percentage_same_signature"
    t.float "percentage_majority_signature"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.integer "total_detections", default: 0
    t.index ["app_record_id"], name: "index_repackaged_detection_results_on_app_record_id"
    t.index ["upload_record_id"], name: "index_repackaged_detection_results_on_upload_record_id"
  end

  create_table "similar_app_records", force: :cascade do |t|
    t.integer "app_record_id", null: false
    t.integer "app_record_similar_id", null: false
    t.float "score"
    t.index ["app_record_id"], name: "index_similar_app_records_on_app_record_id"
    t.index ["app_record_similar_id"], name: "index_similar_app_records_on_app_record_similar_id"
  end

  create_table "upload_records", force: :cascade do |t|
    t.string "android_id"
    t.string "analysis_mode"
    t.bigint "app_record_id"
    t.string "source"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.index ["app_record_id"], name: "index_upload_records_on_app_record_id"
  end

  create_table "users", force: :cascade do |t|
    t.string "email"
    t.string "password_digest"
  end

end
