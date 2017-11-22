class CreateAllRelationTables < ActiveRecord::Migration[5.1]
  def change
    create_table :drawables do |t|
      t.string :file_hash
      t.references :app_record
    end

    create_table :layouts do |t|
      t.string :file_hash
      t.references :app_record
    end

    create_table :assets do |t|
      t.string :file_hash
      t.references :app_record
    end

    create_table :other_files do |t|
      t.string :file_hash
      t.references :app_record
    end

    create_table :permissions do |t|
      t.string :name
      t.references :app_record
    end
  end
end
