class CreateAccounts < ActiveRecord::Migration
  def change
    create_table :accounts do |t|
      t.string :description
      t.integer :balance
      t.float :interest_rate

      t.timestamps null: false
    end
  end
end
