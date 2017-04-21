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
    <div class="row">
        <div class="col-sm-6">
            <c:choose>
                <c:when test="${!user.getBets().isEmpty()}">
                    <div class="col-sm-12">
                        <h3 class="text-center"><fmt:message key="private.page.bets.title"/></h3>
                    </div>
                    <div class="help-block"><fmt:message key="private.page.bets.notice"/></div>
                    <table class="table" id="user-stat">
                        <thead>
                        <tr>
                            <th><fmt:message key="private.page.bets.table.date"/></th>
                            <th><fmt:message key="admin.lot.edit.title.title"/></th>
                            <th><fmt:message key="private.page.bets.table.bet"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="bet" items="${user.getBets()}">
                            <tr>
                                <td><c:out value="${bet.getDate()}"/></td>
                                <td><c:out value="${bet.getLotTitle()}"/></td>
                                <td><c:out value="${bet.getBet()}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="col-sm-12">
                        <h3 class="text-center"><fmt:message key="private.emptyBetList"/></h3>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="col-sm-6">
            <c:choose>
                <c:when test="${user.getBankCard() eq null }">
                    <fmt:message key="bankaccount.registration.notice"/>
                    <a href="#" onclick="showCardForm()"><fmt:message
                            key="register.page.title"/></a>
                    <div id="cardForm" style="display: none;">
                        <form action="${pageContext.request.contextPath}/Controller" method="post">
                            <input type="radio" name="system" value="Visa" checked>Visa
                            <input type="radio" name="system" value="MasterCard">MasterCard
                            <br/>
                            <input class="form-control" name="number" required pattern="\d{4}-\d{4}-\d{4}-\d{4}"
                                   title="<fmt:message key="bankaccount.registration.card"/>"
                                   placeholder="XXXX-XXXX-XXXX-XXXX">
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
                    <h3><fmt:message key="bankaccount.userinfo"/></h3><br/>
                    <h5><c:out value="${user.getBankCard().toString()}"/></h5>
                </c:otherwise>
            </c:choose>
            <div class="row">
                <h6 class="text-center"><a
                        href="${pageContext.request.contextPath}/Controller?command=goTo&page=message"><fmt:message
                        key="messages.page.reference"/> </a>
                </h6>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="text-center">
                <form name="logout" action="${pageContext.request.contextPath}/Controller" method="post">
                    <button class="button-auction" type="submit" name="button"><fmt:message
                            key="private.button.logout"/></button>
                    <input type="hidden" name="command" value="logout">
                    <input type="hidden" name="jspPath" value="${pageContext.request.servletPath}"/><br>
                </form>
            </div>
        </div>
    </div>
    <script src="/js/script.js"></script>
</body>
</html>
