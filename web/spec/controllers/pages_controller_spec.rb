require 'rails_helper'

RSpec.describe PagesController, type: :controller do

  describe "GET the dashboard" do

    it "should GET if logged in" do
      pages_user = FactoryGirl.create(:user)
      sign_in(pages_user)
      assert_response :success
    end

    it "should redirect if not logged in" do
      logged_out_user = FactoryGirl.create(:user)
      sign_out(logged_out_user)
      get :dashboard
      expect(response).to be_redirect
    end
  end
end
