<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage title="Update Team">
    <jsp:attribute name="body">

            <a href="${pageContext.request.contextPath}/teams/detail/${teamUpdate.id}" class="btn btn-default" role="button">
                <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>
                <fmt:message key="back"/>
            </a>

            <div class="page-header">
                <h1>
                   <fmt:message key="team.update"/>
                </h1>
            </div>

        <form:form method="POST"
                   action="${pageContext.request.contextPath}/teams/update/${teamUpdate.id}"
                   acceptCharset=""
                   modelAttribute="teamUpdate"
                   cssClass="form-horizontal">

            <div class="form-group ${name_error?'has-error':''}">
                <form:label path="name" cssClass="col-sm-2 control-label"><fmt:message key="team.name"/></form:label>
                <div class="col-sm-10">
                    <form:input path="name" cssClass="form-control"/>
                    <form:errors path="name" cssClass="help-block"/>
                </div>
            </div>
                
            <div class="form-group ${teamLeader_error?'has-error':''}">
                <form:label path="teamLeader" cssClass="col-sm-2 control-label"><fmt:message key="team.teamLeader"/></form:label>
                <div class="col-sm-10">
                    <form:select path="teamLeaderId" cssClass="form-control">
                        <c:forEach items="${teamUpdate.members}" var="m">
                            <c:choose>
                            <c:when test="${m.id == teamUpdate.teamLeaderId}">
                                <form:option value="${m.id}" selected="selected"> ${m.email} </form:option>
                            </c:when>
                            <c:otherwise>
                                <form:option value="${m.id}"> ${m.email} </form:option>
                            </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="teamLeader" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${members_error?'has-error':''}">
                <form:label path="members" cssClass="col-sm-2 control-label" style="display: none"><fmt:message key="team.members"/></form:label>
            </div>
            
            <div>
                <p>
                    <button class="btn btn-primary createBtn center-block allow-vertical-space" type="submit"><fmt:message key="submit"/></button>
                    </a>
                </p>
            </div>
        </form:form>
    </jsp:attribute>
</own:masterpage>
