class TransferController < ApplicationController
  include TransfersHelper

  def transfer
    to = params[:transfer][:to]
    from = params[:transfer][:from]
    amount = params[:amount]

    result = transfer_money(to, from, amount)

    flash[:notice] = result[:message]


    redirect_to :root
  end
end
