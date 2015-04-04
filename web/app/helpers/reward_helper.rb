module RewardHelper
  def process_sale(item)
    # TODO Check that the User can afford the Item
    # TODO Add the Item to the User's Inventory
    # TODO Remove the points from the User
    item = Item.find(item) rescue nil
    if (item == nil)
      return {:success => false, :message => "The selected item does not exist."}
    end

    if(item.cost > current_user.current_points)
      return {:success => false, :message => "You don't have enough points to buy this item."}
    end

    if(current_user.owned_items.find_by_item_id(i) && !i.consumable)
      return {:success => false, :message => "This item can only be unlocked once."}
    end

    OwnedItem.create(user_id: current_user.id, item_id: item.id)
    current_user.current_points -= item.cost
    current_user.save
    return {:success => true, :message => "You bought a " + item.description + " for £" + item.cost.to_s + "."}
  end
end
