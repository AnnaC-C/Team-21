class Api::AccountsController < ApplicationController
  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json' }

  respond_to :json

  # TODO: Should there be a failure case? Or just when :count = 0?

  def retrieve
    accounts = []
    current_user.accounts.each do |a|
        accounts.push({:id => a.id,
                       :type => a.description,
                       :balance => a.balance,
                       :interest => a.interest_rate})

    end

    render :status => 200,
           :json => { :success => true,
                      :count => current_user.accounts.length,
                      :data => {:accounts => accounts } }
  end
end
