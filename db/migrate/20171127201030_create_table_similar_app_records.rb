class CreateTableSimilarAppRecords < ActiveRecord::Migration[5.1]
  def change
    create_table :similar_app_records do |t|
      t.integer "app_record_id", :null => false
      t.integer "app_record_similar_id", :null => false
      t.float :score

      t.index "app_record_id"
      t.index "app_record_similar_id"
    end

  end
end
