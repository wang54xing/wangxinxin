var cacheKeyId = "";
$(function() {
    var today = new Date();
    //var dateStart = new Date("2016/9/3 00:00:01");
    var dateEnd = new Date("2016/9/10 23:59:59");
    if (today>dateEnd) {
        $('.teacher-D').hide();
    }
    $.ajax({
        url: "/user/check.do",
        type: "post",
        success: function (data) {
            if(data.flag){
                if (data.role==2) {
                    if (getCookie("coupon")=="false") {
                        $(".teacher-main").show();
                        $(".bg").show();
                    }
                } else {
                    //if (getCookie("coupon")=="false") {
                    //    $(".student-main").show();
                    //    $(".bg").show();
                    //}
                }

            }
        }
    });
    $('.teacher-D').click(function() {
        $(".teacher-main").show();
        $(".bg").show();
    });
    $('.student-main-I').click(function() {
        window.open("http://www.fulaan.com/forum/index.do");
    });
    $('.student-main-II').click(function() {
        window.open("http://www.fulaan.com/forum/competition.do");
    });
    $('.student-main-III').click(function() {
        window.open("http://www.fulaan.com");
    });
    $('.student-main-IV').click(function() {
        setCookie("coupon", "true", 7);
        $('.student-main').hide();
        $(".bg").hide();
    });
    $('.teacher-info-em').click(function() {
        setCookie("coupon", "true", 7);
        $('.teacher-main').hide();
        $('.bg').hide();
    });
    $('.teacher-go').click(function() {
        if ($('.teacher-inp').val()=='' || $('.teacher-inp').val()==null) {
            alert("请输入手机号码！");
            return;
        }
        if ($('#vific').val()=='' || $('#vific').val()==null) {
            alert("请输入验证码！");
            return;
        }
        $.ajax({
            url: "/user/update/basic3.do",
            type: "post",
            dataType: "json",
            data: {
                'mobile':$('.teacher-inp').val(),
                'cacheKeyId':cacheKeyId,
                'valiCode':$('#vific').val()
            },
            success: function (data) {
                if(data.code=="200")
                {
                    $.ajax({
                        url: "/mall/users/giveVoucher.do",
                        type: "post",
                        success: function (data) {
                            if (data.resultCode == 0) {
                                var a = $("<a href='http://www.fulaan.com/mall/detail.do?id=57c007d03d4df95eda9f08fa' target='_blank'>fulaan</a>").get(0);
                                var e = document.createEvent('MouseEvents');
                                e.initEvent('click', true, true);
                                a.dispatchEvent(e);
                                $('.teacher-main').hide();
                                $('.bg').hide();
                            }
                        }
                    });
                }
                else
                {
                    alert(data.message);
                }
            }
        });
    });

    $('.teacher-hq').click(function() {
        $.ajax({
            url: "/mall/users/getVerificationCode.do",
            type: "post",
            dataType: "json",
            data: {
                'mobile':$('.teacher-inp').val()
            },
            success: function (data) {
                if(data.code=="200")
                {
                    cacheKeyId = data.cacheKeyId;
                    settime($('.teacher-hq'));

                }
                else
                {
                    alert(data.message);
                }
            }
        });
    });
    $('.teacher-info-cl').click(function() {
        $.ajax({
            url: "/mall/users/giveVoucher.do",
            type: "post",
            success: function (data) {
                if(data.resultCode==0){
                    var a = $("<a href='http://www.fulaan.com/mall/detail.do?id=57c007d03d4df95eda9f08fa' target='_blank'>fulaan</a>").get(0);
                    var e = document.createEvent('MouseEvents');
                    e.initEvent( 'click', true, true );
                    a.dispatchEvent(e);
                    $('.teacher-main').hide();
                    $('.bg').hide();
                }else if (data.resultCode==1){
                    $('.teacher-bottom').show();
                }
                //else if (data.resultCode==2) {
                //    alert("您已领取过，不能重复领取！");
                //    $('.teacher-main').hide();
                //    $('.bg').hide();
                //}
            }
        });

    });
});
var countdown=60;
function settime (obj) {
    if (countdown == 0) {
        //obj.removeAttribute("disabled");
        obj.html("免费获取验证码");
        countdown = 60;
        return;
    } else {
        //obj.setAttribute("disabled", true);
        obj.html("重新发送(" + countdown + ")");
        countdown--;
    }
    setTimeout(function() {
            settime(obj) }
        ,1000)
}

//设置cookie
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
//获取cookie
function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
    }
    return "";
}
//清除cookie
function clearCookie(name) {
    setCookie(name, "", -1);
}