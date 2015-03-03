class TransferController < ApplicationController
  include TransfersHelper

  def transfer
    to = params[:transfer][:to]
    from = params[:transfer][:from]
    amount = params[:amount]

    # TODO: Deliver error from this method to Dashboard.
    transfer_money(to, from, amount)


    redirect_to :root
  end
end
