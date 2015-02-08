class AddPasswordDigestToUsers < ActiveRecord::Migration
  def change
    add_column :users, :digest, :string
  end
end
