class Api::RewardController < ApplicationController
  include RewardHelper

  skip_before_filter :verify_authenticity_token, :if => Proc.new { |c|
    c.request.format == 'application/json'
  }

  respond_to :json

  def get_items
    items = Item.all.map { |i| {
        :id => i.id,
        :description => i.description,
        :image => view_context.image_path(i.image),
        :cost => i.cost,
        :consumable => i.consumable
      }
    }

    render :status => 200,
           :json => { :items => items }

  end

  def get_inventory
    inventory = []

    current_user.owned_items.each do |i|
      item = Item.find(i)
      inventory << { :description => item.description,
                     :image => item.image,
                     :consumable => item.consumable }
    end

    render :status => 200,
           :json => { :inventory => inventory }
  end

  def buy

  end
end
