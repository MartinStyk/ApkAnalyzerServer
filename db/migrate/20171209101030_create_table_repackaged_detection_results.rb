class CreateTableRepackagedDetectionResults < ActiveRecord::Migration[5.1]
  def change
    create_table :repacakged_detection_results do |t|
      t.references :app_record
      t.string :android_id
      t.string :status

      t.float :percentage_same_signature
      t.float :percentage_majority_signature
    end
  end
end
