<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 22.02.2017
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="header.siteTitle"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/darkstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link href="https://fonts.googleapis.com/css?family=PT+Serif" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Jura" rel="stylesheet">
</head>
<body>
<c:import url="/fragments/header.jsp"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<div class="container">
    <c:if test="${err!=null}">
        <fmt:message key="${err}"/>
    </c:if>
    <c:if test="${banned !=null}">
        <label class="alert-danger"><fmt:message key="${banned}"/> </label>
    </c:if>
    <c:if test="${emptyList !=null}">
        <fmt:message key="${emptyList}"/>
    </c:if>
    <c:choose>
        <c:when test="${lotsByCategory==null}">
            <c:forEach var="lot" items="${lots}" varStatus="status">
                <a class="lot-title" href="/Controller?command=showLot&id=${lot.getId()}"><h2>${lot.getTitle()}</h2></a>
                <img style="width: 50%" src="${lot.getImage()}">
            </c:forEach>
        </c:when>
        <c:otherwise>
            <label><fmt:message key="selected.category"/>${categoryValue} </label>
            <c:forEach var="lot" items="${lotsByCategory}" varStatus="status">
                <a class="lot-title" href="/Controller?command=showLot&id=${lot.getId()}"><h3>${lot.getTitle()}</h3></a>
                <img style="width: 50%" src="${lot.getImage()}">
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
