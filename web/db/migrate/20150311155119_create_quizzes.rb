class CreateQuizzes < ActiveRecord::Migration
  def change
    create_table :quizzes do |t|
      t.string :question
      t.string :correct
      t.string :incorrect_1
      t.string :incorrect_2
      t.string :incorrect_3

      t.timestamps null: false
    end
  end
end
