Rails.application.routes.draw do

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  resources :app_records, only: [:index, :show, :create] do
    get :names, on: :collection
    get :names_versions, on: :collection
    resources :upload_records, only: [:index, :show]
    resources :permissions, only: [:index, :show]
    resources :filtered_drawables, only: [:index, :show]
  end

  resources :repackaged_detection, only: [:show, :index]
  resources :repackaged_results, only: [:index, :show] do
    get :statistics, on: :collection
  end

  resources :usage, only: [:index]
  resources :available, only: [:index]

end
