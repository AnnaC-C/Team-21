class PagesController < AuthController

  def dashboard
    if !user_signed_in?
      redirect_to :login
    end
  end
end
