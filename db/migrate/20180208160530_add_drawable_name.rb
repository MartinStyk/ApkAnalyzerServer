class AddDrawableName < ActiveRecord::Migration[5.1]
  def change
    add_column :drawables, :file_name, :string
  end
end
