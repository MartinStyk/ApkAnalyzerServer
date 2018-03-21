class RepackagedResultsStatisticsService

  def executed_detections
    RepackagedDetectionResult.all.count
  end

  def detection_result_ok
    RepackagedDetectionResult.where(status: :ok).count;
  end

  def detection_result_nok
    RepackagedDetectionResult.where(status: :nok).count;
  end

  def detection_result_insufficient_data
    RepackagedDetectionResult.where(status: :insufficient_data).count;
  end

  def top_executed_detections (status = [:ok, :nok, :insufficient_data])
    RepackagedDetectionResult
        .where(status: status)
        .group(:app_record)
        .order("count_all desc")
        .limit(50)
        .count()
        .transform_keys {|app| [app.id, app.package_name, app.version_code]}
  end

end