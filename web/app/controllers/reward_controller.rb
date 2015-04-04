class RewardController < ApplicationController
  include RewardHelper

  def browse_store
  end

  def inventory
  end

  def buy
    result = process_sale(params[:selected_item])
    flash[:notice] = result[:message]
    redirect_to :browse
  end
end
