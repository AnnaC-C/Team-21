class Api::TransfersController < ApplicationController
  include TransactionsHelper

  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json' }

  respond_to :json

  def create
    to = params[:transaction][:to]
    from = params[:transaction][:from]
    amount = params[:amount]

    if(validate_account_ownership(to, from) && transaction_possible?(from, amount) && validate_input(to, from, amount) && transfer_money(to, from, amount))
      render :status => 200,
             :json => { :success => true,
                        :info => "Transfer complete."}
    else
      render :status => :unprocessable_entity,
             :json => { :success => false,
                        :info => "Error. Transfer did not complete." }
    end
  end
end
