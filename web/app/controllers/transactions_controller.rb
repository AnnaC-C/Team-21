class TransactionsController < ApplicationController
  include TransactionsHelper

  def transfer
    to = params[:transaction][:to]
    from = params[:transaction][:from]
    amount = params[:amount]

    if(validate_account_ownership(to, from) && transaction_possible?(from, amount) && validate_input(to, from, amount))
      transfer_money(to, from, amount)
    else
      logger.info("TRANSFER FAILURE.")
    end

    redirect_to :root
  end
end
