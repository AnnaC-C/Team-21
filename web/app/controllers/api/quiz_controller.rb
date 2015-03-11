class Api::QuizController < ApplicationController
  include QuizHelper

  def get_questions
    render :status => 200,
           :json => { :questions => retrieve_random_questions }
  end
end
