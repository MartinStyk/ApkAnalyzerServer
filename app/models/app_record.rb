class AppRecord < ApplicationRecord
  has_many :drawables
  has_many :assets
  has_many :layouts
  has_many :other_files
  has_many :permissions
end
