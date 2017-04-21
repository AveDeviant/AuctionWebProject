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
            <c:if test="${addErr !=null}">
                <div class=" alert alert-danger alert-dismissable fade in">
                    <fmt:message key="${addErr}"/>
                </div>
            </c:if>
            <div class="row">
                <div class="col-sm-12">
                    <form name="addingLot" action="${pageContext.request.contextPath}/Controller"
                          enctype="multipart/form-data" method="post">
                        <label for="title"><fmt:message key="admin.lot.title"/> </label>
                        <input class="form-control" type="text" name="title" id="title"
                               title="<fmt:message key="admin.lot.title.restrict"/>" required>
                        <span class="err" id="errTitle" style="display: none"><fmt:message
                                key="admin.lot.title.restrict"/> </span>
                        <br/>
                        <label for="description"><fmt:message key="admin.lot.description"/> </label>
                        <textarea class="form-control" rows="4" name="description" id="description"
                                  oninput=" return checkLength()"></textarea>
                        <label class="help-block" id="symbolCount"></label><br/><label id="descriptErr"
                                                                                       class="alert-danger">
                    </label><br/>
                        <label for="image"><fmt:message key="admin.lot.image"/> </label>
                        <input class="form-control" type="file" name="image" id="image" required><br/>
                        <c:if test="${imageErr!=null}">
                            <label class="error"><fmt:message key="${imageErr}"/></label>
                        </c:if>
                        <label for="price"><fmt:message key="admin.lot.startingprice"/> </label>
                        <input class="form-control" type="text" name="price" id="price" pattern="^[1-9][0-9]*.[0-9]{2}"
                               required
                               title="<fmt:message key="bet.restrict"/>"
                               placeholder="<fmt:message key="bet.restrict"/>">
                         <br/>
                        <label for="availableTiming"><fmt:message key="lot.timing"/> </label>
                        <input class="form-control" type="date" name="availableTiming" id="availableTiming">
                        <label class="alert-danger" id="errDate"></label><br/>
                        <label for="category"><fmt:message key="admin.lot.value"/> </label>
                        <select name="category" id="category">
                            <c:forEach var="option" items="${categories}">
                                <option value="${option.getValue()}" selected><c:out
                                        value="${option.getValue()}"/></option>
                            </c:forEach>
                        </select> <br/>
                        <button class="button-auction" type="submit" onclick=" return checkInput()"><fmt:message
                                key="button.add"/></button>
                        <input type="hidden" name="command" value="addLot"/>
                        <input type="hidden" name="jspPath"
                               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                    </form>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script>
    function checkInput() {
        var valid = true;
        var checkedDate = document.addingLot.availableTiming.value;
        var textArea = document.getElementById("description").value;
        var errDate = document.getElementById("errDate");
        var MAX_PRICE = 9999999.99;
        var arr = checkedDate.toString().split("-");
        var year = arr[0];
        var month = arr[1];
        var day = arr[2];
        var date = new Date(year, month, day);
        var currentTime = new Date();
        if (date.getTime() < currentTime) {
            valid = false;
            errDate.innerHTML = '<fmt:message key="admin.lot.timing.err"/> ';
        }
        if (textArea.length > 1000) {
            valid = false;
            var error = document.getElementById("descriptErr");
            error.innerHTML = '<fmt:message key="description.length.error"/>';
        }
        return valid;
    }

    function checkLength() {
        var textArea = document.getElementById("description").value;
        var count = document.getElementById("symbolCount");
        count.innerHTML = String(1000 - textArea.length);
        if (textArea.length > 1000) {
            count.style.color = "red";
        }
    }
</script>
</body>
</html>
