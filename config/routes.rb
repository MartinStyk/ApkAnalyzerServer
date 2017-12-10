Rails.application.routes.draw do

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
  resources :app_records, only: [:index, :show, :create] do
    resources :permissions, only: [:index, :show]
    resources :menus, only: [:index, :show]
    resources :drawables, only: [:index, :show]
    resources :features, only: [:index, :show]
    resources :layouts, only: [:index, :show]
  end

  resources :repackaged_detection, only: [:show]
  resources :repackaged_results, only: [:index, :show]

  resources :usage, only: [:index]
end
