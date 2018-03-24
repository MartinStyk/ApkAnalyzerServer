class RepackageDetectionResultAddNumberOfDetections < ActiveRecord::Migration[5.1]
  def change
    add_column :repackaged_detection_results, :total_detections, :integer, default: 0
  end
end
