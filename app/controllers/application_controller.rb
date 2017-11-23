class ApplicationController < ActionController::API
  include Response
  include ExceptionHandler
  include ActionController::HttpAuthentication::Basic::ControllerMethods

  def authenticate
    authenticate_or_request_with_http_basic do |username, password|
      User.find_by(email: username).try(:authenticate, password)
    end
  end

end
