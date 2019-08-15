//var videoId = getUrlId();

function getUrlId(){ 
	var url = location.href;  
	var paraString = url.split("/");
	return paraString[paraString.length - 1];
}
// 显示灰色 jQuery 遮罩层
//function showBg() {
//	$("#fullbg").css({
//		display : "block"
//	});
//	$("#dialog").show();
//}
// 关闭灰色 jQuery 遮罩
function closeBg() {
	$("#fullbg,.dialog").hide();
}

function showLoginDialog() {
    closeBg();
    $("#fullbg").css({
        display : "block"
    });
    $("#loginDialog").show();
}

function register(){
    _czc.push(["_trackEvent",'视频弹出框','点击','注册','2','a']);
}

function showBuyDialog(userInfo) {
    updateDialogInfo(userInfo);
    closeBg();
    $("#fullbg").css({
        display : "block"
    });
    $("#buyDialog").show();
}

function showLackBalanceDialog(userInfo) {
    updateDialogInfo(userInfo);
    closeBg();
    $("#fullbg").css({
        display : "block"
    });
    $("#insuDialog").show();
}

function updateDialogInfo(userInfo){
    var _discount = '';
    if(userInfo.isLocal){
        _discount = "￥0";
    }
    else{
        //_discount = '<img src="/img/lanbei.png" style="width:15px;margin:0 2px 3px 0;vertical-align:middle"/>' + userInfo.disCount;
        _discount = '￥' + userInfo.disCount.toFixed(1);
    }
	$('.cprice').html('￥' + userInfo.price.toFixed(1));
	$('.cdiscount').html(_discount + '<img style="margin-left:3px;vertical-align: bottom" title="该课程支持兰贝支付" src="/img/lanbei.png" />');

/*    var balance = '￥' + userInfo.balance.toFixed(1);
    if(typeof userInfo.lanbei!= 'undefined'){
        balance += '<img style="margin: 0 0 0 10px" src="/img/lanbei.png" />' + userInfo.lanbei.toFixed(1);
    }
	$('.cbalance').html(balance);*/
	
	 $.ajax({
     	url:"/user/getMyBalanceCount.action",
     	type:"post",
     	dataType:"json",
     	success:function(data){
     		$(".balance").html("");
     		$(".balance").append(parseFloat(data.userInfo.balance).toFixed(1));
     		$(".giftBalance").html("");
     		$(".giftBalance").append(parseFloat(data.userInfo.giftBalance).toFixed(1));
     	}
     });
}


function checkUserInfo(callback){
	$.getJSON("/user/isLogin.action",
			{"packageId": packageId},
			function(userInfo){
				//userInfo = data;
				callback(userInfo);
		});
}

function ClickBuyButton()
{
    checkUserInfo(function(userInfo){
        if(!userInfo.isLogin){
            showLoginDialog();
        }
        else if(userInfo.isBuy || userInfo.disCount == 0 || userInfo.isLocal) {
            return;
        }
        else if(userInfo.canBuy) {
            $('.preview-prompt').html('');
            showBuyDialog(userInfo)
        }
        else{
            showLackBalanceDialog(userInfo);
        }
    });
}

function s2j_onPlayOver()
{
    /*
	checkUserInfo(function(userInfo){
		if(!userInfo.isLogin){
			showLoginDialog();
		}
		else if(userInfo.isBuy || userInfo.disCount == 0 || userInfo.isLocal) {
            //$.get("/courseVideoEnd.action", {"videoId": videoId});
			return;
		}
		else if(userInfo.canBuy) {
			showBuyDialog(userInfo);
		}
		else{
			showLackBalanceDialog(userInfo);
		}
        $.get("/courseVideoTrialEnd.action", {"videoId": videoId});
	});*/
    /*
    if(statisticTimer != null) {
        clearInterval(statisticTimer);
        statisticTimer = null;
    }*/
    if(!isTrial) {
        return;
    }

    if(!isLogin){
        showLoginDialog();
    }else {
        checkUserInfo(showBuyDialog);
    }
    $.get("/courseVideoTrialEnd.action", {"videoId": videoId});
    /*
    checkUserInfo(function(userInfo){
        if(!userInfo.isLogin){
            showLoginDialog();
        }
        else {
            showBuyDialog(userInfo);
        }
        $.get("/courseVideoTrialEnd.action", {"videoId": videoId});
    });*/
}

function loginAndTryBuy() {
    _czc.push(["_trackEvent",'视频弹出框','点击','登录','2','a']);
	$("#dialog .message").html("");
    $.ajax({
        url: "/login.action",
        type: "post",
        dataType: "json",
        data: {
            'userName': $("#usn").val(),
            'password': $("#pwd").val(),
            'inJson': true
        },
        success: function (data) {
            if (data.status == "ok") {
            	checkUserInfo(tryBuy);
            } else {
                $(".dialog .message").html(data ? data.errorMessage : "");
            }
        }
    });
}

function tryBuy(userInfo){
	if(userInfo.isBuy || userInfo.disCount == 0 || userInfo.disCount >= 9999 || userInfo.isLocal||true) {//免费
		location.reload();
	}else{
        showBuyDialog(userInfo);
    }
	//else if(userInfo.canBuy){
        //showBuyDialog(userInfo);
	//}
	//else{
		//showLackBalanceDialog(userInfo);
	//}
}

function buyVideo(){
    /*
    var message = '<div><p style="margin-bottom: 30px"><i class="fa fa-question-circle fa-4x" style="vertical-align: middle; margin-right: 30px"></i>确定购买？</p>';
    message += '<input onclick="doBuyVideo()" type="button" class="submit-button" value="购买" style="font-size:13px; width:80px"/>';
    message += '<input onclick="$(\'#prompt-div\').remove()" type="button" class="submit-button" value="取消" style="margin-left:20px;background: gray;width: 80px; font-size:13px"/>';
    message += '</div>';
    ShowPromptDialog(message, true);
    */
    doBuyVideo();
}

function doBuyVideo(){
	$.ajax({
		url:"/user/toBuy.action",
		data: {'goodsId': videoId},
		type:"get",
		success:function(data){
			if(data.result){
                ShowPromptDialog('<i class="fa fa-check-circle fa-4x" style="vertical-align: middle; margin-right: 30px"></i>购买成功。', true);
                setTimeout(function(){location.reload();}, 1500);
			}else{
                ShowPromptDialog('<i class="fa fa-exclamation-triangle fa-4x" style="vertical-align: middle; margin-right: 30px"></i>购买失败：可能是您的余额不足、该课程已禁止购买或已经购买过该课程。');
			}
		},
        error:function(data){
            ShowPromptDialog('<i class="fa fa-exclamation-triangle fa-4x" style="vertical-align: middle; margin-right: 30px"></i>购买失败：服务器响应请求出错，请稍后再试。');
        }
	});
}

var wtimer, statisticTimer = null;
function s2j_onPlayStart()
{
    /*
    checkUserInfo(function(userInfo){
        if(userInfo.isLogin && (userInfo.isBuy || userInfo.disCount == 0)){
            wtimer = setInterval(watchTimeCheck, 1000);
        }
    });*/
    if(!isTrial) {
        wtimer = setInterval(watchTimeCheck, 1000);
    }
}

/*
function s2j_onVideoPause() {
    if(statisticTimer != null) {
        clearInterval(statisticTimer);
        statisticTimer = null;
    }
}

function s2j_onVideoPlay() {
    if(isLogin && statisticTimer == null) {
        statisticTimer = setInterval(statisticWatchTime, 60000);
    }
}

function statisticWatchTime() {
    $.get('/user/addUserWatchVideoTime.action');
}
*/

function watchTimeCheck(){
    var player=getPlayer("polyvplayer1a9bb5bed53becd8b73ad7248608ac85_1");
    if(player!=undefined && player.j2s_getCurrentTime!=undefined && player.j2s_getCurrentTime()>0){
        var sec=player.j2s_getCurrentTime();
        if(videoLen - sec <= 30) {
            $.get("/courseVideoEnd.action", {"videoId": videoId});
            clearInterval(wtimer);
        }
    }
}