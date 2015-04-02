class Api::StoreController < ApplicationController
  include StoreHelper

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

  def buy

  end
end
