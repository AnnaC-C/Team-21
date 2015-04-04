Rails.application.routes.draw do
  get 'store/browse'

  devise_for :users
  resources :users

  devise_scope :user do
    get "/login" => "devise/sessions#new"
    get "/signup" => "devise/registrations#new"
    root 'pages#dashboard'
    get 'pages/dashboard'
    post 'transfer/transfer' => 'transfer#transfer', :as => :transfers
    get 'quiz' => 'quiz#play'
    post 'quiz' => 'quiz#get_score', :as => :play
    get 'store' => 'reward#browse'
    get 'inventory' => 'reward#inventory'
    post 'store' => 'reward#buy', :as => :browse
  end

  namespace :api do
    devise_scope :user do
      post 'registrations' => 'registrations#create', :as => 'register'
      post 'sessions' => 'sessions#create', :as => 'login'
      delete 'sessions' => 'sessions#destroy', :as => 'logout'
      post "transfers" => "transfers#create", :as => 'transfer'
      get "transfers" => "transfers#retrieve", :as => 'retrieve'
      get "questions" => "quiz#get_questions", :as => 'questions'
      post "answers" => "quiz#process_answers", :as => 'answers'
      get "items" => "reward#get_items", :as => 'items'
      post "buy" => "reward#buy_item", :as => 'buy'
      get "inventory" => "reward#get_inventory", :as => 'inventory'
    end
  end
end
