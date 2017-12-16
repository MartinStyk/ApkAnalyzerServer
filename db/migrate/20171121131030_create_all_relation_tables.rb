class CreateAllRelationTables < ActiveRecord::Migration[5.1]
  def change
    create_table :drawables do |t|
      t.string :file_hash
      t.references :app_record

      t.index :file_hash
    end

    create_table :layouts do |t|
      t.string :file_hash
      t.references :app_record

      t.index :file_hash
    end
    create_table :permissions do |t|
      t.string :name
      t.references :app_record
    end

    create_table :upload_records do |t|
      t.string :android_id
      t.string :analysis_mode
      t.references :app_record
      t.string :source
      t.timestamps
    end

  end
end
