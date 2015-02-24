class AddAccountsToTransaction < ActiveRecord::Migration
  def change
    add_column :transactions, :from, :string
    add_column :transactions, :to, :string
    add_column :transactions, :amount, :float
  end
end
