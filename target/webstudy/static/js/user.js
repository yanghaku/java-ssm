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
        "<li class='dropdown user_status'><a href='javascript:void(0)'>欢迎 <span id='username'>"+ username + "</span><i class='fa fa-angle-down'></i></a>"+
        "<ul role='menu' class='sub-menu'><li><a href='./userHome.html'>我的主页</a></li>" +
        "<li><a href='articleEdit.html'>发表文章</a></li>" +
        "<li><a href='javascript:void(0)' onclick='logout_onclick();'>注销</a></li></ul></li>");
};

// 登录事件
$("#login_submit").on('click',function () {
    let user = {};
    user.username = $("#login_username").val();

    if(user.username === null || user.username === ""){
        myAlert("login fail","username could not be null");//用户名不能为空
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
            }
            else{
                myAlert("login fail",data);
            }
        }
    });
});

// 注册事件
$("#register_submit").on('click',function () {
    let user = {};
    user.username = $("#register_username").val();
    if(user.username === null || user.username === ""){
        myAlert("register fail","username could not be null!");//用户名不能为空
        return;
    }

    const password1 = $("#register_password").val();
    const password2 = $("#register_password_repeat").val();
    if(password1 === null || password1 === "") {
        myAlert("register fail","password could not be null!");
        return;
    }
    if(password1 !== password2){
        myAlert("register fail","password not equal!");
        return;
    }
    user.password = $.md5(password1);//密码用md5加密

    const pattern = new RegExp("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    user.email = $("#register_email").val();
    if(pattern.test(user.email) === false){
        myAlert("register fail","email pattern Error!");
        return;
    }

    user.phoneNumber = $("#register_phone_number").val();
    if( (/^1[3456789]\d{9}$/).test(user.phoneNumber) === false){
        myAlert("register fail","phone number pattern error!");
        return;
    }
    user.birthday = $("#register_birthday").val();
    user.qqNumber = $("#register_qq_number").val();
    user.nickname = $("#register_nickname").val();
    user.level = level2number($("#register_level").val());
    user.registerTime = new Date();

    const user_json = JSON.stringify(user);//生成对应的json

    console.log(user_json);

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
            }
            else{
                myAlert("register fail",data);
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
            window.location.href="./index.html";
        }
    });
}

function getUser(username) {
    $.ajax({
        url: "./user/getUser",
        type: "GET",
        data: {"username": username},
        success: function (data) {
            console.log(data);
        }
    });
}
