<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Сведения о книге</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <c:if test="${book!=null}">
        <h3 class="text-center mb-5">Сведения о книге</h3>
        <div class="line">
            <div class="mt-5">
                <c:choose>
                    <c:when test="${not empty coverPhotos}">
                        <img id="img" class="img-book" src="img/books/${coverPhotos.get(0)}" alt="${book.titleRu}"/>
                    </c:when>
                    <c:otherwise>
                        <img id="img" class="img-book" src="img/books/defaultBookCover.jpg" alt="${book.titleRu}"/>
                    </c:otherwise>
                </c:choose>
                <div class="imgButtons mt-3">
                    <button class="btn btn-outline-primary" id="previousButton"><</button>
                    <button class="btn btn-outline-primary" id="nextButton">></button>
                </div>
            </div>
            <div class="mt-5">
                <h5>Обновление обложки книги:</h5>
                <form method="post"
                      action="${pageContext.request.contextPath}/upload?command=coverPhoto&bookId=${book.id}"
                      enctype="multipart/form-data">
                    <div>
                        <input type="file" class="btn" name="coverPhoto" required accept="image/jpg, image/png, image/jpeg">
                        <input type="submit" class="btn btn-primary" value="Добавить обложку книги">
                    </div>
                </form>
            </div>
        </div>
        <div class="error"><p>${errorMsg}</p></div>
        <datalist id="photos" class="mt-3">
            <c:forEach var="photo" items="${coverPhotos}">
                <option value="${photo}">${photo}</option>
            </c:forEach>
        </datalist>
        <form id="form" method="post" class="mt-5">
            <h5>Основная информация:</h5>

            <p>ID: <input readonly class="form-control bg-light" type="number" min="1" max="1_000_000_000" value="${book.id}" name="bookId"></p>
            <p>Название: <input readonly class="form-control bg-light" value="${book.titleRu}" name="titleRu"></p>
            <p>Оригинальное название: <input readonly class="form-control bg-light" value="${book.titleOrigin}" name="titleOrigin"></p>
            <p>Автор<c:if test="${book.authors.size()>1}">ы</c:if>:
                <input readonly class="form-control bg-light" value="${authors}" name="authors">
            </p>
            <p>Жанр<c:if test="${book.genres.size()>1}">ы</c:if>:
                <input readonly class="form-control bg-light" value="${genres}" name="genres">
            </p>
            <p>Описание: <input readonly class="form-control bg-light" value="${book.description}" name="description"></p>
            <p>Цена книги: <input readonly class="form-control bg-light" type="number" value="${book.bookCost}" name="bookCost"></p>
            <p>Цена за день пользования: <input readonly class="form-control bg-light" type="number" value="${book.dayCost}" name="dayCost"></p>

            <p>Год издания: <input readonly class="form-control bg-light" type="number" value="${book.editionYear}" name="editionYear"></p>
            <p>Количество страниц: <input readonly class="form-control bg-light" type="number" min="1" max="10000" value="${book.pageCount}"
                                          name="pageCount"></p>
            <p>Дата регистрации: <input readonly class="form-control bg-light" type="date" value="${book.registrationDate}"
                                        name="registrationDate"></p>
            <p>Общее количество экземпляров: <input readonly class="form-control bg-light" type="number" min="1" max="1000" name="totalCopyCount" value="${totalCopyCount}"></p>
            <p>Количество доступных экземпляров: <input readonly class="form-control bg-light" type="number" min="1" max="1000" name="availableCopyCount" value="${availableCopyCount}"></p>
        </form>
        <div class="container text-center">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/controller?command=main">Вернуться на главную страницу</a>
        </div>
    </c:if>
</div>
</body>
<script src="js/bookPage.js"></script>
</html>
