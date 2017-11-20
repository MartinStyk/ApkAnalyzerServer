<%-- 
    Document   : setting
    Created on : 6.12.2016, 15:19:17
    Author     : petra
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="own" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<own:masterpage title="Settings">
        <jsp:attribute name="scripts">
        <script>
            $(function () {
                $(".datepicker").datetimepicker({ format: 'DD.MM.YYYY'});
            });
        </script>
    </jsp:attribute>
    <jsp:attribute name="body">

            <a href="${pageContext.request.contextPath}" class="btn btn-default" role="button">
                <span class="glyphicon glyphicon-menu-left" aria-hidden="true"></span>
                <fmt:message key="back"/>
            </a>

            <div class="page-header">
                <h1>
                   <fmt:message key="user.update"/>
                </h1>
            </div>
            <form:form method="POST"
                   action="${pageContext.request.contextPath}/settings"
                   acceptCharset=""
                   modelAttribute="user"
                   cssClass="form-horizontal">
          
                
            <div class="form-group ${firstName_error?'has-error':''}">
                <form:label path="firstName" cssClass="col-sm-2 control-label"><fmt:message key="user.firstName"/></form:label>
                <div class="col-sm-10">
                    <form:input path="firstName" cssClass="form-control"/>
                    <form:errors path="firstName" cssClass="help-block"/>
                </div>
            </div>
            
            <div class="form-group ${lastName_error?'has-error':''}">
                <form:label path="lastName" cssClass="col-sm-2 control-label"><fmt:message key="user.lastName"/></form:label>
                <div class="col-sm-10">
                    <form:input path="lastName" cssClass="form-control"/>
                    <form:errors path="lastName" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${email_error?'has-error':''}">
                <form:label path="email" cssClass="col-sm-2 control-label" style="display: none"><fmt:message key="user.email"/></form:label>
                <div class="col-sm-10">
                    <form:input path="email" cssClass="form-control" style="display: none"/>
                    <form:errors path="email" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${height_error?'has-error':''}">
                <form:label path="height" cssClass="col-sm-2 control-label"><fmt:message key="user.height"/></form:label>
                <div class="col-sm-10">
                    <form:input path="height" cssClass="form-control"/>
                    <form:errors path="height" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${weight_error?'has-error':''}">
                <form:label path="weight" cssClass="col-sm-2 control-label"><fmt:message key="user.weight"/></form:label>
                <div class="col-sm-10">
                    <form:input path="weight" cssClass="form-control"/>
                    <form:errors path="weight" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${dateOfBirth_error?'has-error':''}">
                <form:label path="dateOfBirth" cssClass="col-sm-2 control-label"><fmt:message key="user.dateOfBirth"/></form:label>
                <div class="col-sm-10">
                    <form:input path="dateOfBirth" cssClass="form-control datepicker"/>
                    <form:errors path="dateOfBirth" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${sex_error?'has-error':''}">
                <form:label path="sex" cssClass="col-sm-2 control-label"><fmt:message key="user.sex"/></form:label>
                <div class="col-sm-10">
                    <form:select path="sex" cssClass="form-control" >
                        <form:option value="MALE">Male</form:option>
                        <form:option value="FEMALE">Female</form:option>
                    </form:select>
                    <form:errors path="sex" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${passwordHash_error?'has-error':''}">
                <form:label path="passwordHash" cssClass="col-sm-2 control-label" style="display: none"><fmt:message key="user.passwordHash"/></form:label>
                <div class="col-sm-10">
                    <form:input path="passwordHash" cssClass="form-control" style="display: none"/>
                    <form:errors path="passwordHash" cssClass="help-block"/>
                </div>
            </div>

            <div class="form-group ${role_error?'has-error':''}">
                <form:label path="role" cssClass="col-sm-2 control-label" style="display: none"><fmt:message key="user.role"/></form:label>
                <div class="col-sm-10">
                    <form:input path="role" cssClass="form-control" style="display: none"/>
                    <form:errors path="role" cssClass="help-block"/>
                </div>
            </div>    
            <button class="btn btn-primary createBtn center-block allow-vertical-space" type="submit"><fmt:message key="submit"/></button>
        </form:form>
    </jsp:attribute>
</own:masterpage>
