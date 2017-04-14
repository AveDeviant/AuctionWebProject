<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/darkstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <link href="https://fonts.googleapis.com/css?family=PT+Serif" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Jura" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
    <fmt:setLocale value="${locale}"/>
    <fmt:setBundle basename="properties.locale"/>
</head>
<body>
<nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Menu</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand"
               href="${pageContext.request.contextPath}/Controller?command=goTo&page=index">Auction</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=faq">How to?</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false"><fmt:message key="header.menu.categories.title"/> <span
                            class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <jsp:include page="/Controller">
                            <jsp:param name="command" value="getCategories"/>
                        </jsp:include>
                        <c:forEach var="category" items="${categories}" varStatus="status">
                            <li>
                                <a href="${pageContext.request.contextPath}/Controller?command=lotsByCategory&categoryValue=${category.getValue()}">
                                    <c:out value="${category.getValue()}"/></a></li>
                        </c:forEach>
                    </ul>
                </li>
                <li><a href="/jsp/info.jsp"><fmt:message key="header.menu.info"/> </a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <c:if test="${user!=null && (user.isUnreadMessages())}">
                    <li><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=message"><img
                            style="width: 23px;height: 23px;"
                            src="${pageContext.request.contextPath}/css/icons/mail.png"></a>
                    </li>
                </c:if>
                <c:if test="${user !=null && (!user.getWinningBets().isEmpty())}">
                    <li><a class="event-winner"
                           href="${pageContext.request.contextPath}/Controller?command=order">
                            ${user.getWinningBets().size()}</a>
                    </li>
                </c:if>
                <li><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=offer"><fmt:message
                        key="header.offer"/> </a></li>
                <c:choose>
                    <c:when test="${user.getUserName() eq null}">
                        <li>
                            <a href="${pageContext.request.contextPath}/Controller?command=goTo&page=authorization"><fmt:message
                                    key="header.login"/></a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${user.getRole().getValue() eq 'customer'}">
                                <li><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=private">
                                    <c:out value="${user.getUserName()}"/>
                                </a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${pageContext.request.contextPath}/Controller?command=goTo&page=admin">
                                    <c:out value="${user.getUserName()}"/>
                                </a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false"><fmt:message key="header.locale"/> <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li>
                            <form action="/Controller" method="post">
                                <button class="local-spec" type="submit" name="russian">Русский</button>
                                <input type="hidden" name="command" value="locale">
                                <input type="hidden" name="lang" value="default">
                                <input type="hidden" name="jspPath"
                                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            </form>
                        </li>
                        <li>
                            <form action="/Controller" method="post">
                                <button class="local-spec" type="submit" name="english">English</button>
                                <input type="hidden" name="command" value="locale">
                                <input type="hidden" name="lang" value="en_US">
                                <input type="hidden" name="jspPath"
                                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                            </form>
                        </li>
                    </ul>
                </li>
            </ul>
            </ul>
        </div>
    </div>
</nav>
</body>
</html>