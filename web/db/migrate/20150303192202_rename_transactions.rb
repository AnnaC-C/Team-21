class RenameTransactions < ActiveRecord::Migration
  def change
    rename_table :transactions, :transfers
  end
end
