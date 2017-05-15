<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<head>
    <title><fmt:message key="page.404.title"/></title>
</head>
<c:import url="/fragments/header.jsp"/>
<body>
<div class="custom-opacity">
    <div class="container">
        <div class="row">
            <div class="col-sm-12">
                <h1 class="text-center"><fmt:message key="page.404.title"/></h1>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <h2 class="text-center"><a href="${pageContext.request.contextPath}/Auction?command=goTo&page=index">
                    <fmt:message key="main.page.ref"/>
                </a></h2>
            </div>
        </div>
    </div>
</div>
</body>
</html>
