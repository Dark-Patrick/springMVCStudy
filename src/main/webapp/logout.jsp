<%--
  Created by IntelliJ IDEA.
  User: 深海泰坦
  Date: 2021/7/18
  Time: 18:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    退出系统，从session删除数据
    <%
        session.removeAttribute("name");
    %>

</body>
</html>
