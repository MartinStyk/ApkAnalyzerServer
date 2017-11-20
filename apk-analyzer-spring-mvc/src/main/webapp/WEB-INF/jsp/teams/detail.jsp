<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage title="Team detail">
    <jsp:attribute name="body">
        <div class="row">
            
            <a href="${pageContext.request.contextPath}/teams" class="btn btn-default" role="button">
                <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>                
                <fmt:message key="back"/>
            </a>
            
            <div class="page-header">
                <h1>
                     <fmt:message key="team.detail"/><b>&nbsp;${team.name}</b>
                </h1>
            </div>
            <h2><fmt:message key="team.members"/></h2>
                <div class="col-sm-10">
                    <table class="table">
                        <thead>
                            <tr>
                                <th><fmt:message key="user.firstName"/></th>
                                <th><fmt:message key="user.lastName"/></th>
                                <th><fmt:message key="user.email"/></th>
                                <th><fmt:message key="user.sex"/></th>
                                <th><fmt:message key="team.role"/></th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${team.members}" var="u">
                                <tr>
                                    <td class="col-xs-3 lead-column" ><c:out value="${u.firstName}"/></td>
                                    <td class="col-xs-3 lead-column" ><c:out value="${u.lastName}"/></td>
                                    <td class="col-xs-3"><c:out value="${u.email}"/></td>
                                    <td class="col-xs-3"> <c:out value="${u.sex}"/></td>
                                    <c:choose>
                                          <c:when test="${team.teamLeader.id == u.id}">
                                             <td class="col-xs-3 lead-column" ><fmt:message key="team.teamLeader"/></td>
                                          </c:when>
                                          <c:otherwise>
                                              <td class="col-xs-3"></td>
                                         </c:otherwise>
                                    </c:choose>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
        </div>

         <c:if test="${isAdmin || (loggedUser.id == team.teamLeader.id)}">

                <p align="center">
                        <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/teams/update/${team.id}" role="button">
                            <fmt:message key="team.editInfo"/>
                        </a>
                        <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/teams/addUsers/${team.id}" role="button">
                            <fmt:message key="team.addUser"/>
                        </a>
                         <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/teams/removeUsers/${team.id}" role="button">
                                                    <fmt:message key="team.removeUser"/>
                         </a>
                 </p>
         </c:if>

    </jsp:attribute>
</own:masterpage>
