module QuizHelper

  def retrieve_random_questions
    return Question.all.map{
      |x| { :question => x.question,
            :answers => [x.correct, x.incorrect_1, x.incorrect_2, x.incorrect_3]
          }
      }.sample(5)
  end
end
