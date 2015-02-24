class TransactionsController < ApplicationController
  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json' }

  respond_to :json
    
  def transfer
    render :status => 200,
           :json => { :success => true,
                      :info => "Hello",
                      :data => {} }
  end
end
