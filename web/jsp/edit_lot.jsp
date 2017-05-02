<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 27.03.2017
  Time: 17:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="admin.title"/></title>
</head>
<c:import url="/fragments/header.jsp"/>
<body>
<div class="container-fluid">
    <div class="custom-opacity">
    <c:if test="${user eq null or user.getRole().getValue()!='admin'}">
        <c:redirect url="/Auction"/>
    </c:if>
    <jsp:include page="${pageContext.request.contextPath}/Auction">
        <jsp:param name="command" value="getLots"/>
    </jsp:include>
        <c:if test="${editErr != null}">
        <div class=" alert alert-danger alert-dismissable fade in">
            <fmt:message key="${editErr}"/>
        </div>
        </c:if>
    <div class="col-sm-4">
        <form action="${pageContext.request.contextPath}/Auction" method="post" enctype="multipart/form-data"
              name="editLot">
            <label for="id"><fmt:message key="admin.lot.edit.id"/></label>
            <input class="form-control" type="number" name="id" id="id" required>
            <label for="title"><fmt:message key="admin.lot.edit.title"/></label>
            <input class="form-control" type="text" name="title" id="title" required>
            <label for="image"><fmt:message key="admin.lot.image"/> </label>
            <input class="form-control" type="file" id="image" name="image" required>
            <label for="price"><fmt:message key="admin.lot.startingprice"/></label>
            <input class="form-control" type="text" name="price" id="price" pattern="^[1-9][0-9]*.[0-9]{2}" required
                   title="<fmt:message key="bet.restrict"/>" placeholder="<fmt:message key="bet.restrict"/>">
            <label for="date"><fmt:message key="lot.timing"/></label>
            <input class="form-control" type="date" id="date" name="availableTiming" required/>
            <label class="alert-danger" id="errDate"></label><br/>
            <label for="availability"><fmt:message key="admin.lot.edit.availability"/></label><br/>
            <input type="checkbox" id="availability" name="availability" value="true" checked/><br/>
            <label for="category"><fmt:message key="lot.edit.page.category"/></label>
            <select name="category" id="category">
                <c:forEach var="option" items="${categories}">
                    <option value="${option.getValue()}" selected><c:out value="${option.getValue()}"/></option>
                </c:forEach>
            </select>
            <br/>
            <button class="button-auction" type="submit" onclick="return checkDate()"><fmt:message
                    key="admin.lot.edit.button"/></button>
            <input type="hidden" name="command" value="editLot">
            <input type="hidden" name="jspPath"
                   value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
        </form>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>user ID</th>
            <th>ID</th>
            <th><fmt:message key="lot.edit.table.lot.title"/></th>
            <th><fmt:message key="lot.edit.page.category"/></th>
            <th><fmt:message key="admin.lot.edit.title.image"/></th>
            <th><fmt:message key="lot.edit.table.lot.price"/></th>
            <th><fmt:message key="lot.edit.table.lot.current"/></th>
            <th><fmt:message key="lot.edit.table.lot.date"/></th>
            <th><fmt:message key="lot.edit.table.lot.availability"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="lot" items="${allLots}">
            <tr>
                <td><c:out value="${lot.getUserId()}"/></td>
                <td><c:out value="${lot.getId()}"/></td>
                <td><c:out value="${lot.getTitle()}"/></td>
                <td><c:out value="${lot.getCategory()}"/></td>
                <td><img src="${lot.getImage()}" style="width: 20%"/></td>
                <td><c:out value="${lot.getPrice()}"/></td>
                <td><c:out value="${lot.getCurrentPrice()}"/></td>
                <td><c:out value="${lot.getDateAvailable()}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${lot.getAvailability() eq true}">
                            <input type="checkbox" name="availability" value="true" checked/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="availability" value="true"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/Auction">
                        <button class="button-auction" type="submit" name="command" value="deleteLot">
                            <fmt:message key="admin.edit.lot.delete"/></button>
                        <input type="hidden" name="lotId" value="${lot.getId()}">
                        <input type="hidden" name="jspPath"
                               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                    </form>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/Controller">
                        <c:choose>
                            <c:when test="${lot.getAvailability() eq true}">
                                <button class="button-auction" type="submit"><fmt:message key="admin.edit.lot.withdraw"/></button>
                                <input type="hidden" name="command" value="lotStatus">
                                <input type="hidden" name="id" value="${lot.getId()}">
                                <input type="hidden" name="status" value="false">
                                <input type="hidden" name="jspPath"
                                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            </c:when>
                            <c:otherwise>
                                <button class="button-auction" type="submit"><fmt:message key="admin.edit.lot.accept"/></button>
                                <input type="hidden" name="command" value="lotStatus">
                                <input type="hidden" name="id" value="${lot.getId()}">
                                <input type="hidden" name="status" value="true">
                                <input type="hidden" name="jspPath"
                                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            </c:otherwise>
                        </c:choose>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<script>
    function checkDate() {
        var valid = true;
        var checkedDate = document.editLot.availableTiming.value;
        var errDate = document.getElementById("errDate");
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
        return valid;
    }
</script>
</body>
</html>
