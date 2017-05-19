<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="properties.locale"/>
<c:import url="/fragments/header.jsp"/>
<html>
<head>
    <title><fmt:message key="register.page.title"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<c:if test="${user ne null}">
    <c:redirect url="${pageContext.request.contextPath}/Auction"/>
</c:if>
<div class="container">
    <div class="custom-opacity">
        <div class="row">
            <div class="col-sm-12">
                <h1 class="text-center strong text-info"><fmt:message key="register.page.title"/></h1>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-offset-4 col-sm-4">
                <div class="window">
                    <c:if test="${registrationError !=null}">
                        <div class=" alert alert-danger alert-dismissable fade in">
                            <fmt:message key="${registrationError}"/>
                        </div>
                    </c:if>
                    <form name="registrationForm" class="registrationForm"
                          action="${pageContext.request.contextPath}/Controller" method="post">
                        <div class="form-group">
                            <label for="login"><fmt:message key="register.username"/> </label><label
                                class="required">*</label>
                            <input class="form-control" type="text" name="login" id="login" pattern="[\w-_]{6,32}"
                                   required
                                   title="<fmt:message key="login.username.restrict"/>">
                            <span class="help-block"><fmt:message key="login.username.restrict"/> </span>
                        </div>
                        <div class="form-group">
                            <label for="alias"><fmt:message key="registration.alias"/> </label><label
                                class="required">*</label>
                            <input class="form-control" type="text" name="alias" id="alias" pattern="[\dA-Za-z А-Яа-я-,. ]{6,32}"
                                   required title="<fmt:message key="registration.alias.restrict"/>">
                            <span class="help-block"><fmt:message key="registration.alias.restrict"/></span>
                        </div>
                        <div class=" form-group">
                            <label for="password"><fmt:message key="register.password"/> </label><label
                                class="required">*</label>
                            <input class="form-control" type="password" name="password" id="password" required
                                   title="<fmt:message key="login.password.restrict"/>"
                                   pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*[\d])[\w_-]{8,}$">
                            <span class="help-block"><fmt:message key="login.password.restrict"/></span>
                        </div>
                        <div class="form-group">
                            <label for="password2"><fmt:message key="register.password.repeat"/> </label><label
                                class="required">*</label>
                            <input class="form-control" type="password" name="password2" id="password2" required
                                   title="<fmt:message key="login.password.restrict"/>"
                                   pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*[\d])[\w_-]{8,}$">
                            <label class="alert-danger" id="errPwd2" style="display: none">
                                <fmt:message key="registration.passwords.not.equals"/>
                            </label>
                        </div>
                        <div class="form-group">
                            <label for="mail"><fmt:message key="register.email"/></label><label
                                class="required">*</label>
                            <input class="form-control" type="email" name="mail" id="mail" required>
                        </div>
                        <br/>
                        <button class="button-auction" type="submit" onclick="validateForm()"><fmt:message
                                key="register.button.register"/></button>
                        <input type="hidden" name="command" value="registration">
                    </form>
                    <label class="required">*</label><label><fmt:message key="login.notice.necessary"/></label><br/>
                    <a href="${pageContext.request.contextPath}/Auction?command=goTo&page=faq">
                        <fmt:message key="registration.page.rules.notice"/></a>
                </div>
            </div>
        </div>
        <script>
            function validateForm() {
                var result = true;
                var errPwd2 = document.getElementById("errPwd2");
                var pwd = document.registrationForm.password.value;
                var pwd2 = document.registrationForm.password2.value;
                if (pwd !== pwd2) {
                    errPwd2.style.display = "inline";
                    result = false;
                    document.registrationForm.password.value = "";
                    document.registrationForm.password2.value = "";
                }
                return result;
            }
        </script>
    </div>
</div>
</body>
</html>
