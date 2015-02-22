class AddUseridToAccount < ActiveRecord::Migration
  def change
    add_column :accounts, :user_id, :string
  end
end
