class StoreController < ApplicationController
  include StoreHelper

  def browse
  end

  def buy
    process_sale(params[:selected_item])
    redirect_to :browse
  end
end
