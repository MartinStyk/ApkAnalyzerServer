class RenameRepackageDetectionResultTimestamp < ActiveRecord::Migration[5.1]
  def change
    # add new column but allow null values
    add_timestamps :repackaged_detection_results, null: true

    # backfill existing record with created_at and updated_at
    long_ago = DateTime.now
    RepackagedDetectionResult.update_all(created_at: long_ago, updated_at: long_ago)

    # change not null constraints
    change_column_null :repackaged_detection_results, :created_at, false
    change_column_null :repackaged_detection_results, :updated_at, false
  end
end
