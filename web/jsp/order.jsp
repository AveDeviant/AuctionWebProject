<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 24.03.2017
  Time: 22:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:import url="/fragments/header.jsp"/>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<html>
<head>
    <title><fmt:message key="order.page.title"/></title>
</head>
<body>
<div class="container">

    <c:if test="${orderError!=null}">
        <label class="alert-danger"><fmt:message key="${orderError}"/> </label>
    </c:if>
    <div class="row">
    <label class="text-center"><fmt:message key="order.reject.notice"/></label>
    </div>
    <div class="row">
    <h1 class="text-center"><fmt:message key="order.title.greeting"/></h1>
    </div>
    <h3>${lot.getTitle()}</h3>
    <img src="${lot.getImage()}">
    <h4>${lot.getCurrentPrice()}</h4>
    <form action="/Controller" method="post" enctype="multipart/form-data">
        <fmt:message key="order.page.name"/>
        <input type="text" required pattern="[A-Za-z А-Яа-я ]{2,}" name="name"><label
            class="label-warning">*</label><br/>
        <fmt:message key="order.page.city"/>
        <input type="text" required pattern="[\w,-.А-Яа-я ]{5,}" name="city"><label class="label-warning">*</label><br/>
        <fmt:message key="order.page.address"/>
        <input type="text" required pattern="[\w\d,-.А-Яа-я ]{5,}" name="address"><label class="label-warning">*</label><br/>
        <fmt:message key="order.page.phone"/>
        <input type="text" required name="phone"><label class="label-warning">*</label><br/>
        <button class="button-auction" type="submit" name="command" value="buy"><fmt:message
                key="order.button.buy"/></button>
        <input type="hidden" name="jspPath"
               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
    </form>
    <form method="post" action="${pageContext.request.contextPath}/Controller">
        <button class="button-auction" type="submit" name="command" value="reject"><fmt:message
                key="order.button.reject"/></button>
        <input type="hidden" name="jspPath"
               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
    </form>
    <label class="label-warning">*</label><label><fmt:message key="login.notice.unnecessary"/> </label>

</div>
</body>
</html>
