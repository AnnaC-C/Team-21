class Transaction < ActiveRecord::Base
  has_many :Accounts
  belongs_to :Account
end
