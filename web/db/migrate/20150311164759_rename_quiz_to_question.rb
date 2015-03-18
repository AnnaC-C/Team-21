class RenameQuizToQuestion < ActiveRecord::Migration
  def change
    rename_table :quizzes, :questions
  end
end
