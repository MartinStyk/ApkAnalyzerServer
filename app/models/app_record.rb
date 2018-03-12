class AppRecord < ApplicationRecord
  include Filterable

  has_many :upload_records

  has_many :filtered_drawables
  has_many :permissions

  has_many(:similar_app_records, foreign_key: :app_record_id, dependent: :destroy)
  has_many(:reverse_similar_app_records, class_name: :AppRecord, foreign_key: :app_record_similar_id, dependent: :destroy)
  has_many :app_records, through: :similar_app_records, source: :app_record_similar

  scope :package_name, -> (package_name) {where package_name: package_name}
  scope :version_name, ->(version_name) {where version_name: version_name}
  scope :version_code, ->(version_code) {where version_code: version_code}
  scope :certificate_hash, ->(certificate_hash) {where certificate_hash: certificate_hash}
  scope :android_id, -> (android_id) {where id: UploadRecord.where(android_id: android_id)}

  scope :names, -> {group(:package_name).order('count_package_name desc').count(:package_name)}
  scope :names_and_versions, -> {group(:package_name, :version_code).order('count_package_name desc').count(:package_name)}

end
