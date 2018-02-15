class LibraryDrawableHash < ApplicationRecord
  validates_uniqueness_of :file_hash
end
