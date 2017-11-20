<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage>
    <jsp:attribute name="title"><fmt:message key="sport.header"/></jsp:attribute>

    <jsp:attribute name="body">

        <div class="jumbotron">
            <h1><fmt:message key="sport.header"/></h1>
            <p class="lead"><fmt:message key="sport.subheader"/></p>

            <form:form method="GET"
                   action="${pageContext.request.contextPath}/sports/index"
                   acceptCharset=""
                   cssClass="form-inline">

                <fmt:message key="sport_name_placeholder" var="sportNamePlaceholder"/>
                <input name="sportName" value="${param.name}" class="form-control" autocomplete="off" placeholder="${sportNamePlaceholder}"/>
                <button class="btn btn-primary search-btn" type="submit"><i class="glyphicon glyphicon-search"></i>&nbsp;<fmt:message key="search"/></button>
            </form:form>

            <c:if test="${isAdmin}">
                <p align="right">
                    <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/sports/create" role="button">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    <fmt:message key="sport.create"/>
                    </a>
                </p>
             </c:if>
        </div>

        <div class="row">
            <table class="table">
                <thead>
                    <tr>
                        <th><fmt:message key="num"/></th>
                        <th><fmt:message key="sport.name"/></th>
                        <th><fmt:message key="sport.caloriesFactor"/></th>
                          <c:if test="${isAdmin}">
                            <th class="text-center"><fmt:message key="edit"/></th>
                            <th class="text-center"><fmt:message key="remove"/></th>
                          </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${sports}" var="sport">
                        <c:set var="count" value="${count + 1}" scope="page"/>
                        <tr>
                            <td class="col-xs-1 lead-column">${count}.</td>
                            <td class="col-xs-3 lead-column"><c:out value="${sport.name}"/></td>
                            <td class="col-xs-1"><c:out value="${sport.caloriesFactor}"/></td>

                            <c:if test="${isAdmin}">
                                <form:form method="get" action="${pageContext.request.contextPath}/sports/update/${sport.id}" cssClass="form-horizontal">
                                    <td class="col-xs-1 text-center">
                                        <button class="btn btn-default" type="submit">
                                            <span class="sr-only"><fmt:message key="edit"/></span>
                                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                        </button>
                                    </td>
                                </form:form>

                                <form:form method="post" action="${pageContext.request.contextPath}/sports/remove/${sport.id}" cssClass="form-horizontal">
                                    <td class="col-xs-1 text-center">
                                        <button class="btn btn-default" type="submit">
                                            <span class="sr-only"><fmt:message key="remove"/></span>
                                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                                        </button>
                                    </td>
                                </form:form>
                            </c:if>
                        </tr>
                    </c:forEach>
            </table>
        </div>

    </jsp:attribute>
</own:masterpage>
