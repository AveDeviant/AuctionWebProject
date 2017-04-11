<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 15.03.2017
  Time: 0:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="admin.title"/></title>
</head>
<body>
<c:import url="/fragments/header.jsp"/>
<div class="container">
    <c:if test="${user eq null or user.getRole().getValue()!='admin'}">
        <c:redirect url="/Controller"/>
    </c:if>

    <div class="row">
        <div class="col-sm-12">
            <label class="text-center alert-danger">
                <c:if test="${addErr!=null}">
                    <fmt:message key="${addErr}"/>
                </c:if>
            </label>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-4">
            <h3><fmt:message key="admin.edit.page.lot.title"/></h3>
            <form name="addingLot" action="${pageContext.request.contextPath}/Controller" enctype="multipart/form-data"
                  method="post">
                <label for="title"><fmt:message key="admin.lot.title"/> </label>
                <input class="form-control" type="text" id="title" name="title"
                       title="<fmt:message key="admin.lot.title.restrict"/>" required>
                <span class="err" id="errTitle" style="display: none"><fmt:message
                        key="admin.lot.title.restrict"/> </span>
                <label for="description"><fmt:message key="admin.lot.description"/> </label>
                <textarea class="form-control" rows="4" id="description" name="description"></textarea>
                <label for="image"><fmt:message key="admin.lot.image"/> </label>
                <input type="file" class="form-control" name="image" id="image" required>
                <c:if test="${imageErr!=null}">
                    <label class="alert-danger"><fmt:message key="${imageErr}"/></label>
                </c:if>
                <label for="price"><fmt:message key="admin.lot.startingprice"/> </label>
                <input class="form-control" type="text" id="price" name="price" pattern="^[1-9][0-9]*.[0-9]{2}" required
                       title="<fmt:message key="bet.restrict"/> " placeholder="<fmt:message key="bet.restrict"/>">
                <label for="availableTiming"><fmt:message key="lot.timing"/> </label>
                <input class="form-control" type="date" name="availableTiming" id="availableTiming">
                <label id="errDate" style="display: none" class="alert-danger"><fmt:message
                        key="admin.lot.timing.err"/> </label><br/>
                <label for="category"><fmt:message key="admin.lot.value"/> </label>
                <select name="category" id="category">
                    <c:forEach var="option" items="${categories}">
                        <option value="${option.getValue()}" selected><c:out value="${option.getValue()}"/></option>
                    </c:forEach>
                </select> <br/>
                <button class="button-auction" type="submit" onclick=" return checkDate()"><fmt:message
                        key="button.add"/></button>
                <input type="hidden" name="command" value="addLot"/>
                <input type="hidden" name="jspPath"
                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
            </form>
        </div>
        <div class="col-sm-4">
            <label class="alert-danger">
            <c:if test="${categoryErr!=null}">
                <fmt:message key="${categoryErr}"/>
            </c:if>
            </label>
            <h3><fmt:message key="admin.edit.page.category.title"/> </h3>
            <form method="get" action="${pageContext.request.contextPath}/Controller">
                <label for="name"><fmt:message key="admin.new.category"/></label>
                <input class="form-control" type="text" name="name" id="name" required pattern="[A-Za-z А-Яа-я-]{2,}"><br/>
                <button type="submit" class="button-auction"><fmt:message key="button.add"/></button>
                <input type="hidden" name="command" value="addCategory">
                <input type="hidden" name="jspPath"
                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
            </form>
        </div>
        <div class="col-sm-4">
            <h3><fmt:message key="admin.management"/></h3>
            <h6><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=editLot"><fmt:message
                    key="admin.lot.edit"/> </a></h6><br/>
           <h6><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=editUser"><fmt:message
                   key="admin.user.edit"/> </a></h6><br/>
            <h6><a href="${pageContext.request.contextPath}/Controller?command=getOrders"><fmt:message key="admin.order.edit"/></a></h6><br/>
            <h6><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=message"><fmt:message
                    key="messages.page.reference"/> </a></h6>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="text-center">
    <form name="logout" action="${pageContext.request.contextPath}/Controller" method="post">
        <button class="button-auction" type="submit" name="button"><fmt:message key="private.button.logout"/></button>
        <input type="hidden" name="command" value="logout">
        <input type="hidden" name="jspPath"
               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}"/><br>
    </form>
            </div>
        </div>
    </div>
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