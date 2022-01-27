<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${requestScope.pageNumber==null}">
    <c:set var="pageNumber" value="1" scope="request"/>
</c:if>
<html>
<head>
    <title>Pagination</title>
</head>
<body>
<div class="isolated">
    <div class="blog-pagination">
        <c:if test="${pageNumber > 1}">
            <c:if test="${pageNumber > 2}">
                <a class="btn btn-outline-primary"
                   href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=${sortingColumn}&reversedOrder=${reversedOrder}&pageNumber=1"> 1 </a>
                <c:if test="${pageNumber > 3}">
                    ...
                </c:if>
            </c:if>
            <a class="btn btn-outline-primary"
               href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=${sortingColumn}&reversedOrder=${reversedOrder}&pageNumber=${pageNumber-1}"> ${pageNumber-1} </a>
        </c:if>
        <div class="btn btn-outline-secondary disabled">${pageNumber}</div>
        <c:if test="${countOfPages > pageNumber}">
            <a class="btn btn-outline-primary"
               href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=${sortingColumn}&reversedOrder=${reversedOrder}&pageNumber=${pageNumber+1}"> ${pageNumber+1} </a>
            <c:if test="${countOfPages > pageNumber + 1}">
                <c:if test="${countOfPages > pageNumber + 2}">
                    ...
                </c:if>
                <a class="btn btn-outline-primary"
                   href="${pageContext.request.contextPath}/controller?command=readers&sortingColumn=${sortingColumn}&reversedOrder=${reversedOrder}&pageNumber=${countOfPages}"> ${countOfPages} </a>
            </c:if>
        </c:if>
    </div>
</div>
</body>
</html>
