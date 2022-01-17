<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="">
    <h2>Error code: 500</h2>
    <br/>
        Exception: ${pageContext.exception}
    <br/>
    <br/>
        Message from exception: ${pageContext.exception.message}
</div>
</body>
</html>
