FactoryGirl.define do
  factory :user do
    email 'user_one@test.com'
    password 'password'
    password_confirmation 'password'
  end
end
