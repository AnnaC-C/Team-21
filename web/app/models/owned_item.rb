class OwnedItem < ActiveRecord::Base
  belongs_to :User
  belongs_to :Item
end
