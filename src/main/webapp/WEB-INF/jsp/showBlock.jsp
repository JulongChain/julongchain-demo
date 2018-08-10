<%--
  Created by IntelliJ IDEA.
  User: Benjamin
  Date: 2018/8/2
  Time: 10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
    <title>聚龙链区块查看</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/normalize.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/demo.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/component.css"/>

</head>
<body>
<div class="container">

    <header style="margin-top:40px">
        <h1><a href="/sc/jump" style="text-decoration:none;">聚龙链区块信息查看</a></h1>
    </header>
    <div class="component">
        <h2>最新块信息</h2>
        <p>最新区块号；交易id；当前区块HASH值；前一区块HASH值.</p>
        <table>
            <thead>
            <tr>
                <th>最新区块号</th>
                <th>交易id</th>
                <th>当前区块HASH值</th>
                <th>前一区块HASH值</th>
            </tr>
            </thead>

            <tbody>
            <tr>
                <td class="user-name">${blockInfo.numid}</td>
                <td class="user-email">${blockInfo.txid}</td>
                <td class="user-phone">${blockInfo.dataHash}</td>
                <td class="user-mobile">${blockInfo.previousHash}</td>
            </tr>
            </tbody>
        </table>

    </div>

</div><!-- /container -->
<script src="${pageContext.request.contextPath}/resources/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.ba-throttle-debounce.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.stickyheader.js"></script>
</body>
</html>
