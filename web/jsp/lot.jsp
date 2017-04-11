<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="avail-date" uri="availdate" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 18.03.2017
  Time: 23:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:import url="/fragments/header.jsp"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<head>
    <title>${lot.getTitle()}</title>
</head>
<body>
<div class="container">

    <c:if test="${banned !=null}">
        <div class="row">
            <div class="col-sm-12">
                <label class="text-center alert-danger"><fmt:message key="${banned}"/> </label>
            </div>
        </div>
    </c:if>

    <div class="row">
        <div class="col-sm-12">
            <h1 class="text-center">${lot.getTitle()}</h1>
            <h4 class="text-center">ID: ${lot.getId()}</h4>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-8">
            <img class="img-responsive" src="${lot.getImage()}">
        </div>
        <div class="col-sm-4">
            <h4><fmt:message key="lot.page.startingPrice"/></h4>
            <h2>${lot.getPrice()}</h2>
            <h4><fmt:message key="lot.page.currentPrice"/></h4>
            <h2>${lot.getCurrentPrice()}</h2>
            <h5><fmt:message key="lot.timing"/></h5>
            <h4><avail-date:avail-date/></h4>
            <c:choose>
                <c:when test="${user!=null && user.getAccess() eq false || user.getRole().getValue() eq 'admin'}">
                    <button type="button" class="button-auction-disabled" disabled>
                        <fmt:message key="lot.bet.button"/></button>
                </c:when>
                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/Controller" method="post">

                        <label for="price"><fmt:message key="lot.bet.input"/></label>
                        <input class="form-control" type="text" name="price" id="price" pattern="^[1-9][0-9]*.[0-9]{2}"
                           required title="<fmt:message key="bet.restrict"/> "
                               placeholder="<fmt:message key="bet.restrict"/>"><br/>
                        <button class="button-auction" type="submit" name="command" value="makeBet"><fmt:message
                                key="lot.bet.button"/></button>
                        <input type="hidden" name="lotId" value="${lot.getId()}">
                        <input type="hidden" name="userId" value="${user.getUserId()}">
                        <input type="hidden" name="jspPath"
                               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">

                    </form>
                    <label class="alert-danger">
                        <c:if test="${betErr != null}">
                            <fmt:message key="${betErr}"/>
                        </c:if>
                    </label>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <h6>${lot.getDescription()}</h6>
        </div>
    </div>
    <div class="row">
    <div align="center"> <button class="button-auction" type="button" onclick="showBets()"><fmt:message
            key="lot.page.button.showBets"/></button>
    </div>
    </div>
    <br/>
    <div id="bets" style="display: none;">
        <c:forEach var="bet" items="${lot.getBets()}">
            <div class="row">
                <div class="col-sm-6" align="center">
                    <label class="text-center"><c:out value="${bet.getDate()}"/></label>
                </div>
                <div class="col-sm-6" align="center">
                    <label class="text-center"><c:out value="${bet.getBet()}"/></label>
                </div>
                </div>
        </c:forEach>
    </div>
</div>
    </div>
</div>
<script>
    function showBets() {
        var bets = document.getElementById("bets");
        if (bets.style.display === "none") {
            bets.style.display = "inline";
        }
        else {
            bets.style.display = "none";
        }
    }
</script>
</body>
</html>
