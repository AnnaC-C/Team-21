Rails.application.routes.draw do
  devise_for :users
  resources :users
  resources :transactions, path: 'pages'


  root 'pages#dashboard'
  get 'pages/dashboard'
  post 'transactions/transfer' => 'transactions#transfer', :as => :transfer

  devise_scope :user do
    get "/login" => "devise/sessions#new"
    get "/signup" => "devise/registrations#new"
  end

  namespace :api do
    devise_scope :user do
      post "transactions" => "transactions#transfer", :as => 'transfer'
      post 'registrations' => 'registrations#create', :as => 'register'
      post 'sessions' => 'sessions#create', :as => 'login'
      delete 'sessions' => 'sessions#destroy', :as => 'logout'
    end
  end
end
