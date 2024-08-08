$(function() {
    //获取用户列表
    $("#list-btn").on("click", getUserListFunc);

    //用户详情
    $(document).on("click", ".detail-btn",function(){
        let id = $(this).attr('data-id');
        location.href = "detail?id=" + id;
    });

    //删除用户
    $(document).on("click", ".delete-btn",function(){
        let id = $(this).attr('data-id');
        $.ajax({
            url: "user/delete/" + id,
            success: function(data){
                alert("删除成功！");
                getUserListFunc();
            },
            error: function(e){
                alert("系统错误！");
            },
        })
    });

    //添加用户
    $("#add-btn").on("click",function(){
        location.href = "detail";
    });

});

//函数：获取用户列表
let getUserListFunc = function() {
    $.ajax({
        url: "user/list",
        data: {
            currentPage: 1,
            pageSize: 100
        },
        success: function (data) {
            if (data) {
                $("#list-table").empty();
                $("#list-table").html("<tr><th>ID</th><th>账号</th><th>编号</th><th>操作</th></tr>");
                let userArray = data.records;
                for (let i in userArray) {
                    let user = userArray[i];
                    let id = user.id;
                    let userName = user.userName;
                    let userCode = user.userCode
                    let trTemplate = `<tr>
                                    <th>${id}</th>
                                    <th>${userName}</th>
                                    <th>${userCode}</th>
                                    <th>
                                        <button class="detail-btn" data-id="${id}">详情</button>
                                        <button class="delete-btn" data-id="${id}">删除</button>
                                    </th>
                                </tr>`;
                    $("#list-table").append(trTemplate);
                }
            }
        },
        error: function (e) {
            console.log("系统错误！");
        },
    })
}