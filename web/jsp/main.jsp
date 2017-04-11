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
    <div class="row">
        <c:if test="${err!=null}">
            <fmt:message key="${err}"/>
        </c:if>
        <c:if test="${banned !=null}">
            <label class="text-center alert-danger"><fmt:message key="${banned}"/> </label>
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
            <c:forEach var="lot" items="${lots}" varStatus="status">
                <div class="row">
                    <div class="col-sm-8">
                        <a class="lot-title"
                           href="${pageContext.request.contextPath}/Controller?command=showLot&id=${lot.getId()}">
                            <h2>${lot.getTitle()}</h2></a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-8">
                        <img style="width:80%" src="${lot.getImage()}">
                    </div>
                    <div class="col-sm-4">
                        <h4><fmt:message key="lot.page.currentPrice"/></h4>
                        <h2>${lot.getCurrentPrice()}</h2>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <label><fmt:message key="selected.category"/>${categoryValue} </label>
            <c:forEach var="lot" items="${lotsByCategory}" varStatus="status">
                <div class="row">
                    <div class="col-sm-8">
                        <a class="lot-title"
                           href="${pageContext.request.contextPath}/Controller?command=showLot&id=${lot.getId()}">
                            <h2>${lot.getTitle()}</h2></a>
                        <img style="width:80%" src="${lot.getImage()}">
                    </div>
                    <div class="col-sm-4">
                        <h4><fmt:message key="lot.page.currentPrice"/></h4>
                        <h2>${lot.getCurrentPrice()}</h2>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
