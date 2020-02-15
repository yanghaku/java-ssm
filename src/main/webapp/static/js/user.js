"use strict";

//全局变量
const number2level = ["小学", "初中", "高中", "大学四级", "大学六级", "专业四级", "专业六级", "雅思", "托福"];
const level2number = function(level){
    return number2level.indexOf(level);
};

// 不是登录状态的处理函数
const login_error = function(){
    const now = window.location.href.split('/').pop();
    if(now !== "" && now !== "index.html"){//如果不是首页，返回首页 (也就是说不是登录状态的只能访问首页）
        alert("please login first!");
        window.location.href = "./index.html" ;
        return;
    }
    $(".navbar-right").append(
        '<li class="dropdown user_status"><a href="javascript:void(0)" onclick=\'$("#login_modal").modal("show")\'>登录</a></li>' +
        '<li class="dropdown user_status"><a href="javascript:void(0)" onclick=\'$("#register_modal").modal("show")\'>注册</a></li>');
};

// 登录状态的处理函数
const login_correct = function(username){
    $(".user_status").remove();
    $(".navbar-right").append(
        "<li class='dropdown user_status'><a href='javascript:void(0);' role='button' class='dropdown-toggle' data-toggle='dropdown'>" +
        "欢迎 <span id='username'>"+ username + "</span><i class='fa fa-angle-down'></i></a>"+
        "<ul role='menu' class='dropdown-menu' aria-labelledby='dLabel'><li><a href='./userHome.html'>我的主页</a></li>" +
        "<li><a href='articleEdit.html'>发表文章</a></li>" +
        "<li><a href='javascript:change_profile();'>修改资料</a></li>" +
        "<li><a href='javascript:void(0)' onclick='logout_onclick();'>注销</a></li></ul></li>");
};

// 登录事件
$("#login_submit").on('click',function () {
    let user = {};
    user.username = $("#login_username").val();

    if(user.username === null || user.username === ""){
        toastr.error("username could not be null","login fail");//用户名不能为空
        return;
    }

    user.password = $.md5($("#login_password").val());//密码用md5加密

    const user_json = JSON.stringify(user);//生成对应的json

    //console.log(user_json);

    //提交ajax请求
    $.ajax({
        url:"./user/login",
        type:"POST",
        data:user_json,
        contentType:"application/json;charset=utf-8",
        success:function(data){
            if(data === "success"){//登录成功
                $("#login_modal").modal('hide');
                login_correct(user.username);
                toastr.success("login success! ","success");
            }
            else{
                toastr.error(JSON.stringify(data),"login fail!");
            }
        }
    });
});

// 注册事件
$("#register_submit").on('click',function () {
    let user = {};
    user.username = $("#register_username").val();
    if(user.username === null || user.username === ""){
        toastr.error("username could not be null!","register fail!");//用户名不能为空
        return;
    }

    const password1 = $("#register_password").val();
    const password2 = $("#register_password_repeat").val();
    if(password1 === null || password1 === "") {
        toastr.error("password could not be null!","register fail");
        return;
    }
    if(password1 !== password2){
        toastr.error("password not equal!","register fail");
        return;
    }
    user.password = $.md5(password1);//密码用md5加密

    const pattern = new RegExp("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    user.email = $("#register_email").val();
    if(pattern.test(user.email) === false){
        toastr.error("email pattern Error!","register fail");
        return;
    }

    user.phoneNumber = $("#register_phone_number").val();
    if( (/^1[3456789]\d{9}$/).test(user.phoneNumber) === false){
        toastr.error("phone number pattern error!","register fail");
        return;
    }
    user.birthday = $("#register_birthday").val();
    user.qqNumber = $("#register_qq_number").val();
    user.nickname = $("#register_nickname").val();
    user.level = level2number($("#register_level").val());
    user.registerTime = new Date();

    const user_json = JSON.stringify(user);//生成对应的json

    //提交ajax请求
    $.ajax({
        url:"./user/register",
        type:"POST",
        data:user_json,
        contentType:"application/json;charset=utf-8",
        success:function(data){
            if(data === "success"){//注册成功
                $("#register_modal").modal('hide');
                login_correct(user.username);
                toastr.success("register success!!","success");
            }
            else{
                toastr.error(JSON.stringify(data),"register fail!");
            }
        }
    });
});

//注销点击事件
function logout_onclick() {
    $.ajax({
        url:"./user/logout",
        type:"GET",
        async:false,
        success:function(){
            toastr.success("logout success!","success");
            window.location.href="./index.html";
        }
    });
}



// 获取user的所有信息
function getUser(user,callback) {
    // 如果username为null 那就是获取当前登录的user
    let dataGET={};
    if( !(user.username === undefined)){
        dataGET.username = user.username;
    }
    $.ajax({
        url: "./user/getUser",
        type: "GET",
        data: dataGET,
        success: function (data) {
            Object.assign(user,data);// 深拷贝
            if(typeof callback === "function")callback();// 如果参是回调函数，就调用它
        },
        error: function (data) {
            toastr.error("get user info error!! "+JSON.stringify(data),"Error");
        }
    });
}


// 获取user的keyword
function getKeyword(username,callback) {
    if(username === undefined)return;
    $.ajax({
        url: "./user/getKeyword",
        type: "GET",
        data: {"username": username},
        success: function (data) {
            if(typeof callback === "function")callback(data);
        },
        error: function (data) {
            toastr.error("get user keyword error!! "+JSON.stringify(data),"Error");
        }
    });
}


// 修改资料， 弹出修改资料的模态框
function change_profile() {
    let user={};
    getUser(user,function () {// 获取user信息之后再执行的回调函数
        const modalHtml = '<div class="modal fade" id="profile_modal">'+
            '<div class="modal-dialog"><div class="modal-content">' +
            '<div class="modal-header"><h4 class="center-block text-center"><b>Change Profile</b></h4></div>' +
            '<div class="modal-body">' +
            '<div class="form-group">' +
            '  <label for="profile_username">username</label>' +
            '  <input type="text" id="profile_username" class="form-control input-lg" readonly="readonly"  required/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_old_password">old password</label>' +
            '  <input type="password" id="profile_old_password" class="form-control input-lg" required />' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_password">new password</label>' +
            '  <input type="password" id="profile_password" class="form-control input-lg" required />' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_password_repeat">new password repeat</label>' +
            '  <input type="password" id="profile_password_repeat" class="form-control input-lg" required/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_email">email</label>' +
            '  <input type="email" id="profile_email" class="form-control input-lg"/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_phone_number">phone number</label>' +
            '  <input type="tel" id="profile_phone_number" class="form-control input-lg"/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_qq_number">QQ number</label>' +
            '  <input type="number" id="profile_qq_number" class="form-control input-lg"/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_birthday">birthday</label>' +
            '  <input type="date" id="profile_birthday" class="form-control input-lg" required/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_nickname">nickname</label>' +
            '  <input type="text" id="profile_nickname" class="form-control input-lg"/>' +
            '</div>' +
            '<div class="form-group">' +
            '  <label for="profile_level">当前等级</label>' +
            '  <select class="form-control" id="profile_level" required>' +
            '    <option selected>小学</option>' +
            '    <option>初中</option>' +
            '    <option>高中</option>' +
            '    <option>大学四级</option>' +
            '    <option>大学六级</option>' +
            '    <option>专业四级</option>' +
            '    <option>专业六级</option>' +
            '    <option>雅思</option>' +
            '    <option>托福</option>' +
            '  </select>' +
            '</div> <div id="profile_backup" style="display: none"></div>' +
            '</div><div class="modal-footer">' +
            '  <button type="button" onclick="changeProfileSubmit()" class="btn btn-primary">submit</button>' +
            '  <button type="button" class="btn btn-secondary" data-dismiss="modal">cancel</button>' +
            '</div></div></div></div>';

        $("#profile_modal").remove();// 原有的话就删除
        $("body").append(modalHtml);

        $("#profile_backup").text(JSON.stringify(user));
        $("#profile_username").val(user.username);
        $("#profile_email").val(user.email);
        $("#profile_phone_number").val(user.phoneNumber);
        const date=new Date(user.birthday);
        let year=String(date.getFullYear());
        while (year.length<4)year='0'+year;
        let month=String(date.getMonth());
        while (month.length < 2)month = '0' + month;
        let day=String(date.getDate());
        while (date.length < 2)day = '0' + day;
        $("#profile_birthday").val(year+"-"+month+"-"+day);
        $("#profile_qq_number").val(user.qqNumber);
        $("#profile_nickname").val(user.nickname);
        $("#profile_level").val(number2level[user.level]);

        $("#profile_modal").modal();
    });
}


// 修改资料提交的函数
function changeProfileSubmit() {
    let user = JSON.parse( $("#profile_backup").text() );

    const old_password = $.md5($("#profile_old_password").val());
    if(old_password !== user.password){
        toastr.error("old password is not correct!!!!");
        return;
    }

    const password1 = $("#profile_password").val();
    const password2 = $("#profile_password_repeat").val();
    if(password1 === null || password1 === "") {
        toastr.error("password could not be null!","profile fail");
        return;
    }
    if(password1 !== password2){
        toastr.error("password not equal!","profile fail");
        return;
    }
    user.password = $.md5(password1);//密码用md5加密

    const pattern = new RegExp("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    user.email = $("#profile_email").val();
    if(pattern.test(user.email) === false){
        toastr.error("email pattern Error!","profile fail");
        return;
    }

    user.phoneNumber = $("#profile_phone_number").val();
    if( (/^1[3456789]\d{9}$/).test(user.phoneNumber) === false){
        toastr.error("phone number pattern error!","profile fail");
        return;
    }
    user.birthday = $("#profile_birthday").val();
    user.qqNumber = $("#profile_qq_number").val();
    user.nickname = $("#profile_nickname").val();
    user.level = level2number($("#profile_level").val());

    const user_json = JSON.stringify(user);//生成对应的json
    console.log(user_json);
    $.ajax({
        url:"./user/updateUser",
        type:"POST",
        data:user_json,
        contentType:"application/json;charset=utf-8",
        success:function(data){
            if(data === 1){//注册成功
                $("#profile_modal").modal('hide');
                toastr.success("profile change success!!","success");
            }
            else{
                toastr.error(JSON.stringify(data),"profile change fail!");
            }
        }
    });
}