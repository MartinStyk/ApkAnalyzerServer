class AppRecord < ApplicationRecord
  has_many :upload_records

  has_many :drawables
  has_many :assets
  has_many :layouts
  has_many :other_files
  has_many :permissions
  has_many :features
end
