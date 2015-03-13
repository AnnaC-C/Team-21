class QuizController < ApplicationController
  include QuizHelper

  def play
    # ensure login
  end

  def get_score
    #calculate_score(params[:answers])
    5.times do |i|
      logger.info "param:" + params[:"#{i+1}"].to_s
    end
    redirect_to :root
  end

end
