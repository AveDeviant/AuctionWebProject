<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<c:import url="/fragments/header.jsp"/>
<head>
    <title><fmt:message key="offer.lot.title"/></title>
    <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
</head>
<body>
<div class="custom-opacity">
    <div class="container">
        <div class="row">
            <h1 class="text-center"><fmt:message key="offer.lot.page.head"/></h1>
        </div>
        <div class="row">
            <h3 class="text-center"><fmt:message key="offer.lot.page"/></h3>
        </div>
        <c:choose>
            <c:when test="${user eq null}">
                <div class="text-center">
                    <span>
                        <fmt:message key="offer.lot.page.notice"/></span>
                    <a href="${pageContext.request.contextPath}/Auction?command=goTo&page=authorization"><fmt:message
                            key="offer.lot.reference.authorization"/></a>
                </div>
            </c:when>
            <c:otherwise>
                <c:if test="${addErr !=null}">
                    <div class=" alert alert-danger alert-dismissable fade in">
                        <fmt:message key="${addErr}"/>
                    </div>
                </c:if>
                <div class="row">
                    <div class="col-sm-12">
                        <form name="addingLot" action="${pageContext.request.contextPath}/Auction"
                              enctype="multipart/form-data" method="post">
                            <label for="title"><fmt:message key="admin.lot.title"/> </label>
                            <input class="form-control" type="text" name="title" id="title"
                                   title="<fmt:message key="admin.lot.title.restrict"/>" required>
                            <span class="help-block"><fmt:message key="admin.lot.title.restrict"/> </span>
                            <span class="err" id="errTitle" style="display: none"><fmt:message
                                    key="admin.lot.title.restrict"/> </span>
                            <br/>
                            <label for="description"><fmt:message key="admin.lot.description"/> </label>
                            <textarea class="form-control" rows="4" name="description" id="description"
                                      oninput=" return checkLength()"></textarea>
                            <label class="help-block" id="symbolCount"></label><br/>
                            <div style="display:none;" id="descriptErr"
                                 class=" alert alert-danger alert-dismissable fade in">
                                <fmt:message key="description.length.error"/>
                            </div>

                            <label for="image"><fmt:message key="admin.lot.image"/> </label>
                            <input class="form-control" type="file" name="image" id="image" required><br/>
                            <c:if test="${imageErr!=null}">
                                <div class=" alert alert-danger alert-dismissable fade in">
                                    <fmt:message key="${imageErr}"/></div>
                            </c:if>
                            <label for="price"><fmt:message key="admin.lot.startingprice"/> </label>
                            <input class="form-control" type="text" name="price" id="price"
                                   pattern="^[1-9][0-9]*.[0-9]{2}"
                                   required
                                   title="<fmt:message key="bet.restrict"/>"
                                   placeholder="<fmt:message key="bet.restrict"/>">
                            <br/>
                            <label for="availableTiming"><fmt:message key="lot.timing"/> </label>
                            <input class="form-control" type="date" name="availableTiming" id="availableTiming"
                                   required>
                            <span class="help-block"><fmt:message key="lot.timing.note"/> </span>
                            <div style="display:none;" id="errDate"
                                 class=" alert alert-danger alert-dismissable fade in">
                                <fmt:message key="admin.lot.timing.err"/>
                            </div>
                            <br/>
                            <label for="category"><fmt:message key="admin.lot.value"/> </label>
                            <select name="category" id="category">
                                <c:forEach var="option" items="${categories}">
                                    <option value="${option.getValue()}" selected><c:out
                                            value="${option.getValue()}"/></option>
                                </c:forEach>
                            </select> <br/>
                            <button class="button-auction" type="submit" onclick=" return checkInput()"><fmt:message
                                    key="button.add"/></button>
                            <input type="hidden" name="command" value="addLot"/>
                            <input type="hidden" name="jspPath"
                                   value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">
                        </form>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<script>
</script>
</body>
</html>
