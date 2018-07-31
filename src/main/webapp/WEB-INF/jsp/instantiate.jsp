<%--
  Created by IntelliJ IDEA.
  User: Benjamin
  Date: 2018/7/6
  Time: 15:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>instantiate</title>
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div style="margin-left:40%;margin-right:40%;margin-top:100px">

    <form action="..." style="margin-right: inherit">
        用户名 ：<input type="text" name="transfer"><br><br>
        金额  ：<input type="text" name="payee"><br><br>
        用户名 ：<input type="text" name="transfer"><br><br>
        金额  ：<input type="text" name="money"><br><br>
        <div style="margin-left: 36%">
            <button type="submit" class="btn btn-primary active"  >
                move
            </button>
        </div>

    </form>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
