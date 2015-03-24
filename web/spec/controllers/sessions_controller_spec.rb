require 'rails_helper'

RSpec.describe SessionsController, type: :controller do  
  user = FactoryGirl.create(:user)
  login_as(user, :scope => :user)
end
