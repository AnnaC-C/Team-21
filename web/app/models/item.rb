class Item < ActiveRecord::Base
  has_many :owned_items
end
