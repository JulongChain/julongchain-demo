
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>聚龙链演示平台</title>
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
<div style="margin-left:45%;margin-right:40%;margin-top:13%;color:#fff;">

    转账人：${moveModel.transfer}<br><br>
    收款人：${moveModel.payee}<br><br>
    金额&emsp;：${moveModel.money}<br><br>
    <br>

    <form method="get" action="/sc/jump">
        <button class="btn btn-primary active" type="submit" name="type" value="query">查询转账结果</button>
    </form>

</div>
</body>
</html>
