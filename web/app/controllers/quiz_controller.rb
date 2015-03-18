class QuizController < ApplicationController
  include QuizHelper

  def play
    # ensure login
  end

  # TODO: Handle RecordNotFound
  def get_score
    answers_from_params = { :answers => []}

    params[:questions].each do |q,a|
       answers_from_params[:answers].push({:question => Question.find_by(:id => q)[:question], :answer => a} )
    end
       logger.info("\NSCORE: " + calculate_score(answers_from_params[:answers]).to_s + "\n")
    redirect_to :root
  end

end
