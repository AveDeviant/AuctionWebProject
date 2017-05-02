<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 02.05.2017
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<c:import url="${pageContext.request.contextPath}/fragments/header.jsp"/>
<head>
    <title>Лоты продавца</title>
</head>
<body>
<div class="container">
    <div class="custom-opacity">
        <div class="col-sm-12">
            <c:set var="counter" value="${0}" scope="page"/>
            <c:forEach var="lot" items="${traderLots}">
                <c:if test="${(counter%2) eq 0}">
                    <div class="row"></c:if>
                <div class="col-sm-4">
                    <a class="lot-title"
                       href="${pageContext.request.contextPath}/Auction?command=showLot&id=${lot.getId()}">
                        <h5>${lot.getTitle()}</h5></a>
                    <img style="height:250px" src="${lot.getImage()}" alt="${lot.getTitle()}">
                </div>
                <c:set var="counter" value="${counter+1}" scope="page"/>
                <c:if test="${(counter%2) eq 0}"></div>
                    <br></c:if>
            </c:forEach>
            <div class="col-sm-4">
                <h5><fmt:message key="trader.ref"/></h5>
                <h6>${trader.getAlias()}</h6>
                <h5><fmt:message key="trader.rating"/></h5>
                <c:choose>
                    <c:when test="${trader.getUserRating() eq 0.0}">
                        <h3><fmt:message key="trader.rating.unable"/></h3>
                    </c:when>
                    <c:otherwise>
                        <h3>${trader.getUserRating()}</h3>
                    </c:otherwise>
                </c:choose>
                <c:if test="${trader.getCity() ne null}">
                    <h5><fmt:message key="trader.ref.city"/></h5>
                    <p>${trader.getCity()}</p>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
