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
    <div class="custom-opacity">
        <c:if test="${user eq null or user.getRole().getValue()!='customer'}">
            <c:redirect url="/Controller"/>
        </c:if>
        <jsp:include page="${pageContext.request.contextPath}/Auction">
            <jsp:param name="command" value="getOperations"/>
        </jsp:include>
        <c:if test="${error ne null}">
            <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="${error}"/>
            </div>
        </c:if>
        <div class="row">
            <div class="col-sm-6">
                <c:choose>
                    <c:when test="${!bets.isEmpty()}">
                        <div class="col-sm-12">
                            <h3 class="text-center"><fmt:message key="private.page.bets.title"/></h3>
                        </div>
                        <div class="help-block"><fmt:message key="private.page.bets.notice"/></div>
                        <table class="table">
                            <thead>
                            <tr>
                                <th><fmt:message key="private.page.bets.table.date"/></th>
                                <th><fmt:message key="admin.lot.edit.title.title"/></th>
                                <th><fmt:message key="private.page.bets.table.bet"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="bet" items="${bets}">
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
                <h3 class="text-center"><fmt:message key="private.page.orders"/></h3>
                <table class="table">
                    <thead>
                    <tr>
                        <th><fmt:message key="private.page.bets.table.bet"/></th>
                        <th><fmt:message key="private.page.bets.table.bet"/></th>
                        <th><fmt:message key="trader.ref"/></th>
                        <th><fmt:message key="private.page.bets.table.date"/></th>
                        <th><fmt:message key="trader.rating"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td><c:out value="${order.getLotTitle()}"/></td>
                            <td><c:out value="${order.getPayment()}"/></td>
                            <td><c:out value="${order.getTraderUsername()}"/></td>
                            <td><c:out value="${order.getDateTime()}"/></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/Auction">
                                    <select name="rating">
                                        <c:forEach var="mark" items="${rating}">
                                            <option value=${mark}>${mark}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="user-operations"><fmt:message key="faq.form.send"/></button>
                                    <input type="hidden" name="command" value="updateRating">
                                    <input type="hidden" name="traderId" value="${order.getTraderId()}">
                                    <input type="hidden" name="jspPath"
                                           value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="col-sm-4">
                <c:choose>
                    <c:when test="${user.getBankCard() eq null }">
                        <h3><fmt:message key="bankaccount.registration.notice"/>
                            <a href="#" onclick="showCardForm()"><fmt:message
                                    key="register.page.title"/></a>
                        </h3>
                        <div id="cardForm" style="display: none;">
                            <form action="${pageContext.request.contextPath}/Auction" method="post">
                                <input type="radio" name="system" value="Visa" checked><span>Visa</span>
                                <input type="radio" name="system" value="MasterCard"><span>MasterCard</span>
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
                    <h3 class="text-center"><a
                            href="${pageContext.request.contextPath}/Auction?command=goTo&page=message"><fmt:message
                            key="messages.page.reference"/> </a>
                    </h3>
                </div>
            </div>
            <div class="col-sm-2">
                <div class="text-center">
                    <form name="logout" action="${pageContext.request.contextPath}/Auction" method="post">
                        <button id="logout" type="submit" name="button"><fmt:message
                                key="private.button.logout"/></button>
                        <input type="hidden" name="command" value="logout">
                        <input type="hidden" name="jspPath" value="${pageContext.request.servletPath}"/><br>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    function showCardForm() {
        var form = document.getElementById("cardForm");
        if (form.style.display === "none") {
            form.style.display = "inline";
        } else {
            form.style.display = "none";
        }
    }
</script>
</body>
</html>
