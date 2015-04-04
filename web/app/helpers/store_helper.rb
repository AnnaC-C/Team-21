module StoreHelper
  def process_sale(item)
    # TODO Check that the User can afford the Item
    # TODO Add the Item to the User's Inventory
    # TODO Remove the points from the User
    item = Item.find(item) rescue nil
    if (item == nil)
      return {:success => false, :message => "The selected item does not exist."}
    end

    if(item.cost > current_user.current_points)
      return {:success => false, :message => "You don't have enough points to buy this."}
    end
    
  end
end
