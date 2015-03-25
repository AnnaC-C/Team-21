require 'rails_helper'

RSpec.describe User, type: :model do
  it 'should start with 1000 points' do
    new_user = FactoryGirl.create(:user)
    expect(new_user.current_points).to eq(1000)
  end
end
