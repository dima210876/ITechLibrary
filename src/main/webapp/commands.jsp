<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <div class="collapse navbar-collapse">
                <div class="navbar-nav me-auto mb-2 mb-lg-0">
                    <div class="nav-item">
                        <c:choose>
                            <c:when test="${command!='default' && command!='main'}">
                                <a class="nav-link" href=${pageContext.request.contextPath}/controller?command=main>
                                    <img class="logo" src="${pageContext.request.contextPath}/img/logo/logo.png"
                                         alt="Главная страница" title="Главная страница"/>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="nav-link active">
                                    <img class="logo" src="${pageContext.request.contextPath}/img/logo/logo.png"
                                         alt="Главная страница" title="Главная страница"/>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="nav-item">
                        <c:choose>
                            <c:when test="${command!='readers'}">
                                <a class="nav-link"
                                   href=${pageContext.request.contextPath}/controller?command=readers>Список читателей</a>
                            </c:when>
                            <c:otherwise>
                                <div class="nav-link active">Список читателей</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="nav-item">
                        <c:choose>
                            <c:when test="${command!='createBorrow'}">
                                <a class="nav-link"
                                   href=${pageContext.request.contextPath}/controller?command=createBorrow>Выдача книг</a>
                            </c:when>
                            <c:otherwise>
                                <div class="nav-link active">Выдача книг</div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="nav-item">
                        <c:choose>
                            <c:when test="${command!='returnBorrow'}">
                                <a class="nav-link"
                                   href=${pageContext.request.contextPath}/controller?command=returnBorrow>Возврат книг</a>
                            </c:when>
                            <c:otherwise>
                                <div class="nav-link active">Возврат книг</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</body>
</html>
