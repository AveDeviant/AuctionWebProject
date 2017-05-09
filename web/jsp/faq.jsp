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
<div class="custom-opacity">
<div class="container">

        <div class="row">
            <div class="col-sm-1">
                <h2>1</h2>
            </div>
            <div class="col-sm-11">
                <h3>Общие правила</h3>
                <h6>Администрация аукционной площадки "Auction house" (далее Аукцион) не несет ответственности за
                    результаты
                    проведения торгов в случае, если Аукцион не является одной из сторон, принимающей участие в сделке.
                    Регистрируясь на сайте Аукциона вы подтвержаете, что являетесь лицом, достигшим совершеннолетия и
                    полностью отдаете отчет своим действиям.</h6>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <h2>2</h2>
            </div>
            <div class="col-sm-11">
                <h3>Правила проведения торгов</h3>
                <h6>Торги проводятся по правилам прямого английского аукциона. Продавец назначает отправную цену и дату,
                    до
                    которой продолжается аукционная борьба.
                    Участники делают ставки, указывая цену БОЛЬШЕ текущей цены на лот. На момент окончания аукционной
                    борьбы
                    выигрывает тот участник, чья цена является последней (наивысшей).
                    Аукционная борьба закачнивается в 00 часов 00 минут дня, указанного на странице лота. В случае
                    выигрыша
                    участник в течении 10 дней после окончания торгов должен либо принять сделку, либо отказатться от
                    нее. В
                    случае совершения сделки:</h6>
                <h6>
                    - если собственником лота является Аукцион, с банковского счета победителя снимается сумма, равная
                    конечной стоимости лота (Аукцион берет на себя все обязательства предоставить лот победителю
                    торгов);</h6>
                <h6>
                    - если собственником лота является клиент-продавец (далее Продавец), Продавцу приходит уведомление о
                    результатах аукциона, содержащее контактную информацию победителя,
                    наименование лота и его стоимость.
                </h6>
                <h6>В случае отказа от выигрыша:</h6>
                <h6>
                    - все результаты торгов по лоту аннулируются и конечная цена лота возвращается к начальному
                    значению. Лот
                    возвращается на торги на срок в 10 дней от дня отклонения сделки.
                </h6>

            </div>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <h2>3</h2>
            </div>
            <div class="col-sm-11">
                <h3>Участникам торгов</h3>
                <h6>Для участия в торгах участникам торгов (далее Клиент) необходимо зарегистрировать свою банковскую
                    карту
                    - это необходимо для подтверждения платежеспособности Клиента.
                    Данные о карте Клиента будут доступны исключительно администрации Аукциона.</h6>
                <h6>В случае многократного отклонения сделок Клиентом и в других случаях, нарушающих принцип проведения
                    торгов, администрация Аукциона вправе ограничить доступ Клиента к функционалу
                    Аукциона (операции ставок, оформление сделки, выставление лота на торги).</h6>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-1">
                <h2>4</h2>
            </div>
            <div class="col-sm-11">
                <h3>Продавцам</h3>
                <h6>Лот, который предложил Продавец, будет допущен к торгам если указанные наименование, фото и описание
                    лота
                    полностью раскрывают его состояние, свойства и предназначение. Торги по лоту будут окончены в 00
                    часов 00 минут дня, который
                    был указан Продавцом при выставлении лота.</h6>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-11">
                <h3>Обратная связь</h3>
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
