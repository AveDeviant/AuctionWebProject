<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="avail-date" uri="availdate" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:import url="/fragments/header.jsp"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<head>
    <title>${lot.getTitle()}</title>
</head>
<body onload="timeRemaining()">
<input type="hidden" id="remainingTime" value="${lot.getDateAvailable().toString()}">
<div class="custom-opacity">
    <div class="container">
        <c:if test="${banned ne null}">
            <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="user.banned.title"/></div>
        </c:if>
        <c:set var="step" scope="page" value="${lot.getPrice()/100*5}"/>
        <c:if test="${betErr ne null}">
            <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="${betErr}"/></div>
        </c:if>
        <c:if test="${betSizeErr ne null}">
            <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="${betSizeErr}"/><c:out value="${lot.getCurrentPrice()+step}"/></div>
        </c:if>
        <c:if test="${error ne null}">
            <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="${error}"/></div>
        </c:if>
        <div class="row">
            <div class="col-sm-12">
                <div class="col-sm-6">
                    <h2 class="text-center">${lot.getTitle()}</h2>
                </div>
                <div class="col-sm-3">
                    <div class="lot-stat">
                        <div class="row">
                            <h6 class="text-center">ID: ${lot.getId()}</h6>
                            <div class="row">
                                <h6 class="text-center"><span
                                        class="glyphicon glyphicon-eye-open"
                                        title="<fmt:message key="lot.followers"/>"></span> ${lot.getFollowersCount()}
                                </h6>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-3">
                    <div class="lot-stat">
                        <div class="row">
                            <h6 class="text-center" id="remaining"></h6>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-6">
                <img class="img-responsive" src="${lot.getImage()}">
                <h6>${lot.getDescription()}</h6>
                <br/>
                <a href="#comments" onclick="showComments()"><fmt:message
                        key="show.comments"/></a>
                <div id="comments" style="display: none">
                    <div class="row">
                        <c:forEach var="comment" items="${lot.getComments()}">
                            <span><strong><c:out value="${comment.getUserAlias()}"/></strong></span><br/>
                            <span><c:out value="${comment.getContent()}"/></span><br/>
                        </c:forEach>
                        <form method="post" action="${pageContext.request.contextPath}/Auction">
                            <label for="content"><fmt:message key="comment.title"/></label><label
                                class="required">*</label>
                            <textarea type="text" name="content" id="content" required rows="5"
                                      class="form-control"></textarea>
                            <span class="help-block"><fmt:message key="comment.length"/></span>
                            <br/>
                            <button class="button-auction" type="submit" name="command" value="comment"><fmt:message
                                    key="faq.form.send"/></button>
                            <input type="hidden" name="jspPath"
                                   value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            <input type="hidden" name="lotId" value="${lot.getId()}">
                        </form>
                    </div>
                </div>
            </div>
            <div class="col-sm-3">
                <h5><fmt:message key="lot.page.startingPrice"/></h5>
                <h2 id="start-price">${lot.getPrice()}</h2>
                <h5><fmt:message key="lot.page.currentPrice"/></h5>
                <h2 id="current-price">${lot.getCurrentPrice()}</h2>
                <h5><fmt:message key="lot.timing"/></h5>
                <h4><avail-date:avail-date/></h4>
                <c:choose>
                    <c:when test="${user!=null && user.getAccess() eq false || user.getRole().getValue() eq 'admin'}">
                        <button type="button" class="button-auction-disabled" disabled>
                            <fmt:message key="lot.bet.button"/></button>
                    </c:when>
                    <c:otherwise>
                        <form action="${pageContext.request.contextPath}/Auction" method="post">
                            <label for="price"><fmt:message key="lot.bet.input"/></label>
                            <input class="form-control" type="text" name="price" id="price"
                                   pattern="^[1-9][0-9]*.[0-9]{2}"
                                   required title="<fmt:message key="bet.restrict"/> "
                                   placeholder="${lot.getCurrentPrice()+step}">
                            <br/>
                            <button class="button-auction" type="submit" name="command"
                                    value="makeBet"><fmt:message
                                    key="lot.bet.button"/></button>
                            <input type="hidden" name="lotId" value="${lot.getId()}">
                            <input type="hidden" name="jspPath"
                                   value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">

                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col-sm-3">
                <h5><fmt:message key="trader.ref"/></h5>
                <h6>${trader.getAlias()}</h6>
                <a href="${pageContext.request.contextPath}/Auction?command=traderLots&traderId=${trader.getUserId()}"><fmt:message
                        key="trader.lot.ref"/>
                </a>
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
                <a href="#bets" onclick="showBets()"><fmt:message
                        key="lot.page.button.showBets"/></a>
                <br/>
                <div id="bets" class="bets" style="display: none;">
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
</div>
<script async="async">
    function showBets() {
        var bets = document.getElementById("bets");
        if (bets.style.display === "none") {
            bets.style.display = "inline";
        }
        else {
            bets.style.display = "none";
        }
    }
    function showComments() {
        var comments = document.getElementById("comments");
        if (comments.style.display === "none") {
            comments.style.display = "inline";
        }
        else {
            comments.style.display = "none";
        }
    }
    function timeRemaining() {
        var remaining = document.getElementById("remaining");
        var date = document.getElementById("remainingTime").value;
        var dateAsArray = date.toString().split("-");
        lotYear = dateAsArray[0];
        lotMonth = dateAsArray[1];
        lotDay = dateAsArray[2];
        var lotDate = Date.parse(lotMonth + "/" + lotDay + "/" + lotYear);
        var now = new Date();
        var sec = (lotDate - now) / 1000;
        if (sec > 0) {
            var days = Math.floor(sec / (60 * 60 * 24));
            days = (days < 10) ? "0" + days : days;
            var hours = 24 - now.getHours() - 1;
            hours = (hours < 10) ? "0" + hours : hours;
            var minutes = 60 - now.getMinutes() - 1;
            minutes = (minutes < 10) ? "0" + minutes : minutes;
            var seconds = 60 - now.getSeconds() - 1;
            seconds = (seconds < 10) ? "0" + seconds : seconds;
            remaining.innerHTML = '<fmt:message key="timer.remaining"/>' + " " + '<fmt:message key="timer.days"/>' + " " + days
                + " " + '<fmt:message key="timer.hours"/>' + " " + hours + " " + '<fmt:message key="timer.minutes"/>' + " "
                + minutes + " " + '<fmt:message key="timer.seconds"/>' + " " + seconds;
        } else {
            remaining.innerHTML = '<fmt:message key="timer.over"/>';
        }
        window.setTimeout("timeRemaining()", 1000)
    }
</script>
<c:import url="${pageContext.request.contextPath}/fragments/footer.jsp"/>
</body>
</html>
