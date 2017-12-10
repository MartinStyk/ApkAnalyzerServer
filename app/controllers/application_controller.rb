class ApplicationController < ActionController::API

  include Response
  include ExceptionHandler
  include ActionController::HttpAuthentication::Basic::ControllerMethods

  def authenticate_admin
    authenticate_or_request_with_http_basic do |username, password|
      User.find_by(email: username).try(:authenticate, password) if username != 'device'
    end
  end

  def authenticate_device
    authenticate_or_request_with_http_basic do |username, password|
      User.find_by(email: 'device').try(:authenticate, password)
    end
  end

end
