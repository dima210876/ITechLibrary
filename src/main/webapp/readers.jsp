<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список читателей</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<jsp:include page="commands.jsp"/>
<div class="container py-4">
    <div class="table-responsive">
        <table class="table table-bordered table-striped table-hover table-condensed">
            <thead>
            <tr>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'last_name'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=last_name&reversedOrder=true">Фамилия</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=last_name&reversedOrder=false">Фамилия</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=last_name&reversedOrder=false">Фамилия</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'first_name'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=first_name&reversedOrder=true">Имя</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=first_name&reversedOrder=false">Имя</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=first_name&reversedOrder=false">Имя</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'birth_date'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=birth_date&reversedOrder=true">Дата рождения</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=birth_date&reversedOrder=false">Дата рождения</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=birth_date&reversedOrder=false">Дата рождения</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'email'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=email&reversedOrder=true">Электронная почта</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=email&reversedOrder=false">Электронная почта</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=email&reversedOrder=false">Электронная почта</a>
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>
                    <c:choose>
                        <c:when test="${requestScope.sortingColumn == 'address'}">
                            <c:choose>
                                <c:when test="${requestScope.reversedOrder == 'false'}">
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=address&reversedOrder=true">Адрес</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=address&reversedOrder=false">Адрес</a>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=address&reversedOrder=false">Адрес</a>
                        </c:otherwise>
                    </c:choose>
                </th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="reader" items="${requestScope.readers}">
                <tr>
                    <td>${reader.lastName}</td>
                    <td>${reader.firstName}</td>
                    <td>${reader.birthDate}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/controller?command=reader&readerId=${reader.id}">${reader.email}</a>
                    </td>
                    <td>${reader.address}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <jsp:include page="pagination.jsp"/>
    <br>
    <a class="btn btn-primary btn-md" href="${pageContext.request.contextPath}/controller?command=reader_creation_form">Добавить нового читателя</a>
</div>
</body>
</html>
