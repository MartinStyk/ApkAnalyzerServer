class DropTableDrawables < ActiveRecord::Migration[5.1]
  def up
    drop_table :drawables
  end

  def down
    raise ActiveRecord::IrreversibleMigration
  end
end
