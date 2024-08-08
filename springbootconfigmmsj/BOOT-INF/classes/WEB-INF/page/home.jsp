<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>首页</title>
    <link rel="stylesheet" href="css/common.css"/>
</head>
<body>
<div>
    <h1>首页</h1>
    <div class="row">
        <table id="list-table" border="1">
            <tr>
                <th>编号</th>
                <th>账号</th>
                <th>密码</th>
                <th>操作</th>
            </tr>
        </table>
    </div>
    <div class="row">
        <div class="col">
            <button id="list-btn">用户列表</button>
        </div>
        <div class="col">
            <button id="add-btn">添加用户</button>
        </div>
    </div>
</div>
</body>
<script src="js/jquery-3.1.1.min.js"></script>
<script src="js/home.js"></script>
</html>