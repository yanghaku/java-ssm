'use strict';

//提交ajax请求 判断用户的登录状态
$.ajax({
	url:"./user/status",
	type:"GET",
	success:function(data){
		if(data != null && data !== "")login_correct(data);
		else login_error();
	},
	error: login_error
});


toastr.options = { // toastr配置
	"closeButton": true, //是否显示关闭按钮
	"debug": false, //是否使用debug模式
	"progressBar": true, //是否显示进度条
	"positionClass": "toast-top-center",//显示的动画位置
	"showDuration": "400", //显示的动画时间
	"hideDuration": "1000", //消失的动画时间
	"timeOut": "7000", //展现时间
	"extendedTimeOut": "1000", //加长展示时间
	"showEasing": "swing", //显示时的动画缓冲方式
	"hideEasing": "linear", //消失时的动画缓冲方式
	"showMethod": "fadeIn", //显示时的动画方式
	"hideMethod": "fadeOut" //消失时的动画方式
};


//取得get参数
function getRequest() {
	let str = window.location.href ;
	const num = str.indexOf('?')+1;
	if(num !== -1) {
		let ans = {};
		str = str.substr(num);//取得get的参数
		let requests = str.split('&');//分割
		for(let i=0;i<requests.length; ++i){
			let tmp = requests[i].split('=');
			if(tmp.length === 2){
				ans[decodeURIComponent(tmp[0])] = decodeURIComponent(tmp[1]);
			}
		}
		if(ans === {})return null;
		return ans;
	}
	return null;
}
