require 'rails_helper'

RSpec.describe StoreController, type: :controller do

  describe "GET #browse" do
    it "returns http success" do
      get :browse
      expect(response).to have_http_status(:success)
    end
  end

end
