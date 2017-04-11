<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 30.03.2017
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<head>
    <title><fmt:message key="admin.title"/></title>
</head>
<body>
<c:import url="/fragments/header.jsp"/>

<div class="container">
    <c:if test="${err!=null}">
        <fmt:message key="${err}"/>
    </c:if>
    <table class="table">
        <thead>
        <tr>
            <th>ID</th>
            <th>ID lot</th>
            <th><fmt:message key="order.title.customer.id"/></th>
            <th><fmt:message key="order.title.customer.name"/></th>
            <th><fmt:message key="order.title.customer.city"/></th>
            <th><fmt:message key="order.title.customer.address"/></th>
            <th><fmt:message key="order.title.customer.payment"/></th>
            <th><fmt:message key="order.title.customer.accept"/></th>
            <th><fmt:message key="order.title.customer.date"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td><c:out value="${order.getOrderId()}"/></td>
                <td><c:out value="${order.getLotId()}"/></td>
                <td><c:out value="${order.getUserId()}"/></td>
                <td><c:out value="${order.getCostumerName()}"/></td>
                <td><c:out value="${order.getCostumerCity()}"/></td>
                <td><c:out value="${order.getCostumerAddress()}"/></td>
                <td><c:out value="${order.getPayment()}"/></td>
                <td><c:out value="${order.getAccept()}"/></td>
                <td><c:out value="${order.getDateTime()}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
