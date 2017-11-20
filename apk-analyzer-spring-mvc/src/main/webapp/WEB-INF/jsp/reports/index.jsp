<%-- 
    Document   : index
    Created on : Dec 3, 2016, 12:08:22 AM
    Author     : Adam Laurencik
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<own:masterpage title="Reports">
    <jsp:attribute name="body">

        <div class="jumbotron">
            <h1><fmt:message key="reports.header"/></h1>
            <p class="lead"><fmt:message key="reports.subheader"/></p>

            <%--   <c:if test="${isAdmin}"> --%>
            <p align="right">
                <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/reports/create" role="button">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    <fmt:message key="reports.create"/>
                </a>
            </p>
            <%--  </c:if> --%>
        </div>
        <c:if test="${not empty reports}">
            <div class="row">
                <table class="table">
                    <thead>
                        <tr>
                            <th><fmt:message key="num"></fmt:message></th>
                            <th><fmt:message key="reports.sport"></fmt:message></th>
                            <th><fmt:message key="reports.start"></fmt:message></th>
                            <th><fmt:message key="reports.end"></fmt:message></th>
                            <th><fmt:message key="reports.burnedCalories"></fmt:message></th>
                            <th class="text-center"><fmt:message key="edit"/></th>
                            <th class="text-center"><fmt:message key="remove"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${reports}" var="report">
                            <c:set var="count" value="${count+1}" scope="page"/>
                            <tr>
                                <td class="col-xs-1 lead-column">${count}</td>
                                <td class="col-xs-2 lead-column"><c:out value="${report.sportActivity.name}"/></td>
                                <td class="col-xs-2 lead-column"><javatime:format value="${report.startTime}" pattern="HH:mm dd.MM.yyyy"/></td>
                                <td class="col-xs-2 lead-column"><javatime:format value="${report.endTime}" pattern="HH:mm dd.MM.yyyy"/></td>
                                <td class="col-xs-2 lead-column"><c:out value="${report.burnedCalories}"/></td>                              
                                <form:form method="get" action="${pageContext.request.contextPath}/reports/update/${report.id}" cssClass="form-horizontal">
                                    <td class="col-xs-1 text-center">
                                        <button class="btn btn-default" type="submit">
                                            <span class="sr-only"><fmt:message key="edit"/></span>
                                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                        </button>
                                    </td>
                                </form:form>
                                <form:form method="post" action="${pageContext.request.contextPath}/reports/remove/${report.id}" cssClass="form-horizontal">
                                    <td class="col-xs-1 text-center">
                                        <button class="btn btn-default" type="submit">
                                            <span class="sr-only"><fmt:message key="remove"/></span>
                                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                        </button>
                                    </td>
                                </form:form>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

    </jsp:attribute>
</own:masterpage>
