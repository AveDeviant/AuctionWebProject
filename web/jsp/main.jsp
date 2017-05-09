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
<div class="custom-opacity">
    <div class="container">
        <div class="custom-wrapper">
            <div class="row">
                <c:if test="${err != null}">
                    <div class=" alert alert-danger alert-dismissable fade in">
                        <fmt:message key="${err}"/></div>
                </c:if>
                <c:if test="${banned != null}">
                    <div class=" alert alert-danger alert-dismissable fade in">
                        <fmt:message key="user.banned.title"/></div>
                </c:if>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <c:if test="${emptyList !=null}">
                        <h1 class="text-center"><fmt:message key="${emptyList}"/></h1>
                    </c:if>
                </div>
            </div>
            <c:choose>
                <c:when test="${lotsByCategory==null}">
                    <c:set var="counter" value="${0}" scope="page"/>
                    <c:forEach var="lot" items="${lots}" varStatus="status">
                        <c:if test="${(counter%2) eq 0}">
                            <div class="row"></c:if>
                        <div class="col-sm-6">
                            <a class="lot-title"
                               href="${pageContext.request.contextPath}/Auction?command=showLot&id=${lot.getId()}">
                                <h4>${lot.getTitle()}</h4></a>
                            <img style="height:350px" src="${lot.getImage()}" alt="${lot.getTitle()}">
                        </div>
                        <c:set var="counter" value="${counter+1}" scope="page"/>
                        <c:if test="${(counter%2) eq 0}"></div>
                            <br></c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <h5 class="text center"><fmt:message key="selected.category"/>${categoryValue} </h5>
                    <c:set var="counter" value="${0}" scope="page"/>
                    <c:forEach var="lot" items="${lotsByCategory}" varStatus="status">
                        <c:if test="${(counter%2) eq 0}">
                            <div class="row"></c:if>
                        <div class="col-sm-6">
                            <a class="lot-title"
                               href="${pageContext.request.contextPath}/Auction?command=showLot&id=${lot.getId()}">
                                <h4>${lot.getTitle()}</h4></a>
                            <img style="height:350px" src="${lot.getImage()}" alt="${lot.getTitle()}">
                        </div>
                        <c:set var="counter" value="${counter+1}" scope="page"/>
                        <c:if test="${(counter%2) eq 0}"></div>
                            <br></c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
<c:import url="${pageContext.request.contextPath}/fragments/footer.jsp"/>
</html>
