<%-- 
    Document   : users
    Created on : 9.12.2016, 22:12:12
    Author     : Petra Ondřejková
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage>
    <jsp:attribute name="title"><fmt:message key="user.users"/></jsp:attribute>

    <jsp:attribute name="body">

        <div class="jumbotron">
            <h1><fmt:message key="user.users"/></h1>
            <p class="lead"><fmt:message key="user.usersSub"/></p>

        </div>

        <div class="row">
            <table class="table">
                <thead>
                    <tr>
                        <th><fmt:message key="num"/></th>
                        <th><fmt:message key="user.firstName"/></th>
                        <th><fmt:message key="user.lastName"/></th>
                        <th><fmt:message key="user.role"/></th>
                          <c:if test="${isAdmin}">
                            <th class="text-center"><fmt:message key="user.makeAdmin"/></th>
                            <th class="text-center"><fmt:message key="remove"/></th>
                          </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="user">
                        <c:set var="count" value="${count + 1}" scope="page"/>
                        <tr>
                            <td class="col-xs-1 lead-column">${count}.</td>
                            <td class="col-xs-3 lead-column"><c:out value="${user.firstName}"/></td>
                            <td class="col-xs-3 lead-column"><c:out value="${user.lastName}"/></td>
                            <td class="col-xs-3 lead-column"><c:out value="${user.role}"/></td>

                            <c:if test="${isAdmin}">
                                <c:choose>
                                <c:when test="${user.role !='ADMIN'}">
                                    <form:form method="post" action="${pageContext.request.contextPath}/users/update/${user.id}" cssClass="form-horizontal">
                                        <td class="col-xs-1 text-center">
                                             <a href="${pageContext.request.contextPath}/users/makeAdmin/${user.id}" class="btn btn-default">Admin</a>
                                        </td>
                                    </form:form>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${loggedUser.id!=user.id}">
                                        <form:form method="post" action="${pageContext.request.contextPath}/users/update/${user.id}" cssClass="form-horizontal">
                                                <td class="col-xs-1 text-center">
                                                    <a href="${pageContext.request.contextPath}/users/makeRegular/${user.id}" class="btn btn-default">Regular</a>
                                                </td>
                                         </form:form>
                                    </c:if>
                                </c:otherwise>
                                </c:choose>
                                <c:if test="${loggedUser.id!=user.id}">
                                    <form:form method="post" action="${pageContext.request.contextPath}/users/remove/${user.id}" cssClass="form-horizontal">
                                        <td class="col-xs-1 text-center">
                                            <button class="btn btn-default" type="submit">
                                                <span class="sr-only"><fmt:message key="remove"/></span>
                                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                            </button>
                                        </td>
                                    </form:form>
                                </c:if>
                            </c:if>
                        </tr>
                    </c:forEach>
            </table>
        </div>

    </jsp:attribute>
</own:masterpage>