class TransactionsController < ApplicationController
  include TransactionsHelper
  def new
    @transaction = Transaction.new
  end

  def transfer
    to = params[:transaction][:to]
    from = params[:transaction][:from]
    amount = params[:transaction][:amount]

    if(validate_account_ownership(to, from) && transaction_possible?(from, amount))
      transfer_money(to, from, amount)
    else
      logger.info("TRANSFER FAILURE.")
    end

    redirect_to :root
  end
end
