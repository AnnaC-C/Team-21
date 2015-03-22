class Api::QuizController < ApplicationController
  include QuizHelper

  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json'
  }

  respond_to :json

  def get_questions
    render :status => 200,
           :json => { :questions => retrieve_random_questions }
  end

  def process_answers
    answers = params[:answers]
    points = calculate_score(answers)
    
    # Other renders for erroneous submissions.
    render :status => 200,
           :json => { :score => points }
  end
end
