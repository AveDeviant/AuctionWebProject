<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 27.04.2017
  Time: 21:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<c:import url="${pageContext.request.contextPath}/fragments/header.jsp"/>
<head>
    <title><fmt:message key="operation.success"/></title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="text-center"><fmt:message key="operation.success"/></h1>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div align="center">
                <form method="get" action="${pageContext.request.contextPath}/Auction">
                    <button class="button-auction" type="submit"><fmt:message key="operation.success.back"/> </button>
                    <input type="hidden" name="command" value="back">
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
