<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Добавление новой книги</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="error"><p>${errorMsg}</p></div>
<div class="container py-4">
    <h3 class="text-center">Добавление новой книги</h3>
    <form id="form" method="post" class="mt-5" action="${pageContext.request.contextPath}/controller?command=create_book">
        <p>Название: <input class="form-control bg-light" required type="text" maxlength="95" name="titleRu"></p>
        <p>Оригинальное название: <input class="form-control bg-light" type="text" maxlength="95" name="titleOrigin"></p>
        <p>Авторы:
            <select class="form-control bg-light" name="bookAuthors" id="bookAuthors" required multiple>
                <c:forEach var="author" items="${requestScope.authors}">
                    <option value="${author.id}">${author.name} ${author.surname}</option>
                </c:forEach>
            </select>
        </p>
        <p>Жанры:
            <select class="form-control bg-light" name="bookGenres" id="bookGenres" required multiple>
                <c:forEach var="genre" items="${requestScope.genres}">
                    <option value="${genre.id}">${genre.genreName}</option>
                </c:forEach>
            </select>
        </p>
        <p>Описание: <input class="form-control bg-light" type="text" maxlength="145" name="description"></p>
        <p>Цена книги: <input class="form-control bg-light" required type="number" min="0" step="0.01" maxlength="10" name="bookCost"></p>
        <p>Цена за день пользования: <input class="form-control bg-light" required type="number" min="0" step="0.01" maxlength="10" name="dayCost"></p>
        <p>Год издания: <input class="form-control bg-light" type="number" min="1900" max="2100" maxlength="4" name="editionYear"></p>
        <p>Количество страниц: <input class="form-control bg-light" type="number" min="1" max="10000" maxlength="5" name="pageCount"></p>
        <p>Дата регистрации: <input class="form-control bg-light" id="registrationDate" type="date" maxlength="8" name="registrationDate"></p>
        <p>Количество экземпляров: <input class="form-control bg-light" type="number" min="1" max="100" maxlength="3" name="copyCount"></p>
        <div class="d-flex justify-content-around mt-5">
            <input type="submit" class="btn btn-success btn-lg" value="Добавить книгу">
            <input type="reset" class="btn btn-secondary btn-lg" value="Сбросить">
        </div>
    </form>
    <div class="container text-center mt-5">
        <a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/controller?command=main">Вернуться на главную страницу</a>
    </div>
</div>
</body>
<script src="js/createBook.js"></script>
</html>
