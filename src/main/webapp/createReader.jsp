<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Регистрация нового читателя</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="error"><p>${errorMsg}</p></div>
<div class="container py-4">
    <h3 class="text-center">Регистрация нового читателя</h3>
    <form id="form" method="post" class="mt-5" action="${pageContext.request.contextPath}/controller?command=create_reader">
        <p>Фамилия: <input class="form-control bg-light" required type="text" maxlength="45" name="lastName"></p>
        <p>Имя: <input class="form-control bg-light" required type="text" maxlength="45" name="firstName"></p>
        <p>Отчество: <input class="form-control bg-light" type="text" maxlength="45" name="middleName"></p>
        <p>Номер паспорта: <input class="form-control bg-light" maxlength="35" name="passportNumber"></p>
        <p>Дата рождения: <input class="form-control bg-light" required type="date" min="1900-01-01" max="2100-01-01" maxlength="8" name="birthDate"></p>
        <p>Электронная почта: <input class="form-control bg-light" required type="email" maxlength="45" name="email"></p>
        <p>Адрес: <input class="form-control bg-light" maxlength="195" name="address"></p>
        <div class="d-flex justify-content-around mt-5">
            <input type="submit" class="btn btn-success btn-lg" value="Зарегистрировать">
            <input type="reset" class="btn btn-secondary btn-lg" value="Сбросить">
        </div>
    </form>
    <div class="container text-center mt-5">
        <a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/controller?command=main">Вернуться на главную страницу</a>
    </div>
</div>
</body>
</html>
