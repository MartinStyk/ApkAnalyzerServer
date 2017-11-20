<%@ tag pageEncoding="utf-8" dynamic-attributes="dynattrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="head" fragment="true" %>
<%@ attribute name="body" fragment="true" required="true" %>
<%@ attribute name="scripts" fragment="true" required="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="onw" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="${pageContext.request.locale}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!--    cross sit request forgery tags-->
        <meta name="_csrf" th:content="${_csrf.token}"/>
        <meta name="_csrf_header" th:content="${_csrf.headerName}"/>

        <title><fmt:message key="application_name"/></title>
        <!-- bootstrap css -->
        <!--<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon">-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" crossorigin="anonymous">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"  crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/a549aa8780dbda16f6cff545aeabc3d71073911e/build/css/bootstrap-datetimepicker.css" crossorigin="anonymous">
        <!--custom css stylesheet-->
        <link href="/pa165/resources/css/site.css" rel="stylesheet">
        <!-- jquery themes -->
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <!--Icon-->
        <link rel="shortcut icon" href="resources/img/Calories-Burned.png">
        <link rel="apple-touch-icon" href="resources/img/Calories-Burned.png">
        <jsp:invoke fragment="head"/>
    </head>
    <body class="body">
        <!-- navigation bar -->
        <nav class="navbar navbar-inverse navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="${pageContext.request.contextPath}"><fmt:message key="application_name"/></a>
                <div id="navbar" class="collapse navbar-collapse pull-left">
                    <ul class="nav navbar-nav">
                        <li><a href="${pageContext.request.contextPath}/reports"><fmt:message key="menu_reports"/></a></li>
                        <li><a href="${pageContext.request.contextPath}/sports"><fmt:message key="menu_sports"/></a></li>
                        <li><a href="${pageContext.request.contextPath}/teams"><fmt:message key="menu_teams"/></a></li>
                        <li><a href="${pageContext.request.contextPath}/users"><fmt:message key="menu_users"/></a></li>
                    </ul>
                </div><!--/.nav-collapse -->
                <ul class="nav navbar-nav navbar-right pull-right">        
                    <c:choose>
                        <c:when test="${not empty loggedUser}">
                            <li class="dropdown" id="menuLogin">
                              <a class="dropdown-toggle" href="#" data-toggle="dropdown" id="navLogin">
                                  <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
                                  <span class="userName">${loggedUser.firstName}</span>
                                  <span class="caret"></span>
                              </a>
                              <div class="dropdown-menu" style="padding:27px; background-color: white">
                                  <p><fmt:message key="menu_user"/>&nbsp;<span class="fullName">${loggedUser.firstName}&nbsp;${loggedUser.lastName}</span></p>
                                  <p><fmt:message key="menu_email"/>&nbsp;<span class="userEmail">${loggedUser.email}</span></p>
                                  <c:if test="${isAdmin}">
                                       <p><fmt:message key="admin_rights"/></p>
                                  </c:if>

                                  <a href="${pageContext.request.contextPath}/settings" class="btn btn-lg btn-primary btn-block"><fmt:message key="menu_settings"/></a>
                                  <a href="${pageContext.request.contextPath}/logout" class="btn btn-lg btn-primary btn-block"><fmt:message key="log_out"/></a>
                              </div>
                            </li>

                        </c:when>
                        <c:otherwise>
                          <li><a href="${pageContext.request.contextPath}/login"><fmt:message key="log_in"/></a></li>
                          <li><a href="${pageContext.request.contextPath}/register"><fmt:message key="user_register"/></a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
                </div>
            </div>
        </nav>

            <div class="container">
                <!-- alerts -->
                <c:if test="${not empty alert_danger}">
                    <div class="alert alert-danger" role="alert">
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        <c:out value="${alert_danger}"/></div>
                    </c:if>
                    <c:if test="${not empty alert_info}">
                    <div class="alert alert-info" role="alert"><c:out value="${alert_info}"/></div>
                </c:if>
                <c:if test="${not empty alert_success}">
                    <div class="alert alert-success" role="alert"><c:out value="${alert_success}"/></div>
                </c:if>
                <c:if test="${not empty alert_warning}">
                    <div class="alert alert-warning" role="alert"><c:out value="${alert_warning}"/></div>
                </c:if>
            </div>

            <!-- page body -->
            <div class="container">
                <jsp:invoke fragment="body"/>
            </div>
        </div>
            
        <!-- footer -->
        <footer class="footer">
            <div class="container" align="center">
                <p class="text-muted">&copy;&nbsp;<%=java.time.Year.now().toString()%>&nbsp; Jan Grundmann, Adam Laurenčík, Petra Ondřejková, Martin Styk</p>
            </div>
        </footer>
            
        <!-- JavaScript libraries -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment-with-locales.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.bundle.min.js"></script>
        <script src="https://cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/a549aa8780dbda16f6cff545aeabc3d71073911e/src/js/bootstrap-datetimepicker.js"></script>
        
        <!-- include JavaScript custom files -->
        <jsp:invoke fragment="scripts"/>
    </body>
</html>