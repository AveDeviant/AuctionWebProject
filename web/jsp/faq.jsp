<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 31.03.2017
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title>FAQ</title>
</head>
<body>
<c:import url="/fragments/header.jsp"/>
<div class="container">
    <label class="alert-danger">
    <c:if test="${messageErr!=null}">
        <div class=" alert alert-danger alert-dismissable fade in">
        <fmt:message key="${messageErr}"/>
        </div>
    </c:if>
    </label>
    <c:if test="${user eq null or user.getRole().getValue() eq 'customer'}">
    <form method="post" action="${pageContext.request.contextPath}/Controller">
        <label for="theme"><fmt:message key="faq.form.theme"/> </label>
        <input class="form-control" type="text" name="theme" id="theme">
        <label for="content"><fmt:message key="faq.form.message"/></label><label class="required">*</label>
        <textarea type="text" name="content" id="content" required rows="5" class="form-control"></textarea>
        <br/>
        <button class="button-auction" type="submit" name="command" value="message"><fmt:message key="faq.form.send"/> </button>
        <input type="hidden" name="jspPath"
               value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">

    </form>
        <label class="required">*</label><fmt:message key="login.notice.necessary"/>
    </c:if>
</div>
</body>
</html>
