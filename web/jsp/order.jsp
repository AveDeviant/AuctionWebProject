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

    <div class="row">
        <div class="col-sm-12">
            <c:if test="${orderError!=null}">
                <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="${orderError}"/>
                </div>
            </c:if>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <label class="text-center"><fmt:message key="order.reject.notice"/></label>
        </div>
    </div>
    <div class="row">
        <h1 class="text-center"><fmt:message key="order.title.greeting"/></h1>
    </div>
    <h2 class="text-center">${lot.getTitle()}</h2>
    <div class="row">
        <div class="col-sm-8">
            <img class="img-responsive" src="${lot.getImage()}">
        </div>
        <div class="col-sm-4">
            <h4><fmt:message key="order.page.cost"/></h4>
            <h3>${lot.getCurrentPrice()}</h3>

            <form action="${pageContext.request.contextPath}/Controller" method="post" enctype="multipart/form-data">
                <label for="name"><fmt:message key="order.page.name"/></label><label
                    class="required">*</label>
                <input class="form-control" type="text" required pattern="[A-Za-z А-Яа-я ]{2,}" name="name" id="name">
                <label for="city"><fmt:message key="order.page.city"/></label><label class="required">*</label>
                <input class="form-control" type="text" required pattern="[\w,-.А-Яа-я ]{5,}" name="city" id="city">
                <label for="address"><fmt:message key="order.page.address"/></label><label class="required">*</label>
                <input class="form-control" type="text" required pattern="[\w\d,-.А-Яа-я ]{5,}" name="address"
                       id="address">
                <label for="phone"><fmt:message key="order.page.phone"/></label><label class="required">*</label>
                <input class="form-control" type="text" required name="phone" id="phone" pattern="[\d+()-]{3,18}"><br/>
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
            <label class="required">*</label><label><fmt:message key="login.notice.unnecessary"/> </label>
        </div>
    </div>

</div>
</body>
</html>
