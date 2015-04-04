class CreateOwnedItems < ActiveRecord::Migration
  def change
    create_table :owned_items do |t|
      t.string :user_id
      t.string :item_id

      t.timestamps null: false
    end
  end
end
