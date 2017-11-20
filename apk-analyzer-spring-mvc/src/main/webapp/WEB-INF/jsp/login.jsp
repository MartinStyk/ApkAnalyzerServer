<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage title="Login">
    <jsp:attribute name="body">

        <div css-class="loginForm">
            <div css-class="form-control">

                <form:form method="POST"
                           action="j_spring_security_check"
                           acceptCharset=""
                           modelAttribute="user"
                           cssClass="form-horizontal form-signin">

                    <h2 class="form-signin-heading"><fmt:message key="login.signin"/></h2>

                    <c:if test="${not empty param.error}">
                        <div class="alert alert-danger" role="alert">
                            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                            Invalid username or password.
                        </div>
                    </c:if>

                    <label for="inputEmail" class="sr-only">Email address</label>
                    <input type="email" name="user" id="inputEmail" class="form-control" placeholder="Email address" required="true" autofocus="true">
                    <label for="inputPassword" class="sr-only">Password</label>
                    <input type="password" name="pass" id="inputPassword" class="form-control" placeholder="Password" required="true">

                    <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="login.submit"/></button>
                </form:form>
            </div>
        </div>

    </jsp:attribute>
</own:masterpage>