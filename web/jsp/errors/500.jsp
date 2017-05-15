<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<c:import url="/fragments/header.jsp"/>
<head>
    <title><fmt:message key="page.500.title"/></title>
</head>
<body>
<div class="custom-opacity">
    <div class="row">
        <div class="col-sm-12">
            <h1 class="text-center"><fmt:message key="page.500.title"/></h1>
            <h3 class="text-center"><fmt:message key="page.500.messaqe"/></h3>
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
</body>
</html>
