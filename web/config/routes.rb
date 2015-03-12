Rails.application.routes.draw do
  devise_for :users
  resources :users

  root 'pages#dashboard'
  get 'pages/dashboard'
  post 'transfer/transfer' => 'transfer#transfer', :as => :transfers

  devise_scope :user do
    get "/login" => "devise/sessions#new"
    get "/signup" => "devise/registrations#new"
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
    end
  end
end
