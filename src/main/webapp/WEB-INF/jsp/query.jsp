<%--
  Created by IntelliJ IDEA.
  User: Benjamin
  Date: 2018/7/6
  Time: 10:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">

</head>
<body>
<script type="text/javascript">
    function check(){
        var name=document.getElementById('name').value;
        if(name!==null&&name!==""){
            document.baseForm.submit();
        }else{
            alert('输入不能为空');
            document.baseForm.name.focus();

        }
    }
</script>


<div style="width: inherit;height: 980px;padding-top: 20%;" class="query">
    <%--background:url(${pageContext.request.contextPath}/resources/pic/12.jpg);--%>
    <div style="margin-left:45%;margin-right:35%;">

    <form action="/sc/query" style="margin-right: inherit" name="baseForm">
        <h1>查询</h1><input type="text" name="name" placeholder="用户名" id="name"><br><br>
        <%--金额：<input type="text" name="money"><br><br>--%>
        <div style="margin-left: 40%">
            <button type="button" class="btn btn-primary active" onclick="return check()" >
                确定
            </button>
        </div>

    </form>
</div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
