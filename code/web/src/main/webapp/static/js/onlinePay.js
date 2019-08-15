$(function() {
    $(".currentSelected").removeClass("currentSelected");
    $(".icon_account").addClass("currentSelected");

    var inputPrice = $('#input-price');

    inputPrice.keyup(function(event) {
        var priceRule = /^(?:\d+|\d+\.\d{0,1})$/;
        if (!priceRule.test(inputPrice.val())) {
            inputPrice.val(inputPrice.val().substr(0, inputPrice.val().length - 1));
        }
        if (!(inputPrice.val()[0] >= 0 && inputPrice.val()[0] <= 9)) {
            inputPrice.val('');
        }
    });

    $('#pay-btn').on('click', function() {
        if (inputPrice.val() != '') {
            $.ajax({
                url: '/generateRechargeOrders.action',
                type: 'post',
                dataType: 'json',
                data: {
                    rechargeAmount: inputPrice.val()
                },
            }).success(function(order) {
                checkCourseBuy(order);
            }).error(function() {
//                alert('服务器错误！');
            });
        }
    });
});

function checkCourseBuy(order) {
    var checkCourse;
    $.ajax({
        url: '/validateCoursesBuy.action',
        type: 'post',
        data: {
            'orderid': order.orderId
        },
        async: false,
        success: function(data) {

            checkCourse = data;
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    })

    if (!checkCourse.result) {
        MessageBox(checkCourse.text, -1);
    } else {
        DoOnlinePay(order);
    }
}

function DoOnlinePay(order) {
    var type = $("input[name='online']:checked").val();
    var url;
    if (type == '1') {
        url = '/fortest/paypal.action';
    } else {
        url = '/fortest/pay.action';
    }

    $('#post-pay-dialog, #bg1').show();

    var f = document.getElementById('online-pay-form');
    f.action = url;
    f.orderid.value = order.orderId;
    f.submit();
    $('.pay-success').attr('href', '/business/user/onlinepayResult.jsp?oid=' + order.orderId);
}