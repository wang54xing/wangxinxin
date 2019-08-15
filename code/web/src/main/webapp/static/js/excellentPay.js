	var email = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	var phone = /^1\d{10}$/;
	var reg = /^-?\d+[\.\d]?\d{0,2}$/;

    var uint = /^\d+$/;

//提现验证
function checkWithDraw(){

	var flag1 = false;
	var flag2 = false;
	var flag3 = false;

    var flag4 = false;
    var flag5 = false;
	var flag6 = false;

    var flag_bank = false;
    var flag_bank_no = false;
	//支付宝账号
	var account = $('#zhibubao-account').val();
	if(account == null || account == ''){
		$('#zhibubao-account').addClass('error-border');
		flag1 = msgShow($('#account-require'),'*  输入支付宝账号');
	}else{
		if(email.test(account) || phone.test(account)){
			$('#zhibubao-account').removeClass('error-border');
			flag1 = msgHide($('#account-require'));
		}else{
			$('#zhibubao-account').addClass('error-border');
	    	flag1 = msgShow($('#account-require'),'*  账号格式为邮箱或者手机号码');
		}
	}

    //户名
    var account_name = $('#XM-account').val();
    if(account_name == null || account_name == '') {
        $('#XM-account').addClass('error-border');
        flag4 = msgShow($('#account-require-XM'),'*  输入收款人姓名');
    } else {
		$('#XM-account').removeClass('error-border');
		flag4 = msgHide($('#account-require-XM'));
    }

    //手机
    var phone_no = $('#phone-account').val();
    if(phone_no == null || phone_no == ''){
        $('#phone-account').addClass('error-border');
        flag5 = msgShow($('#account-require-SJ'),'*  输入联系手机');
    }else{
        if(phone.test(phone_no)){
            $('#phone-account').removeClass('error-border');
            flag5 = msgHide($('#account-require-SJ'));
        }else{
            $('#phone-account').addClass('error-border');
            flag5 = msgShow($('#account-require-SJ'),'*  手机号码不正确');
        }
    }

    //开户行
    var bank_name = $('#bank-account').val();
    if(bank_name == null || bank_name == '') {
        $('#bank-account').addClass('error-border');
        flag_bank = msgShow($('#account-require-YH'),'*  输入开户行');
    } else {
        $('#bank-account').removeClass('error-border');
        flag_bank = msgHide($('#account-require-YH'));
    }

    //银行卡号
    var bank_account_no = $('#ZH-account').val();
    if(bank_account_no == null || bank_account_no == ''){
        $('#ZH-account').addClass('error-border');
        flag_bank_no = msgShow($('#account-require-ZH'),'*  输入银行卡号');
    }else{
        if(uint.test(bank_account_no)){
            $('#ZH-account').removeClass('error-border');
            flag_bank_no = msgHide($('#account-require-ZH'));
        }else{
            $('#ZH-account').addClass('error-border');
            flag_bank_no = msgShow($('#account-require-ZH'),'*  卡号错误');
        }
    }

	//提现金额
	var amount = $('#withdraw-numb').val();
	if(amount == null || amount == ''){
		$('#withdraw-numb').addClass('error-border');
    	flag2 = msgShow($('#withdraw-require'),'*  输入提取金额');
	}else{
		if(!reg.test(amount)){
			$('#withdraw-numb').addClass('error-border');
			flag2 = msgShow($('#withdraw-require'),'*  数值必须为整数或小数，小数点后不超过2位');
		}else{
			if(parseFloat(amount) <= 0){
		    	$('#withdraw-numb').addClass('error-border');
		    	flag2 = msgShow($('#withdraw-require'),'*  提取金额必须大于等于0.01');
			}else{
				if(parseFloat($('#withdraw-numb').val()) > accountBalance){
					flag2 = msgShow($('#withdraw-require'),'*  提取金额必须小于等于账户余额');
				}else{
					$('#withdraw-numb').removeClass('error-border');
			    	flag2 = msgHide($('#withdraw-require'));
			    	$('#withdraw-msg').show();
			    	$('#actual-withdraw').html($('#withdraw-numb').val());
				}
			}
		}
	}
	
	if($('#account-pwd').val() == null || $('#account-pwd').val() == ''){
		$('#account-pwd').addClass('error-border');
    	flag3 = msgShow($('#pwd-require'),'*  输入登录密码');
	}else{
		$('#account-pwd').removeClass('error-border');
    	flag3 = msgHide($('#pwd-require'));
	}
	if ($('#card-num').val() == null || $('#card-num').val() == '') {
		$('#card-num').addClass('error-border');
		flag6 = msgShow($('#card-require'),'*  输入身份证号');
		$(".input-P").css("display","none");
	} else {
		if (IdentityCodeValid($('#card-num').val())) {
			$('#card-num').removeClass('error-border');
			flag6 = msgHide($('#card-require'));
			$(".input-P").css("display","inline-block");
		} else {
			flag6 = msgShow($('#card-require'),'*  身份证号输入错误');
			$(".input-P").css("display","none");
		}

	}
	if(flag2 && flag3 && flag4 && flag5 && flag6){
        if (withDrawZFB) {
            if (flag1) {
                return true;
            }
        } else {
            if (flag_bank && flag_bank_no) {
                return true;
            }
        }
	}
    return false;
}
	//验证身份证
	function IdentityCodeValid(code) {
		var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
		var tip = "";
		var pass= true;

		if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
			tip = "身份证号格式错误";
			pass = false;
		}

		else if(!city[code.substr(0,2)]){
			tip = "地址编码错误";
			pass = false;
		}
		else{
			//18位身份证需要验证最后一位校验位
			if(code.length == 18){
				code = code.split('');
				//∑(ai×Wi)(mod 11)
				//加权因子
				var factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];
				//校验位
				var parity = [ 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 ];
				var sum = 0;
				var ai = 0;
				var wi = 0;
				for (var i = 0; i < 17; i++)
				{
					ai = code[i];
					wi = factor[i];
					sum += ai * wi;
				}
				var last = parity[sum % 11];
				if(parity[sum % 11] != code[17]){
					tip = "校验位错误";
					pass =false;
				}
			}
		}
		//if(!pass) alert(tip);
		return pass;
	}
//充值金额验证
function checkRecharge(input){
	var flag = false;
	if(input == null || input == ''){
    	$('#recharge-numb').addClass('error-border');
    	flag = msgShow($('#recharge-require'),'*  输入充值金额');
	}else{
		if(!reg.test(input)){
	    	$('#recharge-numb').addClass('error-border');
	    	flag = msgShow($('#recharge-require'),'*  数值必须为整数或小数，小数点后不超过2位');
		}else{
			if(parseFloat(input) <= 0){
		    	$('#recharge-numb').addClass('error-border');
		    	flag = msgShow($('#recharge-require'),'*  充值金额必须大于等于0.01');
			}else{
		    	$('#recharge-numb').removeClass('error-border');
		    	flag = msgHide($('#recharge-require'));
			}
		}
	}
	return flag;
}

function msgShow($selector,msg){
	$selector.show();
	$selector.html(msg);
	return false;
}

function msgHide($selector){
	$selector.hide();
	$selector.html('');
	return true;
}
$(function() { 
    var inputPrice = $('#recharge-numb');
	$('#recharge-btn').on('click', function() {
		if(checkRecharge(inputPrice.val())){
			$('#post-pay-dialog, #bg1').show();
			window.open('/emarket/createRecharge.do?rechargeMoney='+inputPrice.val()+'&payType=0');
			// $.ajax({
   //              url: '/excellentLesson/createRecharge.do',
   //              type: 'post',
   //              dataType: 'json',
   //              async: false,
   //              data: {
   //                  rechargeMoney: inputPrice.val(),
   //                  payType: 0
   //              },
   //          }).success(function(data) {
   //              if (data.resultCode==0) {
   //                 DoOnlinePay(data); 
   //             } else {
   //              alert("订单生成失败！请重新创建！");
   //             }
                
   //          }).error(function() {
   //              alert('服务器错误！');
   //          });
		}
    });
    var payAccount = $('#zhibubao-account');
    var money = $('#withdraw-numb');
    var password = $('#account-pwd');
	var phone = $('#phone-account');
	var bankAccount = $('#bank-account');
	var zhaccount = $('#ZH-account');
	var username = $('#XM-account');
	var cardnum = $('#card-num');
    $('#withdraw-btn').on('click', function() {
        if (checkWithDraw()) {
		var tid = $(this).attr('tid');
			var pay = '';
			if (tid==1) {
			pay = payAccount.val();
			} else {
				pay = zhaccount.val();
			}
            $.ajax({
                url: '/emarket/withdrawCash.do',
                type: 'post',
                dataType: 'json',
				data: {
					paypalAccount: pay,
					cash: money.val(),
					password: password.val(),
					phone : phone.val(),
					username:username.val(),
					openbank:bankAccount.val(),
					paytype:tid,
					cardnum:cardnum.val()
				}
            }).success(function(data) {
                if (data.resultCode==0) {
                   alert("提取金额成功！我们会在3个工作日内到账！");
                   window.location.href = '/emarket/withDraw.do';
               } else if (data.resultCode==2) {
					$('#XM-account').addClass('error-border');
					flag4 = msgShow($('#account-require-XM'),'*  收款人姓名与本用户不符');
               } else {
					alert(data.mesg);
				}
                
            }).error(function() {
                alert('服务器错误！');
            });
        }
    });
    
});

function DoOnlinePay(data) {
 	location.href = '/pay/fzpaypal.do?orderid='+data.orderId;
    $('#post-pay-dialog, #bg1').show();
    
}
function showTips(){
	var amount = $('#withdraw-numb').val();
	$('#withdraw-msg').hide();
	if(amount == null || amount == ''){
		/*$('#withdraw-numb').addClass('error-border');
    	flag2 = msgShow($('#withdraw-require'),'*  输入提取金额');*/
	}else{
		if(!reg.test(amount)){
			$('#withdraw-numb').addClass('error-border');
			flag2 = msgShow($('#withdraw-require'),'*  数值必须为整数或小数，小数点后不超过2位');
		}else{
			if(parseFloat(amount) <= 0){
		    	$('#withdraw-numb').addClass('error-border');
		    	msgShow($('#withdraw-require'),'*  提取金额必须大于等于0.01');
			}else{
				if(parseFloat($('#withdraw-numb').val()) > accountBalance){
					msgShow($('#withdraw-require'),'*  提取金额必须小于等于账户余额');
				}else{
					$('#withdraw-numb').removeClass('error-border');
			    	msgHide($('#withdraw-require'));
			    	
			    	$('#withdraw-msg').show();
			    	$('#actual-withdraw').html($('#withdraw-numb').val());
				}
		    	
			}
		}
	}
}