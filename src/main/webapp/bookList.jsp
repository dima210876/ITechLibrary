<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
    <title>Book List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
    <div class="table-responsive">
        <table class="table table-bordered table-striped table-hover table-condensed">
            <thead>
            <tr>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'title_ru'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=title_ru&reversedOrder=true">Название книги</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=title_ru&reversedOrder=false">Название книги</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=title_ru&reversedOrder=false">Название книги</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>Жанры</th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'edition_year'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=edition_year&reversedOrder=true">Год выпуска</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=edition_year&reversedOrder=false">Год выпуска</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=edition_year&reversedOrder=false">Год выпуска</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'book_cost'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=book_cost&reversedOrder=true">Цена книги</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=book_cost&reversedOrder=false">Цена книги</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=book_cost&reversedOrder=false">Цена книги</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'day_cost'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=day_cost&reversedOrder=true">Цена за день</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=day_cost&reversedOrder=false">Цена за день</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=main&sortingColumn=day_cost&reversedOrder=false">Цена за день</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>Всего экземпляров</th>
                <th>Доступно экземпляров</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="book" items="${requestScope.bookList}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=book&bookId=${book.id}">${book.titleRu}</a>
                    </td>
                    <td>
                        <c:set var="str" value=""/>
                        <c:set var="countOfGenres" value="0"/>
                        <c:forEach var="genre" items="${book.genres}">
                            <c:set var="str" value="${str} ${genre.genreName}, "/>
                            <c:set var="countOfGenres" value="${countOfGenres + 1}"/>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${countOfGenres > 1}">${str.substring(0, str.length() - 2)}</c:when>
                            <c:otherwise>${str}</c:otherwise>
                        </c:choose>
                    </td>
                    <td>${book.editionYear}</td>
                    <td>${book.bookCost}</td>
                    <td>${book.dayCost}</td>
                    <td>${book.totalCopyCount}</td>
                    <td>${book.availableCopyCount}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
