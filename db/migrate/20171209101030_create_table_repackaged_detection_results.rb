class CreateTableRepackagedDetectionResults < ActiveRecord::Migration[5.1]
  def change
    create_table :repackaged_detection_results do |t|
      t.references :app_record
      t.references :upload_record

      t.integer :status, default: 0
      t.integer :total_repackaged_apps
      t.integer :total_different_repackaged_apps

      t.float :percentage_same_signature
      t.float :percentage_majority_signature
    end
  end
end
