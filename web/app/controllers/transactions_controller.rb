class TransactionsController < ApplicationController
  include TransactionsHelper
  def new
    @transaction = Transaction.new
  end

  def transfer
    validate_account_ownership(params[:transaction][:to], params[:transaction][:from])
  end
end
