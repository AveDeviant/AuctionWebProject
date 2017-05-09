<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 27.02.2017
  Time: 19:38
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <fmt:setLocale value="${locale}" scope="session"/>
    <fmt:setBundle basename="properties.locale"/>
    <title><fmt:message key="login.page.title"/></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/darkstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<c:import url="${pageContext.request.contextPath}/fragments/header.jsp"/>
<c:if test="${user!=null}">
    <c:redirect url="/jsp/private.jsp"/>
</c:if>
<div class="custom-opacity">
<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="text-center strong text-info"><fmt:message key="login.page.title"/></h1>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-offset-4 col-sm-4">
            <div class="window">
                <form action="${pageContext.request.contextPath}/Auction" method="post">
                    <div class="form-group">
                        <label for="login"><fmt:message key="login.title.login"/></label><label
                            class="error">*</label>
                        <input type="text" name="login" id="login" pattern="[\w-_]{6,32}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="password"><fmt:message key="login.title.password"/></label><label
                            class="error">*</label>
                        <input type="password" name="password" id="password" class="form-control" required
                               title="<fmt:message key="login.password.restrict"/>"
                               pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*[\d])[\w_-]{8,}$">
                    </div>
                    <br>
                    <button class="button-auction" type="submit" name="button"><fmt:message key="login.button.login"/></button><br/>
                        <c:if test="${authorizationError !=null}">
                            <div class=" alert alert-danger alert-dismissable fade in">
                            <fmt:message key="${authorizationError}"/>
                            </div>
                        </c:if>
                    <input type="hidden" name="command" value="authorization">
                    <input type="hidden" name="jspPath" value="${pageContext.request.servletPath}"/><br>
                </form>
                <br>
                <label class="error">*</label><label><fmt:message key="login.notice.necessary"/></label><br/>
                <fmt:message key="login.firsttime.title"/>
                <a href="${pageContext.request.contextPath}/Controller?command=goTo&page=registration"><fmt:message
                        key="login.button.register"/></a>
            </div>
        </div>
    </div>
    </div>
</div>
</body>
</html>
