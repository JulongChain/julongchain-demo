<%--
  Created by IntelliJ IDEA.
  User: Benjamin
  Date: 2018/7/18
  Time: 11:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div style="margin-left:43%;margin-right:40%;margin-top:100px">
    余额不足
    信息：${massage.value}<br><br>
    <form method="get" action="/sc/jump">
        <button type="submit" name="type" value="query">查询余额</button>
    </form>
</div>
</body>
</html>
