class CreateItems < ActiveRecord::Migration
  def change
    create_table :items do |t|
      t.string :description
      t.string :image
      t.integer :cost

      t.timestamps null: false
    end
  end
end
