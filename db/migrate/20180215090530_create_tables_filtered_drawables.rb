class CreateTablesFilteredDrawables < ActiveRecord::Migration[5.1]
  def change
    create_table :filtered_drawables do |t|
      t.string :file_hash
      t.string :file_name
      t.references :app_record
      t.index :file_hash
    end

    create_table :library_drawable_hashes do |t|
      t.string :file_hash
      t.index :file_hash, unique: true
    end
  end
end
