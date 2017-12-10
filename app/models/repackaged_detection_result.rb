class RepackagedDetectionResult < ApplicationRecord
  belongs_to :app_record
  enum status: [:ok, :nok, :insufficient_data]
end
