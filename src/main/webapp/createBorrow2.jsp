<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Подтверждение новой выдачи книг</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="error"><p>${errorMsg}</p></div>
<div class="container py-4">
    <h3 class="text-center">Подтверждение новой выдачи книг</h3>
    <form id="form" method="post" class="mt-5" action="${pageContext.request.contextPath}/controller?command=create_borrow">
        <p>ID читателя: <input readonly class="form-control bg-light" type="number" min="1" max="1_000_000_000" value="${reader.id}" name="readerId"></p>
        <p>Фамилия: <input readonly class="form-control bg-light" type="text" maxlength="45" name="lastName" value="${reader.lastName}"></p>
        <p>Имя: <input readonly class="form-control bg-light" type="text" maxlength="45" name="firstName" value="${reader.firstName}"></p>
        <p>Дата возврата: <input class="form-control bg-light" id="returnDate" type="date" maxlength="8" name="returnDate" value="${returnDate}"></p>

        <p>Предварительная полная стоимость: <input readonly class="form-control bg-light" type="number" min="0" step="0.01" maxlength="10" name="fullCost" value="${fullCost}"></p>
        <p>Скидка(%): <input readonly class="form-control bg-light" type="number" min="0" step="1" maxlength="3" name="discountPercent" value="${discountPercent}"></p>
        <p>Предварительная стоимость: <input readonly class="form-control bg-light" type="number" min="0" step="0.01" maxlength="10" name="cost" value="${cost}"></p>
        <p>Список выдаваемых экземпляров:</p>
        <div class="table-responsive">
            <table class="table table-bordered table-striped table-hover table-condensed">
                <thead>
                    <tr>
                        <th>ID экземпляра</th>
                        <th>ID книги</th>
                        <th>Название книги</th>
                        <th>Автор(-ы)</th>
                        <th>Цена книги</th>
                        <th>Цена за день</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="bookCopy" items="${requestScope.bookCopies}">
                        <tr>
                            <td>${bookCopy.id}</td>
                            <td>${bookCopy.bookDto.id}</td>
                            <td>${bookCopy.bookDto.titleRu}</td>
                            <td>${bookCopy.bookDto.authors}</td>
                            <td>${bookCopy.bookDto.bookCost}</td>
                            <td>${bookCopy.bookDto.dayCost}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="d-flex justify-content-around mt-5">
            <input type="submit" class="btn btn-success btn-lg" value="Подтвердить выдачу">
            <div class="container text-center mt-5">
                <a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/controller?command=main">Отмена</a>
            </div>
        </div>
    </form>
</div>
</body>
</html>

