class TransactionsController < ApplicationController

  def new
    @transaction = Transaction.new
  end

  def transfer
  end
end
