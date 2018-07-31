<%@ page contentType="text/html; charset=utf-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
<script type="text/javascript">
    function check() {
        var name = document.getElementsByTagName('input');
        for (var i = 0; i < name.length; i++) {
            if (name[i] !== null&&name[i] !=="") {

            }else {
                alert('输入不能为空');
                return false
            }


        }
        document.baseForm.submit();
    }
</script>
<div  class="move">
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
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>

</body>
</html>