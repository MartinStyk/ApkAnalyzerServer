class DropTableLayouts < ActiveRecord::Migration[5.1]
  def change
    drop_table :layouts do |t|
      t.string :file_hash
      t.references :app_record

      t.index :file_hash
    end
  end
end
