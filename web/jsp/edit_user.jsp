<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 28.03.2017
  Time: 14:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<head>
    <title><fmt:message key="admin.title"/></title>
</head>
<c:import url="/fragments/header.jsp"/>
<body>
<div class="container">
    <div class="custom-opacity">
    <c:if test="${user eq null or user.getRole().getValue()!='admin'}">
        <c:redirect url="/Controller"/>
    </c:if>
    <jsp:include page="${pageContext.request.contextPath}/Auction">
        <jsp:param name="command" value="getUsers"/>
    </jsp:include>
    <c:if test="${editErr !=null}">
        <div class=" alert alert-danger alert-dismissable fade in">
            <fmt:message key="${editErr}"/>
        </div>
    </c:if>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th><fmt:message key="admin.user.edit.availability"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="customer" items="${customers}">
            <tr>
                <td><c:out value="${customer.getUserId()}"/></td>
                <td><c:out value="${customer.getAlias()}"/></td>
                <td>
                    <c:choose>
                        <c:when test="${customer.getAccess() eq true}">
                            <input type="checkbox" name="access" value="true" checked/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="access" value="true"/>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${customer.getAccess() eq true}">
                            <form action="${pageContext.request.contextPath}/Auction" method="post">
                                <button type="submit" class="button-auction"><fmt:message
                                        key="admin.user.edit.ban"/></button>
                                <input type="hidden" name="id" value="${customer.getUserId()}">
                                <input type="hidden" name="command" value="editAccess">
                                <input type="hidden" name="state" value="banned">
                                <input type="hidden" name="jspPath"
                                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/Auction" method="post">
                                <button type="submit" class="button-auction"><fmt:message
                                        key="admin.user.edit.unban"/></button>
                                <input type="hidden" name="id" value="${customer.getUserId()}">
                                <input type="hidden" name="command" value="editAccess">
                                <input type="hidden" name="state" value="unbanned">
                                <input type="hidden" name="jspPath"
                                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            </form>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
</body>
</html>
