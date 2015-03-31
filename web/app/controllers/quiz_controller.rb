require 'json'

class QuizController < ApplicationController
  include QuizHelper

  def play
    # ensure login
  end

  # TODO: Handle RecordNotFound
  def get_score
    answers_from_params = { :answers => []}

    params[:questions].each do |i,a,q|
       answers_from_params[:answers].push({ :id=> i, :answer => a } )
    end

    logger.info(JSON.pretty_generate(answers_from_params))
    logger.info(JSON.pretty_generate(params[:questions]))
    flash[:notice] = "You scored " + calculate_score(answers_from_params[:answers]).to_s + " points!"
    redirect_to :root
  end

end
