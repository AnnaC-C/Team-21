class Transfer < ActiveRecord::Base
  has_many :Accounts
  belongs_to :Account
end
