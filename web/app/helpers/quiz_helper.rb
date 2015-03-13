module QuizHelper

  def retrieve_random_questions
    return Question.all.map{
      |x| { :question => x.question,
            :answers => [x.correct, x.incorrect_1, x.incorrect_2, x.incorrect_3].shuffle
          }
      }.sample(5)
  end

  def calculate_score(answers)
    points = 0
    all_correct = true

    answers.each do |a|
      if a[:answer] == Question.find(a[:question]).correct
        points += 10
      else
        all_correct = false
      end
    end

    if all_correct
      points += 20
    end

    # TODO: Write method for User to add points to both at once. (Transaction?)
    current_user.current_points += points.to_i
    current_user.lifetime_points += points.to_i
    current_user.save

    return points
  end
end
