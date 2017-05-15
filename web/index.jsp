<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <jsp:include page="${pageContext.request.contextPath}/Auction">
    <jsp:param name="command" value=""/>
  </jsp:include>
  <%--<c:redirect url="/Controller"/>--%>
  </body>
</html>