<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Создание новой выдачи книг</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="error"><p>${errorMsg}</p></div>
<div class="container py-4">
    <h3 class="text-center">Создание новой выдачи книг</h3>
    <form id="form" method="post" class="mt-5" action="${pageContext.request.contextPath}/controller?command=confirm_borrow_order">
        <p>ID читателя: <input readonly class="form-control bg-light" type="number" min="1" max="1_000_000_000" value="${reader.id}" name="readerId"></p>
        <p>Фамилия: <input readonly class="form-control bg-light" type="text" maxlength="45" name="lastName" value="${reader.lastName}"></p>
        <p>Имя: <input readonly class="form-control bg-light" type="text" maxlength="45" name="firstName" value="${reader.firstName}"></p>
        <p>Дата возврата: <input class="form-control bg-light" id="returnDate" type="date" maxlength="8" name="returnDate"></p>
        <p>Выберите книги (не более 5):
            <select class="form-control bg-light" name="selectedBooksIds" id="selectedBooksIds" required multiple size="12">
                <c:forEach var="availableBook" items="${requestScope.availableBooks}">
                    <option value="${availableBook.id}">${availableBook.titleRu}</option>
                </c:forEach>
            </select>
        </p>
        <div class="d-flex justify-content-around mt-5">
            <input type="submit" class="btn btn-success btn-lg" value="Продолжить">
        </div>
    </form>
    <div class="container text-center mt-5">
        <a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/controller?command=main">Вернуться на главную страницу</a>
    </div>
</div>
</body>
<script src="js/createBorrow.js"></script>
</html>
