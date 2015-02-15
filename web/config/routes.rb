Rails.application.routes.draw do
  devise_for :users

  root 'pages#dashboard'
  get 'pages/dashboard'

  resources :users

  namespace :api do
    devise_scope :user do
      post 'registrations' => 'registrations#create', :as => 'register'
      post 'sessions' => 'sessions#create', :as => 'login'
      delete 'sessions' => 'sessions#destroy', :as => 'logout'
    end
  end
end
