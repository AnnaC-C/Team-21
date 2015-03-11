module QuizHelper

  def retrieve_random_questions
    return Question.all.map{
      |x| { :question => x.question,
            :correct => x.correct,
            :incorrect_1 =>  x.incorrect_1,
            :incorrect_2 =>  x.incorrect_2,
            :incorrect_3 =>  x.incorrect_3
          }
      }.sample(5)
  end
end
