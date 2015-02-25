class FixTransactionColumns < ActiveRecord::Migration
  def change
    rename_column :transactions, :from, :sender_id
    rename_column :transactions, :to, :receiver_id
    add_column :transactions, :user_id, :string
  end
end
