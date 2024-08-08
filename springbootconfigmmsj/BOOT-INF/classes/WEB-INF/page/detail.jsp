<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>保存</title>
    <link rel="stylesheet" href="css/common.css"/>
</head>
<body>
<div>
    <h1>保存</h1>
    <div class="row">
        <div class="col">
            <label>用户名：</label><input id="username-input" type="text"/>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <label>密码：</label><input id="userpwd-input" type="text"/>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <button id="save-btn">保存</button>
        </div>
        <div class="col">
            <button id="close-btn">关闭</button>
        </div>
    </div>
    <input id="id" type="hidden" value="${param.id}" />
</div>
</body>
<script src="js/jquery-3.1.1.min.js"></script>
<script src="js/detail.js"></script>
</html>