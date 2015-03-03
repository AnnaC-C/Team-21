class Api::SessionsController < Devise::SessionsController
  include ApiHelper

  skip_before_filter :verify_authenticity_token, :verify_signed_out_user, :if => Proc.new { |c|
    c.request.format == 'application/json' }

    respond_to :json

    # Create a new Session (Login)
    def create
      # Attempt to authenticate the user, call the failure method if unsuccessful.
      warden.authenticate!(:scope => resource_name, :recall =>
        "#{controller_path}#failure")

      # If it was successful, return appropriate JSON and the User's Accounts.
      render :status => 200,
             :json => { :success => true,
                        :info => "Logged in",
                        :data => { :auth_token => current_user.authentication_token,
                                   :accounts => retrieve_accounts
                                 }
                      }
    end

    # Destroy a Session (Logout).
    def destroy
      # Attempt to authenticate the user, call the failure method if unsuccessful.
      warden.authenticate!(:scope => resource_name, :recall =>
        "#{controller_path}#failure")

      # If authenticated, destroy the User's authentication_token.
      current_user.update_column(:authentication_token, nil)
      render :status => 200,
             :json => { :success => true,
                        :info => "Logged out",
                        :data => {} }
    end

    # Called on authentication failure.
    def failure
      render :status => 401,
             :json => { :success => false,
                        :info => "Login failed",
                        :data => {} }
    end
end
