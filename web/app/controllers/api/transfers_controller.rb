class Api::TransfersController < ApplicationController
  include TransfersHelper
  include ApiHelper

  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json' }

  respond_to :json

  # Create a new Transfer through the API.
  def create
    # Extract the parameters.
    to = params[:transfer][:to]
    from = params[:transfer][:from]
    amount = params[:amount]

    result = transfer_money(to, from, amount)

    if(result[:success])
      # If the transfer was successful, return the status and the new accounts.
      render :status => 200,
             :json => { :result => result,
                        :accounts => retrieve_accounts
                      }
    else
      # If it wasn't, return the error.
      render :status => :unprocessable_entity,
             :json => { :result => result }
    end
  end

  def retrieve
    render :status => 200,
           :json => { :transfers => retrieve_transfers }
  end
end
