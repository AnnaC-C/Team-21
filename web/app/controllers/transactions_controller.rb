class TransactionsController < ApplicationController
  include TransactionsHelper

  def transfer
    to = params[:transaction][:to]
    from = params[:transaction][:from]
    amount = params[:amount]

    transfer_money(to, from, amount)


    redirect_to :root
  end
end
