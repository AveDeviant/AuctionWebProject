<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 13.03.2017
  Time: 23:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="cabinet.page.title"/></title>
</head>
<body>
<c:import url="${pageContext.request.contextPath}/fragments/header.jsp"/>
<div class="container">
    <c:if test="${user eq null or user.getRole().getValue()!='customer'}">
        <c:redirect url="/Controller"/>
    </c:if>

    <c:choose>
        <c:when test="${user.getBankCard() eq null }">
            <fmt:message key="bankaccount.registration.notice"/>
            <button class="button-auction" type="button" onclick="showCardForm()"><fmt:message
                    key="register.page.title"/></button>
            <div id="cardForm" style="display: none;">
                <form action="/Controller" method="post">
                    <input type="radio" name="system" value="Visa" checked>Visa
                    <input type="radio" name="system" value="MasterCard">MasterCard
                    <br/>
                    <input name="number" required pattern="[\d]{4}-[\d]{4}-[\d]{4}-[\d]{4}"
                           title="<fmt:message key="bankaccount.registration.card"/> ">
                    <br>
                    <button class="button-auction" type="submit" name="command" value="addBankAccount">
                        <fmt:message key="card.register.button"/></button>
                    <input type="hidden" name="jspPath"
                           value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">

                </form>
                <label class="alert-danger">
                    <c:if test="${bankErr !=null}">
                        <fmt:message key="${bankErr}"/>
                    </c:if>
                </label>
            </div>
        </c:when>
        <c:otherwise>
            <fmt:message key="bankaccount.userinfo"/><br/>
            <c:out value="${user.getBankCard().toString()}"/>
        </c:otherwise>
    </c:choose>
        <a href="${pageContext.request.contextPath}/Controller?command=goTo&page=message"><fmt:message
                key="messages.page.reference"/> </a>
    <c:choose>
        <c:when test="${!user.getBets().isEmpty()}">
            <c:forEach var="bet" items="${user.getBets()}">
                <c:out value="${bet.getDate()}"/>
                <c:out value="${bet.getLotTitle()}"/>
                <c:out value="${bet.getBet()}"/>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <fmt:message key="private.emptyBetList"/>
        </c:otherwise>
    </c:choose>
    <form name="logout" action="/Controller" method="post" style="margin: 20px">
        <button class="button-auction" type="submit" name="button"><fmt:message key="private.button.logout"/></button>
        <input type="hidden" name="command" value="logout">
        <input type="hidden" name="jspPath" value="${pageContext.request.servletPath}"/><br>
    </form>
</div>
<script src="/js/script.js"></script>
</body>
</html>
