<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户列表</title>
<style type="text/css">
    td{
        text-align: center;
        color: coral;
    }
</style>
</head>
<body>
    <h1 align="center" style="color: aquamarine"> 用户列表 </h1>
    <table align="center" border="1px" width="60%">
        <tr>
            <th>userId</th>
            <th>username</th>
            <th>password</th>
        </tr>
        <c:forEach items="${requestScope.users}" var="user">
            <tr>
                <td>${user.userId}</td>
                <td>${user.userName}</td>
                <td>${user.userPass}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
