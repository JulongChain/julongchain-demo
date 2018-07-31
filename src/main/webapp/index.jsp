<%@ page contentType="text/html; charset=utf-8"%>
<html>
<head>
</head>
<body >

<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
<%--<link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">--%>
<div style="width: inherit;height: 980px;">
    <%--background:url(${pageContext.request.contextPath}/resources/pic/12.jpg);--%>
    <%--<div style="margin-right: 0;margin-top: 0">--%>
        <%--<form action="/user/index" style="margin-right: inherit">--%>
            <%--<button type="submit" class="btn btn-primary active" >登录</button><br><br>--%>
        <%--</form>--%>
    <%--</div>--%>

    <div style="margin-left:48.5%;margin-right:40%; margin-top: 20%">
        <form action="/sc/jump" style="margin-right: inherit">
            <%--<button type="submit" class="btn btn-primary active" name="type" value="instantiate">instantiate</button><br><br>--%>
            <button type="submit" class="btn btn-primary btn-block btn-large" name="type" value="move">转账</button><br><br>
            <button type="submit" class="btn btn-primary btn-block btn-large" name="type" value="query">查询</button><br><br>
        </form>
    </div>

</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
