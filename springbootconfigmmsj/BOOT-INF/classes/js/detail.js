$(function() {
    //加载
    let id = $("#id").val();
    if(id){
        $.ajax({
            url: "user/get/" + id,
            success: function(data){
                if(data){
                    let userName = data.userName;
                    let userPwd = data.userPwd;
                    $("#username-input").val(userName);
                    $("#userpwd-input").val(userPwd);
                }else{
                    alert("系统错误！");
                }
            },
            error: function(e){
                alert("系统错误！");
            },
        })
    }

    //获取用户列表
    $("#save-btn").on("click",function(){
        let userName = $("#username-input").val();
        if(! userName){
            alert("用户名不能为空");
            return;
        }
        let userPwd = $("#userpwd-input").val();
        if(! userPwd){
            alert("密码不能为空");
            return;
        }
        let user;
        let url;
        //修改
        if(id){
            url = "user/update";
            user = {
                userName: userName,
                userPwd: userPwd,
                id: id
            };
            //添加
        }else{
            url = "user/add";
            user = {
                userName: userName,
                userPwd: userPwd
            };
        }
        $.ajax({
            url: url,
            type: "POST",
            data: user,
            success: function(data){
                alert("保存成功！");
            },
            error: function(e){
                alert("系统错误！");
            },
        })
    });

    //添加用户
    $("#close-btn").on("click",function(){
        location.href = "home";
    });

});