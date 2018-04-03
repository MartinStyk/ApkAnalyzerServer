class AppRecordTimestamp < ActiveRecord::Migration[5.1]
  def change
    # add new column but allow null values
    add_timestamps :app_records, null: true

    # backfill existing record with created_at and updated_at
    long_ago = DateTime.now
    AppRecord.update_all(created_at: long_ago, updated_at: long_ago)

    # change not null constraints
    change_column_null :app_records, :created_at, false
    change_column_null :app_records, :updated_at, false
  end
end
