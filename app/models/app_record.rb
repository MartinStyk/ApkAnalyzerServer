class AppRecord < ApplicationRecord
  has_many :upload_records

  has_many :drawables
  has_many :layouts
  has_many :permissions

  has_many(:similar_app_records, foreign_key: :app_record_id, dependent: :destroy)
  has_many(:reverse_similar_app_records, class_name: :AppRecord, foreign_key: :app_record_similar_id, dependent: :destroy)
  has_many :app_records, through: :similar_app_records, source: :app_record_similar
end
