class TransactionsController < ApplicationController
  include TransactionsHelper
  def new
    @transaction = Transaction.new
  end

  def transfer
    to = params[:transaction][:to]
    from = params[:transaction][:from]
    amount = params[:amount]

    if(validate_account_ownership(to, from) && transaction_possible?(from, amount) && amount.to_i >= 0)
      transfer_money(to, from, amount)
    else
      logger.info("TRANSFER FAILURE.")
    end

    redirect_to :root
  end
end
