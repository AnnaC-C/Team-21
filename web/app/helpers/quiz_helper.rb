module QuizHelper

  def retrieve_random_questions
    return Question.all.map{
      |x| { :question => x.question,
            :a => x.correct,
            :b =>  x.incorrect_1,
            :c =>  x.incorrect_2,
            :d =>  x.incorrect_3
          }
      }.sample(5)
  end
end
