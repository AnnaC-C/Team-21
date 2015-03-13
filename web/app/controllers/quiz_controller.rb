class QuizController < ApplicationController
  include QuizHelper

  def play
    # ensure login
  end

  def get_score
    #calculate_score(params[:answers])
    logger.info("LOOK HERE, THE PARAMS FOR THE QUIZ: " + params[:play].to_s)
    redirect_to :root
  end

end
