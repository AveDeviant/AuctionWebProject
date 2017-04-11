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
    <c:if test="${user eq null}">
        <c:redirect url="/Controller"/>
    </c:if>
    <jsp:include page="/Controller">
        <jsp:param name="command" value="getMessages"/>
    </jsp:include>
    <c:forEach var="message" items="${user.getUserMessages()}">
        <c:out value="${message.getSenderId()}"/>
        <c:out value="${message.getSenderUsername()}"/>
        <c:out value="${message.getContent()}"/>
        <c:if test="${user.getRole().getValue() eq 'admin' and message.getSenderId()!=user.getUserId()}">
            <button type="button" onclick="showMessageForm()">Ответ</button>
            <form id="message-form" method="post" action="${pageContext.request.contextPath}/Controller"
                  style="display:none;">
                <label>Theme</label>
                <input type="text" name="theme">
                <label>Content</label>
                <input type="text" name="content">
                <button type="submit">Send</button>
                <input type="hidden" name="recipientId" value="${message.getSenderId()}">
                <input type="hidden" name="command" value="message">
                <input type="hidden" name="jspPath"
                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}"/>
            </form>
        </c:if>
        <br/>
    </c:forEach>

    <label>Остались неразрешенные вопросы? Задайте их здесь</label>
</div>
<script>
    function showMessageForm() {
        var form = document.getElementById("message-form");
        if (form.style.display === "none") {
            form.style.display = "inline";
        } else {
            form.style.display = "none";
        }
    }
</script>
</body>
</html>
