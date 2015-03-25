FactoryGirl.define do
  sequence :email do |n|
    "user#{n}@test.com"
  end

  factory :user do
    email
    password "password"
    password_confirmation "password"
  end

  sequence :user_id do |a|
    a
  end

  factory :account do
    description "test"
    balance 100.0
    interest_rate 7.5
    user_id
  end
end
