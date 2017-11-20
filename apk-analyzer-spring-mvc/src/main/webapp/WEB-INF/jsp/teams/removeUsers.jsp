<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage title="Remove Members">
    <jsp:attribute name="body">

        <a href="${pageContext.request.contextPath}/teams/detail/${currentTeam.id}" class="btn btn-default" role="button">
            <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>
            <fmt:message key="back"/>
        </a>

        <div class="page-header">
            <h1>
                <fmt:message key="team.removeUser"/>
            </h1>
        </div>

        <form:form method="POST"
                   action="${pageContext.request.contextPath}/teams/removeUsers/${currentTeam.id}"
                   acceptCharset=""
                   modelAttribute="users"
                   cssClass="form-horizontal">

            <div class="form-group ${addUsers_error?'has-error':''}">
                <div class="col-sm-12">
                                       
                    <form:select multiple="true" class="form-control" path="members" items="${users.members}" itemLabel="email" itemValue="id"/>
                    <form:errors path="members" cssClass="help-block"/>
                </div>
            </div>
            <button class="btn btn-primary createBtn center-block allow-vertical-space" type="submit"><fmt:message key="submit"/></button>
        </form:form>
    </jsp:attribute>
</own:masterpage>
