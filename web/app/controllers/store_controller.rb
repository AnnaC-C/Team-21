class StoreController < ApplicationController
  def browse
  end

  def buy
    logger.info("\n\n\n\n\nHELLO FROM BUY : ITEM: " + params[:selected_item] +  "\n\n\n\n\n")
    redirect_to :browse
  end
end
