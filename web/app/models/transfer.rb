class Transfer < ActiveRecord::Base
  has_many :Accounts
  belongs_to :Account

  validates_numericality_of :amount, :receiver_id, :sender_id
end
