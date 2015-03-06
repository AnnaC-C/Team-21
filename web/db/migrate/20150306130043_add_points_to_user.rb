class AddPointsToUser < ActiveRecord::Migration
  def change
    add_column :users, :lifetime_points,  :integer, :default => 1000
    add_column :users, :current_points,  :integer, :default => 1000
  end
end
