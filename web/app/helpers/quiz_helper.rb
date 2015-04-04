module QuizHelper

# How many questions should be returned.
@@question_count = 5

  def retrieve_random_questions

    # Get question_count amount of random questions and map them to a hash.
    questions = Question.all.map.with_index {
      |x| {   :id => x.id,
              :question => x.question,
              :answers => [x.correct, x.incorrect_1, x.incorrect_2, x.incorrect_3].shuffle
          }
      }.sample(@@question_count)

    # Return questions.
    return questions
  end

  def calculate_score(answers)
    points = 0
    correct = 0

      answers.each do |a|
        if a[:answer] == Question.find(a[:id]).correct
          points += 100
          correct += 1
        end
      end

    if correct == @@question_count
      points += 200
    end

    # TODO: Write method for User to add points to both at once. (Transaction?)
    current_user.current_points += points.to_i
    current_user.lifetime_points += points.to_i
    current_user.save

    return points
  end
end
