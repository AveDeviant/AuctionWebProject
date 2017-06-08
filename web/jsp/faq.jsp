<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<head>
    <title><fmt:message key="general.rules.title"/> </title>
</head>
<body>
<c:import url="/fragments/header.jsp"/>
<div class="custom-opacity">
<div class="container">

        <div class="row">
            <div class="col-sm-1">
                <h2>1</h2>
            </div>
            <div class="col-sm-11">
                <h3><fmt:message key="general.rules.title"/> </h3>
                <h6><fmt:message key="general.rules.responsibility"/> </h6>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <h2>2</h2>
            </div>
            <div class="col-sm-11">
                <h3><fmt:message key="general.rules.bids.title"/> </h3>
                <h6><fmt:message key="general.rules.bids.1"/> </h6>
                <h6>
                  <fmt:message key="general.rules.bids.2"/>
                </h6>
                <h6><fmt:message key="general.rules.reject.title"/> </h6>
                <h6>
                 <fmt:message key="general.rules.reject.1"/>
                </h6>

            </div>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <h2>3</h2>
            </div>
            <div class="col-sm-11">
                <h3><fmt:message key="general.rules.bidders.title"/> </h3>

                <h6><fmt:message key="general.rules.bidders.1"/> </h6>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <h2>4</h2>
            </div>
            <div class="col-sm-11">
                <h3><fmt:message key="general.rules.traders.title"/> </h3>
                <h6><fmt:message key="general.rules.traders.1"/></h6>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-11">
                <h3><fmt:message key="faq.feedback"/> </h3>
            </div>
        </div>
        <c:if test="${messageErr!=null}">
            <div class=" alert alert-danger alert-dismissable fade in">
                <fmt:message key="${messageErr}"/>
            </div>
        </c:if>
        <c:if test="${user eq null or user.getRole().getValue() eq 'customer'}">
            <form method="post" action="${pageContext.request.contextPath}/Auction">
                <label for="theme"><fmt:message key="faq.form.theme"/> </label>
                <input class="form-control" type="text" name="theme" id="theme">
                <label for="content"><fmt:message key="faq.form.message"/></label><label class="required">*</label>
                <textarea type="text" name="content" id="content" required rows="5" class="form-control"></textarea>
                <br/>
                <button class="button-auction" type="submit" name="command" value="message"><fmt:message
                        key="faq.form.send"/></button>
                <input type="hidden" name="jspPath"
                       value="${pageContext.request.requestURI.concat("?").concat(pageContext.request.queryString)}">

            </form>
            <label class="required">*</label><label><fmt:message key="login.notice.necessary"/></label>
        </c:if>
    </div>
</div>
</body>
</html>
