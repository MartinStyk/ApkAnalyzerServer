class SimilarAppRecord < ApplicationRecord
  belongs_to :app_record, class_name: :AppRecord
  belongs_to :app_record_similar, class_name: :AppRecord
end
