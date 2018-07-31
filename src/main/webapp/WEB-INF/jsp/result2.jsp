
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
<div style="margin-left:43%;margin-right:40%;margin-top:20%;color:#fff;">

    转账人：${moveModel.transfer}<br><br>
    收款人：${moveModel.payee}<br><br>
    金额  ：${moveModel.money}<br><br>
    <br>

    <form method="get" action="/sc/jump">
        <button type="submit" name="type" value="query">查询转账结果</button>
    </form>

</div>
</body>
</html>
