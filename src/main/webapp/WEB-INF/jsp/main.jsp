<%@ page contentType="text/html; charset=utf-8"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>聚龙链演示平台</title>
</head>
<body>

<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet"><form action="/sc/jump">
    <button style="margin-top: 1%;width:5%;margin-left: 1%" type="submit" class="btn btn-primary btn-block btn-large" name="type" value="point">设置</button>
</form>
<%--<link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">--%>
<div style="width: inherit;height: 980px;">

    <h1 style="margin-left:42%;margin-right:45%;margin-top: 10%"><a href="/sc/jump" style="color:gold;text-decoration:none;">聚龙链演示平台</a></h1>
    <div style="margin-left:44.5%;margin-right:43.5%; margin-top: 2%">
        <form action="/sc/jump" style="margin-right: inherit">
            <%--<button type="submit" class="btn btn-primary active" name="type" value="instantiate">instantiate</button><br><br>--%>
            <button type="submit" class="btn btn-primary btn-block btn-large" name="type" value="move">转账</button>
            <br><br>
            <button type="submit" class="btn btn-primary btn-block btn-large" name="type" value="query">查询</button>
            <br><br>
            <button type="submit" class="btn btn-primary btn-block btn-large" name="type" value="block">区块查看</button>
            <br><br>
        </form>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>





