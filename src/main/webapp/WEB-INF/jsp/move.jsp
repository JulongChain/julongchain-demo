<%@ page contentType="text/html; charset=utf-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>聚龙链演示平台</title>
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
<script type="text/javascript">
    function check() {
        var name = document.getElementsByTagName("input");
        for (var i = 0; i < name.length; i++) {
            if (name[i].value === null||name[i].value ==="") {
                alert('输入不能为空');
                return false
            }
        }
        document.baseForm.submit();
    }
</script>
<div style="margin-left:41.5%;margin-right:43.5%; margin-top: 12%">
<div  class="move">

        <h1><a href="/sc/jump" style="color:gold;text-decoration:none;">聚龙链演示平台</a> </h1>
        <h1>转账</h1>
        <form action="/sc/invoke" name="baseForm">
            <input type="text" placeholder="转账人" name="transfer" ><br><br>
            <input type="text" placeholder="收款人" name="payee" ><br><br>
            <input type="text" placeholder="金额" name="money" id="money" ><br><br>

            <button type="button" class="btn btn-primary btn-block btn-large" onclick="return check()">
                确定
            </button>


        </form>
    </div>


</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>

</body>
</html>