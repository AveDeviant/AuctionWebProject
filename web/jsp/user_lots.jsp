<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="admin.lot.edit"/></title>
</head>
<c:import url="${pageContext.request.contextPath}/fragments/header.jsp"/>
<body>
<c:if test="${user eq null}">
    <c:redirect url="${pageContext.request.contextPath}/Auction"/>
</c:if>
<div class="custom-opacity">
    <c:if test="${extendErr ne null}">
        <div class=" alert alert-danger alert-dismissable fade in">
            <fmt:message key="${extendErr}"/>
        </div>
    </c:if>
    <div class="container">
        <div class="col-sm-12">
            <div class="row">
                <h4><fmt:message key="user.lot.page.approved"/></h4>
                <c:choose>
                    <c:when test="${lots.isEmpty()}">
                        <h1><fmt:message key="lots.empty"/></h1>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                            <tr>
                                <th><fmt:message key="lot.edit.table.lot.title"/></th>
                                <th><fmt:message key="lot.edit.table.lot.price"/></th>
                                <th><fmt:message key="lot.edit.table.lot.current"/></th>
                                <th><fmt:message key="lot.edit.table.lot.date"/></th>
                                <th><fmt:message key="lot.edit.table.lot.availability"/></th>
                                <th><fmt:message key="extend.period.title"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="lot" items="${lots}">
                                <tr>
                                    <td><c:out value="${lot.getTitle()}"/></td>
                                    <td><c:out value="${lot.getPrice()}"/></td>
                                    <td><c:out value="${lot.getCurrentPrice()}"/></td>
                                    <td><c:out value="${lot.getDateAvailable()}"/></td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/Auction">
                                            <button class="button-auction" type="submit"><fmt:message
                                                    key="admin.edit.lot.withdraw"/></button>
                                            <input type="hidden" name="command" value="lotStatus">
                                            <input type="hidden" name="id" value="${lot.getId()}">
                                            <input type="hidden" name="status" value="false">
                                            <input type="hidden" name="jspPath"
                                                   value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                                        </form>
                                    </td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/Auction">
                                            <select name="period">
                                                <option value="7">7</option>
                                                <option value="15">15</option>
                                            </select>
                                            <button class="button-auction" type="submit"><fmt:message
                                                    key="extend.button"/></button>
                                            <input type="hidden" name="command" value="extendPeriod">
                                            <input type="hidden" name="lotId" value="${lot.getId()}">
                                            <input type="hidden" name="jspPath"
                                                   value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>
