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

function myAlert(header,content) {
	$("#alert_modal").remove();
	$("body").append('<div class="modal fade" id="alert_modal"><div class="modal-dialog">'+
'<div class="modal-content"><div class="modal-header"><h4 class="center-block text-warning"><b>' + header +
'</b></h4></div><div class="modal-body"><p class="text-center text-danger"><b>' + content +
'</b></p><div class="modal-footer"><button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>' +
'</div></div></div></div>');
	$("#alert_modal").modal();
}

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
