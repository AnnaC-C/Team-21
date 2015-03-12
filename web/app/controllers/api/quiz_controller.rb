class Api::QuizController < ApplicationController
  include QuizHelper

  def get_questions
    render :status => 200,
           :json => { :questions => retrieve_random_questions }
  end

  def process_answers
    answers = params[:answers]
    points = 0
    all_correct = true

    answers.each do |a|
      if a.answer == Question.find(a.question).correct
        points += 10
      else
        all_correct = false
      end
    end

    if all_correct
      points += 20
    end

    # TODO: Write method for User to add points to both at once. (Transaction?)
    current_user.current_points += points
    current_user.current_points += points

    render :status => 200,
           :json => { :score => points }
  end
end
