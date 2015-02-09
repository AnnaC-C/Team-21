class PagesController < ApplicationController
  def dashboard
    if not logged_in?
      redirect_to login_path
    end
  end
end
