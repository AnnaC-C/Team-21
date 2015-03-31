class AddConsumeableToItem < ActiveRecord::Migration
  def change
    add_column :items, :consumable, :boolean
  end
end
