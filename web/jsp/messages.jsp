<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 07.04.2017
  Time: 19:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="message.page.title"/></title>
</head>
<body>
<c:import url="/fragments/header.jsp"/>
<div class="container">
    <div class="custom-opacity">
        <c:if test="${user eq null}">
            <c:redirect url="/Controller"/>
        </c:if>
        <jsp:include page="/Controller">
            <jsp:param name="command" value="getMessages"/>
        </jsp:include>
        <c:forEach var="message" items="${user.getUserMessages()}">
            <span><c:out value="${message.getDateTime()}"/>
                <strong><c:out value="${message.getSenderUsername()}"/></strong></span>
            <p><c:out value="${message.getTheme()}"/></p><br/>
            <p><c:out value="${message.getContent()}"/></p>
            <c:if test="${user.getRole().getValue() eq 'admin' and message.getSenderId()!=user.getUserId()}">
                <a href="#" onclick="showMessageForm(${message.getSenderId()})">
                    <fmt:message key="admin.message.button"/></a>
                <form id=${message.getSenderId()} method="post" action="${pageContext.request.contextPath}/Auction"
                      style="display:none;">
                    <br/>
                    <label for="theme"><fmt:message key="faq.form.theme"/></label>
                    <input class="form-control" type="text" name="theme" id="theme">
                    <label for="content"><fmt:message key="faq.form.message"/> </label>
                    <input class="form-control" type="text" name="content" id="content">
                    <button type="submit" class="button-auction"><fmt:message key="faq.form.send"/></button>
                    <input type="hidden" name="recipientId" value="${message.getSenderId()}">
                    <input type="hidden" name="command" value="message">
                    <input type="hidden" name="jspPath"
                           value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}"/>
                </form>
            </c:if>
            <br/>
        </c:forEach>

        <a href="${pageContext.request.contextPath}/Auction?command=goTo&page=faq"><fmt:message
                key="messages.page.notice"/></a>
    </div>
</div>
<script>
    function showMessageForm(a) {
        var form = document.getElementById(a);
        if (form.style.display === "none") {
            form.style.display = "inline";
        } else {
            form.style.display = "none";
        }
    }
</script>
</body>
</html>
