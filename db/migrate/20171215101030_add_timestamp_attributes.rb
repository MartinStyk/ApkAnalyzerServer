class AddTimestampAttributes < ActiveRecord::Migration[5.1]
  def change
    add_column :upload_records, :created_at, :datetime
    add_column :upload_records, :updated_at, :datetime

    UploadRecord.update_all(created_at: Time.now)
    UploadRecord.update_all(updated_at: Time.now)

    change_column :upload_records, :created_at, :datetime, null: false
    change_column :upload_records, :updated_at, :datetime, null: false

    add_column :repackaged_detection_results, :created_at, :datetime
    add_column :repackaged_detection_results, :updated_at, :datetime

    RepackagedDetectionResult.update_all(created_at: Time.now)
    RepackagedDetectionResult.update_all(updated_at: Time.now)

    change_column :repackaged_detection_results, :created_at, :datetime, null: false
    change_column :repackaged_detection_results, :updated_at, :datetime, null: false
  end
end
