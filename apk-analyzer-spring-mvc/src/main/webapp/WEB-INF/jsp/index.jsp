<%-- 
    Document   : index
    Created on : Dec 3, 2016, 12:08:22 AM
    Author     : Adam
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage>
    <jsp:attribute name="scripts">
        <script>
            function hashString(str){
            var hash = 0;
            if (str.length == 0) return hash;
            for (i = 0; i < str.length; i++) {
            char = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + char;
            hash = hash & hash; // Convert to 32bit integer
            }
            return hash;
            }
            function stringToColorCode(str) {
            return '#' + ('000000' + (hashString(str) * 0xFFFFFF << 0).toString(16)).slice( - 6);
            }
            var sportsCtx = document.getElementById("sportsChart");
            var sportsData = {
            datasets: [{
            data: [
            <c:forEach items="${statistics.sportActivities}" var="sportActivityEntry" varStatus="loop">
                ${sportActivityEntry.value}${!loop.last ? ',' : ''}
            </c:forEach>
            ],
                    backgroundColor: [
            <c:forEach items="${statistics.sportActivities}" var="sportActivityEntry" varStatus="loop">
                    stringToColorCode("${sportActivityEntry.key.name}")${!loop.last ? ',' : ''}
            </c:forEach>
                    ],
                    label: 'Sports' // for legend
            }],
                    labels: [
            <c:forEach items="${statistics.sportActivities}" var="sportActivityEntry" varStatus="loop">
                    "${sportActivityEntry.key.name}"${!loop.last ? ',' : ''}
            </c:forEach>
                    ]
            };
            var sportChart = new Chart(sportsCtx, {
            type: 'pie',
                    data: sportsData
            });
            var caloriesCtx = document.getElementById("caloriesChart");
            var caloriesData = {
            datasets: [{
            data: [
            <c:forEach items="${statistics.caloriesForActivities}" var="sportActivityEntry" varStatus="loop">
                ${sportActivityEntry.value}${!loop.last ? ',' : ''}
            </c:forEach>
            ],
                    backgroundColor: [
            <c:forEach items="${statistics.caloriesForActivities}" var="sportActivityEntry" varStatus="loop">
                    stringToColorCode("${sportActivityEntry.key.name}")${!loop.last ? ',' : ''}
            </c:forEach>
                    ],
                    label: 'Sports' // for legend
            }],
                    labels: [
            <c:forEach items="${statistics.caloriesForActivities}" var="sportActivityEntry" varStatus="loop">
                    "${sportActivityEntry.key.name}"${!loop.last ? ',' : ''}
            </c:forEach>
                    ]
            };
            var caloriesChart = new Chart(caloriesCtx, {
            type: 'pie',
                    data: caloriesData
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="body">

        <div class="jumbotron homepage">
            <c:choose>
                <c:when test="${empty loggedUser}">
                    <h1><fmt:message key="application_name"/></h1>
                    <p class="lead"><fmt:message key="index_welcome"/></p>
                    <p><fmt:message key="index_text"/></p>
                    <p align="right">
                        <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/login" role="button">
                            <fmt:message key="sign_in"/>
                        </a>
                        <a class="btn btn-lg btn-success btn-jumbotron" href="${pageContext.request.contextPath}/register" role="button">
                            <fmt:message key="user_register"/>
                        </a>
                    </p>

                </c:when>
                <c:otherwise>
                    <h1>
                        <fmt:message key="index_dashoard">
                            <fmt:param value="${loggedUser.firstName}"/>
                        </fmt:message>
                    </h1>
                    <p><fmt:message key="index_subtext_dashboard"/></p>

                    <div class="row homepagePanels">

                    <div class=" col-lg-4 col-md-4 col-sm-4 allBurnCalories">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">
                                    <img class="icon calorie" src="resources/img/Calories-Burned-White.png"
                                         style="max-height:25px; max-width:25px"/>
                                    <span><fmt:message key="user.totalCalories"/></span>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <span><c:out value="${statistics.totalCalories}"/></span>
                            </div>
                        </div>
                    </div>

                    <div class=" col-lg-4 col-md-4 col-sm-4 BurnCalorieLastWeek">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">
                                    <img class="icon calorie" src="resources/img/Calories-Burned-White.png"
                                         style="max-height:25px; max-width:25px"/>
                                    <span><fmt:message key="user.caloriesLastWeek"/></span>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <span><c:out value="${statistics.caloriesLastWeek}"/></span>
                            </div>
                        </div>
                    </div>

                    <div class=" col-lg-4 col-md-4 col-sm-4 BurnCalorieLastMounth">
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">
                                    <img class="icon calorie" src="resources/img/Calories-Burned-White.png"
                                         style="max-height:25px; max-width:25px"/>
                                    <span><fmt:message key="user.caloriesLastMonth"/></span>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <span><c:out value="${statistics.caloriesLastMonth}"/></span>
                            </div>
                        </div>
                    </div>

                </div>
                    <div class="row homepagePanels">

                        <div class=" col-lg-4 col-md-4 col-sm-4 allBurnCalories">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <img class="icon calorie" src="resources/img/Sport-Activities-White.png"
                                             style="max-height:25px; max-width:25px"/>
                                        <span><fmt:message key="user.totalActivities"/></span>
                                    </h3>
                                </div>
                                <div class="panel-body">
                                    <span><c:out value="${statistics.totalSportActivities}"/></span>
                                </div>
                            </div>
                        </div>

                        <div class=" col-lg-4 col-md-4 col-sm-4 BurnCalorieLastWeek">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <img class="icon calorie" src="resources/img/Sport-Activities-White.png"
                                             style="max-height:25px; max-width:25px"/>
                                        <span><fmt:message key="user.activitiesLastWeek"/></span>
                                    </h3>
                                </div>
                                <div class="panel-body">
                                    <span><c:out value="${statistics.sportActivitiesLastWeek}"/></span>
                                </div>
                            </div>
                        </div>

                        <div class=" col-lg-4 col-md-4 col-sm-4 BurnCalorieLastMounth">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <img class="icon calorie" src="resources/img/Sport-Activities-White.png"
                                             style="max-height:25px; max-width:25px"/>
                                        <span><fmt:message key="user.activitiesLastMonth"/></span>
                                    </h3>
                                </div>
                                <div class="panel-body">
                                    <span><c:out value="${statistics.sportActivitiesLastMonth}"/></span>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class ="row homepagePanels">
                        <div class="col-md-6">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <img class="icon calorie" src="resources/img/Sport-Activities-White.png"
                                             style="max-height:25px; max-width:25px"/>
                                        <span><fmt:message key="user.sportsData"/></span>
                                    </h3>
                                </div>
                                <div class="panel-body">
                                    <c:choose>
                                        <c:when test="${not empty statistics.sportActivities}">                                           
                                    <canvas id="sportsChart" width="400" height="400"></canvas>
                                        </c:when>
                                        <c:otherwise>
                                        <h2> <fmt:message key="user.noData"></fmt:message></h2>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <img class="icon calorie" src="resources/img/Calories-Burned-White.png"
                                             style="max-height:25px; max-width:25px"/>
                                        <span><fmt:message key="user.caloriesData"/></span>
                                    </h3>
                                </div>
                                <div class="panel-body">
                                    <c:choose>
                                        <c:when test="${not empty statistics.sportActivities}">                                           
                                    <canvas id="caloriesChart" width="400" height="400"></canvas>
                                        </c:when>
                                        <c:otherwise>
                                            <h2> <fmt:message key="user.noData"></fmt:message></h2>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>


                </c:otherwise>
            </c:choose>

        </div>

    </jsp:attribute>
</own:masterpage>
