class RenameRepackageDetectionResultAttributes < ActiveRecord::Migration[5.1]
  def change
    rename_column :repackaged_detection_results, :total_different_repackaged_apps, :total_different_similar_apps
    rename_column :repackaged_detection_results, :total_repackaged_apps, :total_similar_apps
  end
end
