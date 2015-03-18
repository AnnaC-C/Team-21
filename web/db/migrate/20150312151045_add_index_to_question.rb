class AddIndexToQuestion < ActiveRecord::Migration
  def change
    add_index :questions, :question
  end
end
