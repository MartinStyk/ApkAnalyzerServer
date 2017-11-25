class AppRecord < ApplicationRecord
  has_many :upload_records

  has_many :drawables
  has_many :menus
  has_many :layouts
  has_many :permissions
  has_many :features
end
