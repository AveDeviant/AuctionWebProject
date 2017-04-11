<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 25.03.2017
  Time: 19:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<c:import url="/fragments/header.jsp"/>
<head>
    <title><fmt:message key="offer.lot.title"/></title>
</head>
<body>
<div class="container">
    <div class="row">
        <h1 class="text-center"><fmt:message key="offer.lot.page.head"/></h1>
    </div>
    <div class="row">
    <h3 class="text-center"><fmt:message key="offer.lot.page"/></h3>
    </div>
    <c:choose>
        <c:when test="${user eq null}">
            <fmt:message key="offer.lot.page.notice"/>
            <a href="${pageContext.request.contextPath}/Controller?command=goTo&page=authorization"><fmt:message
                    key="offer.lot.reference.authorization"/></a>
        </c:when>
        <c:otherwise>
            <c:if test="${offerErr !=null}">
                <fmt:message key="offer.bank.account"/>
            </c:if>
            <form name="addingLot" action="${pageContext.request.contextPath}/Controller" enctype="multipart/form-data" method="post">
                <span><fmt:message key="admin.lot.title"/> </span>
                <input type="text" name="title" title="<fmt:message key="admin.lot.title.restrict"/>" required>
                <span class="err" id="errTitle" style="display: none"><fmt:message
                        key="admin.lot.title.restrict"/> </span>
                <br/>
                <span><fmt:message key="admin.lot.description"/> </span>
                <input type="text" name="description"><br/>
                <span><fmt:message key="admin.lot.image"/> </span>
                <input type="file" name="image" required><br/>
                <c:if test="${imageErr!=null}">
                    <label class="error"><fmt:message key="${imageErr}"/></label>
                </c:if>
                <span><fmt:message key="admin.lot.startingprice"/> </span>
                <input type="text" name="price" pattern="^[1-9][0-9]*.[0-9]{2}" required
                       title="<fmt:message key="bet.restrict"/>"><br/>
                <span><fmt:message key="lot.timing"/> </span>
                <input type="date" name="availableTiming" id="availableTiming">
                <label class="label-danger" id="errDate" style="display: none"><fmt:message
                        key="admin.lot.timing.err"/> </label><br/>
                <span><fmt:message key="admin.lot.value"/> </span>
                <select name="category">
                    <c:forEach var="option" items="${categories}">
                        <option value="${option.getValue()}" selected><c:out value="${option.getValue()}"/></option>
                    </c:forEach>
                </select> <br/>
                <button type="submit" onclick=" return checkDate()"><fmt:message key="button.add"/></button>
                <input type="hidden" name="command" value="addLot"/>
                <input type="hidden" name="jspPath"
                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
            </form>
        </c:otherwise>
    </c:choose>
</div>
<script>
    function checkDate() {
        var valid = true;
        var checkedDate = document.addingLot.availableTiming.value;
        var errDate = document.getElementById("errDate");
        var arr = checkedDate.toString().split("-");
        var year = arr[0];
        var month = arr[1];
        var day = arr[2];
        var date = new Date(year, month, day);
        var currentTime = new Date();
        if (date.getTime() < currentTime) {
            valid = false;
            errDate.style.display = "inline";
        }
        return valid;
    }
</script>
</body>
</html>
