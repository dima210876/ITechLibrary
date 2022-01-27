<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Сведения о читателе</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="error"><p>${errorMsg}</p></div>
<div class="container py-4">
    <c:if test="${reader!=null}">
        <h3 class="text-center">Сведения о читателе</h3>
        <form id="form" method="post" class="mt-5">
            <p>ID: <input readonly class="form-control bg-light" type="number" min="1" max="1_000_000_000" value="${reader.id}" name="readerId"></p>
            <p>Фамилия: <input readonly class="form-control bg-light" value="${reader.lastName}" name="lastName"></p>
            <p>Имя: <input readonly class="form-control bg-light" value="${reader.firstName}" name="firstName"></p>
            <p>Отчество: <input readonly class="form-control bg-light" value="${reader.middleName}" name="middleName"></p>
            <p>Номер пасспорта: <input readonly class="form-control bg-light" value="${reader.passportNumber}" name="passportNumber"></p>
            <p>Дата рождения: <input readonly class="form-control bg-light" type="date" value="${reader.birthDate}" name="birthDate"></p>
            <p>Электронная почта: <input readonly class="form-control bg-light" type="email" value="${reader.email}" name="email"></p>
            <p>Адрес: <input readonly class="form-control bg-light" value="${reader.address}" name="address"></p>
        </form>
        <c:choose>
            <c:when test="${not empty notReturnedBorrow}">
                <p class="text-danger mt-5">У читателя есть невозвращённый займ c ID = ${notReturnedBorrow.id}.</p>
                <div class="container">
                    <a class="btn btn-warning mt-3"
                       href="${pageContext.request.contextPath}/controller?command=return_borrow&borrowId=${notReturnedBorrow.id}">
                        Возврат книг
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-success mt-5">У читателя нет задолженностей перед библиотекой.</p>
                <a class="btn btn-success mt-3"
                   href="${pageContext.request.contextPath}/controller?command=create_borrow_order&readerId=${reader.id}">
                    Выдача книг
                </a>
            </c:otherwise>
        </c:choose>
        <div class="container text-center mt-5">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/controller?command=main">Вернуться на главную страницу</a>
        </div>
    </c:if>
</div>
</body>
</html>
