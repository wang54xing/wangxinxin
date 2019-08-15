/**
 * Created by NiuXin on 14-5-29.
 * 订单相关模块
 */

function ListOrders() {
    $.getJSON('/goodsOrderList.action',function(data){
        $('#account-right-content').removeClass('hardloadingb');
        $('#order-list-table tr:gt(0)').remove();
        var html = '';
        for(var i in data.rows){
            var order = data.rows[i];

            var status, oper;
            switch (order.paymentStatus){
                case 0:
                    status = '<td style="color:#fe7200">等待支付</td>';
                    oper = '<td style="color:#fe7200"><a style="color:#fe7200" href="/business/user/payOrder.jsp?oid='+ order.orderId +'">付款</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a style="color:#fe7200" onclick="CancelOrder(' + order.orderId + ')">取消</a></td>'
                    break;
                case 1:
                    status = '<td>支付完成</td>';
                    oper = '<td></td>';
                    break;
                case 2:
                    status = '<td style="color:#fe7200">支付失败</td>';
                    oper = '<td style="color:#fe7200"><a style="color:#fe7200" href="/business/user/payOrder.jsp?oid='+ order.orderId +'">付款</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a style="color:#fe7200" onclick="CancelOrder(' + order.orderId + ')">取消</a></td>'
                    break;
                case 3:
                    status = '<td>订单取消</td>';
                    oper = '<td><a onclick="DeleteOrder(' + order.orderId + ')">删除该订单</a></td>';
                    break;
                default :
                    status = '<td></td>';
                    oper = '<td></td>';
            }

            html += '<tr align="center">';
            html += '<td>' + order.orderId + '</td>';
            html += '<td>' + order.date.replace('T', '<br/>') + '</td>';
            html += '<td style="text-align: left;padding-left: 10px; padding-right: 10px">';
            for(var j in order.packageList) {
                var co = order.packageList[j];
                var vid;
                if(co.courses[0].videos !=null && co.courses[0].videos[0]!=null){
           			vid = co.courses[0].videos[0].id;
           		}else{
           			vid = '';
           		}
                html += '<a href="/video/' + vid + '/'+co.id+'">' + co.packageName + '</a><br/>'
            }
/*            for(var j in order.coursesList) {
                var course = order.coursesList[j];
                var vid;
                if(course.videos !=null && course.videos[0]!=null){
                    vid = course.videos[0].id;
                }else {
                    vid = '';
                }
                html += '<a href="/video/' + vid + '">' + course.courseName + '</a><br/>'
            }*/
            html += '</td><td>' + order.rechargeAmount.toFixed(1) + '元</td>';
            html += status;
            html += oper;
            html += '</tr>';
        }

        if(!html) {
            html = '<tr><td colspan="6" align="center">暂无订单。</td></tr>';
        }
        $('#order-list-table').append(html);
    });
}

function DeleteOrder(oid) {
    ConfirmDialog('确定删除编号为' + oid + '的订单?', 'DoDeleteOrder(' + oid + ')');
}

function DoDeleteOrder(oid) {
    MessageBox('正在删除...', 0);
    $.ajax({
        url: "/deleteOrder.action",
        type: "get",
        data: {
            'orderId': oid
        },
        success: function(data) {
            if(data.status == 'ok'){
                MessageBox(data.message, 1);
                ListOrders();

            }else {
                MessageBox(data.message, -1);
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
}

function CancelOrder(oid) {
    ConfirmDialog('确定取消编号为' + oid + '的订单?', 'DoCancelOrder(' + oid + ')');
}

function DoCancelOrder(oid) {
    MessageBox('正在取消...', 0);
    $.ajax({
        url: "/cancelOrder.action",
        type: "get",
        data: {
            'orderId': oid
        },
        success: function(data) {
            if(data.status == 'ok'){
                MessageBox(data.message, 1);
                ListOrders();

            }else {
                MessageBox(data.message, -1);
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
}

function GetOrderDetail() {
    $.ajax({
        url: "/paymentOrder.action",
        type: "get",
        data: {
            'orderId': orderId
        },
        success: function(data) {
            if(data.orderInfo == null) {
                $('#order-title').html('订单不存在。');
                $('#order-title').show();
                $('#loading-div').hide();
                //setTimeout(function(){location.href = '/business/user/myOrderList.jsp';}, 5000);
            }else{
                switch (data.orderInfo.paymentStatus) {
                    case 1:
                        $('#order-message').html('订单已支付，无需再次支付。');
                        $('#order-title').show();
                        $('#loading-div').hide();
                        break;
                    case 3:
                        $('#order-message').html('订单已取消，无法支付。');
                        $('#order-title').show();
                        $('#loading-div').hide();
                        break;
                    default:
                        orderCoursePackage = data.coursePackageInfo;
                        userInfo = data.userInfo;
                        orderInfo = data.orderInfo;
                        UpdateOrderPage();
                        $('#loading-div').hide();
                        $('#order-title').show();
                        $('#order-content').show();
                        $('#confirmorder').show();
                }
            }
        },
        error: function(e) {
            $('#order-title').html('服务器响应请求失败。');
            $('#order-title').show();
            $('#loading-div').hide();
        }
    });
}

function UpdateOrderPage() {
    $('#order-courses-table tr:gt(0)').remove();
    var html = '';
    for(var i in orderCoursePackage){
        var course = orderCoursePackage[i];
        html += '<tr align="center"><td class="cart-entry" style="padding-left:25px;"><img src="' + course.imageUrl + '" />' +
            '</td><td style="font-family: Microsoft Yahei;font-size:16px;color:#7d7d7d;">'
                + course.packageName +'</td>';
        
        var teacherName = '';
	    if(course.teachers.length > 0){
	    	for(var j in course.teachers){
	    		teacherName += course.teachers[j].teacherName+"、";
	    		if(course.teachers.length >= 2 || course.teachers.length ==1){
	    			teacherName = teacherName.substr(0,teacherName.lastIndexOf("、"));
	    			if(course.teachers.length > 2){
		    			teacherName +=" 等";
		    			break;
	    			}
	    		}
	    	}
	    }
       
        html += '<td class="price"><span style="color:#7d7d7d">'+teacherName+'</span></td>';
        html += '<td class="price"><span style="color:#7d7d7d">￥' + course.price.toFixed(1) + '</span></td>';
        html += '<td class="price"><span style="color:#7d7d7d">￥' + course.discount.toFixed(1) + '</span></td>';
        html += '</tr>';
    }

    //html += '<tr><td colspan="5" style="height:10px;">&nbsp;</td></tr>'
    html += '<tr><td colspan="5" style="font-size:16px;text-align: right;color:#7d7d7d;padding-right:15px;height:20px;">' +
        '订单总金额：<span class="orange">￥' + orderInfo.rechargeAmount.toFixed(1) +'</span></td></tr>'
    $('#order-courses-table').append(html);

    $('#giftBalance-span').html('' + userInfo.giftBalance.toFixed(1));
    $('#balance-span').html('￥' + userInfo.balance.toFixed(1));

    if(userInfo.giftBalance + userInfo.balance > 0) {
        $('#balance-div').show();
    }

    if(userInfo.giftBalance + userInfo.balance >= orderInfo.rechargeAmount) {
        $('#online-pay-div').hide();
        needOnlinePay = false;
    }else {
        needOnlinePay = true;
    }

    if(userInfo.giftBalance > 0) {
        $("#gift-check").attr("checked", 'true');
    }
    else {
        $('#lanbei-div').hide();
    }

    if(userInfo.balance <= 0) {
        $('#rmb-div').hide();
    }else if(userInfo.giftBalance < orderInfo.rechargeAmount) {
        $("#balance-check").attr("checked", 'true');
    }
}

function OnlinePayCheck() {
    var availBalance = 0;
    if($('#gift-check').is(":checked")) {
        availBalance += userInfo.giftBalance;
    }
    if($('#balance-check').is(":checked")) {
        availBalance += userInfo.balance;
    }
    if(availBalance >= orderInfo.rechargeAmount){
        $('#online-pay-div').hide();
        needOnlinePay = false;
    }else {
        $('#online-pay-div').show();
        needOnlinePay = true;
    }
}

function HandlePayButton(){
    if(!needOnlinePay) {
        DirectPay();
    }
    else {
    	OnlinePay();
    }
}

function DirectPay() {
	var password = $('#pay-pass').val();
    if(password == '') {
        MessageBox('请输入登录密码。', -1);
        return;
    }
    MessageBox('支付中...', 0);
    $.ajax({
        url: "/pay.action",
        type: "post",
        data: {
            'orderid': orderId,
            "password":password,
            'isGiftBalance': $('#gift-check').is(":checked"),
            'isBalance': $('#balance-check').is(":checked")
        },
        success: function(data) {
        	if(data.result == true){
	        	location.href = "/business/user/payResult.jsp?oid=" + orderId;
	
	        }else {
	            MessageBox(data.text, -1);
	        }
	    },
	    error: function(e) {
	        MessageBox('服务器响应请求失败，请稍后再试。', -1);
	    }
    });
}

function OnlinePay() {
    var useGift = $('#gift-check').is(":checked"), useBalance = $('#balance-check').is(":checked");
    if(useGift || useBalance) {//需要密码
        var password = $('#pay-pass').val();
        if(password == '') {
            MessageBox('请输入登录密码。', -1);
            return;
        }
        //MessageBox('请求支付中...',0);
        var checkPassword;
        $.ajax({
            url:'/validatePassword.action',
            type:'post',
            data:{'password':password},
            async:false,
            success:function(data) {
//                if(!data.result) {
//                    MessageBox(data.text, -1);
//                }
//                else {
//                    ClosePromptDialog();
//                    DoOnlinePay();
//                }
                checkPassword = data;
            },
            error: function(e){
                MessageBox('服务器响应请求失败，请稍后再试。', -1);
            }
        })

        if(!checkPassword.result) {
            MessageBox(checkPassword.text, -1);
        }
        else {
            //ClosePromptDialog();
        	checkCourseBuy();
        }
    }
    else {
    	checkCourseBuy();
    }
}
function checkCourseBuy() {
        var checkCourse;
        $.ajax({
            url:'/validateCoursesBuy.action',
            type:'post',
            data:{'orderid': orderId},
            async:false,
            success:function(data) {

            	checkCourse = data;
            },
            error: function(e){
                MessageBox('服务器响应请求失败，请稍后再试。', -1);
            }
        })

        if(!checkCourse.result) {
            MessageBox(checkCourse.text, -1);
        }
        else {
            DoOnlinePay();
        }
}
function DoOnlinePay() {
    var type = $("input[name='online']:checked").val();
    var url;
    if(type == '1') {
        url = '/fortest/paypal.action';
    }
    else {
        url = '/fortest/pay.action';
    }

    $('#post-pay-dialog, #bg1').show();

    var f = document.getElementById('online-pay-form');
    f.action = url;
    f.orderid.value = orderId;
    f.isGiftBalance.value = $('#gift-check').is(":checked");
    f.isBalance.value = $('#balance-check').is(":checked");
    f.submit();
}

