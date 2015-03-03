class Api::TransfersController < ApplicationController
  include TransfersHelper
  include ApiHelper

  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json' }

  respond_to :json

  def create
    to = params[:transfer][:to]
    from = params[:transfer][:from]
    amount = params[:amount]

    if(transfer_money(to, from, amount)
      render :status => 200,
             :json => { :success => true,
                        :info => "Transfer complete.",
                        :accounts => retrieve_accounts
                      }
    else
      render :status => :unprocessable_entity,
             :json => { :success => false,
                        :info => "Error. Transfer did not complete." }
    end
  end
end
