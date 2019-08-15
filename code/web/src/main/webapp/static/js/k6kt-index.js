jQuery(document).ready(function(){
    $(".store-main-left-top").click(function(){
        $(".wind-banner-hot").fadeIn();
        $(".bg").fadeIn();
    });
    $(".div-hot-x").click(function(){
        $(".wind-banner-hot").fadeOut();
        $(".bg").fadeOut();
    });
});

$(function () {
    sessionStorage.setItem("login", false);

    $('.login-btn').on('click', function () {
        loginIndex();
    });

    $('.input-account').focus(function () {
        $(this).removeClass('error');
        $('.username-error').hide();

    });
    
    $('.input-password').focus(function () {
        $(this).removeClass('error');
        $('.password-error').hide();

    });
    $('.input-verycode').focus(function () {
        $(this).removeClass('error');
        $('.verycode-error').hide();

    });

    $('.input-password').keydown(function (event) {
        if (event.which == 13) {
            loginIndex();
        }
    });

    $('#close').on('click', function(){
        dialog();
    })

    $('#go').on('click', function(){
        window.location = "/newisolatepage/selectResult.do";
    })

    $('#stay').on('click', function(){
        dialog();
    })

    $('#mallindex').click(function(){
        /*location.href='/mall';*/
    })

    $('body').on('click', '.detail', function(){
        window.open("/mall/detail.do?id=" + $(this).attr('id'));
    })

    //$('#readMore').on('click', function(){
    //    if(login){
    //
    //    } else {
    //        alert("未登录，请先登录")
    //    }
    //})

    //$('body').on('click', '.addToCar', function(){
    //    if(login){
    //        alert($(this).attr('id'));
    //    } else {
    //        alert("未登录，请先登录");
    //    }
    //})

   // getGoods();
    
   // sso();
});


function sso() {
   
    $.ajax({
    	url: "http://www.mysso.com/k6ktsso/sso/login1.do?name=siri&pwd=1919&jsonpCallback=callbackFunction",
        type: "GET",
        dataType: 'jsonp',
        jsonp: "callback",
        data: {},
        crossDomain: true,
        cache: false,
        success: function (html) {
        },
        error: function (data) {
        }
    });
  
    
   
}















$(window).load(function () {
    if ($('#slider').length > 0) {
        $('#slider').fadeIn().nivoSlider({
            pauseOnHover: true,
            pauseTime: 4000,
            effect: 'random',
            directionNav: false, // Next & Prev navigation
            controlNav: false, // 1,2,3... navigation
            controlNavThumbs: false
        });
    }

    var arrStr = document.cookie.split(";");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == "momcallme") {
            if (decodeURI(temp[1]) != null && decodeURI(temp[1]) != "\"\"") {
                $(".input-account").val(decodeURI(temp[1]));
            }
        }
    }

});

function loginIndex() {
    $('.login-btn').addClass('active');
    $.ajax({
        url: "/user/login.do",
        type: "post",
        dataType: "json",
        data: {
            'name': $(".input-account").val(),
            'pwd': $(".input-password").val(),
            'verifyCode': $(".input-verycode").val()
        },
        success: function (data) {
            if (data.code == 200) {
            	
            	
            	$('#veryCodeSpan').hide();
                var session = data.message;
                $('#name').text(session.userName);
                $('#avatar').attr('src', session.minAvatar);
                sessionStorage.setItem("login", true);
                sessionStorage.setItem("userName", session.userName);
                $('.unlogin').hide();
                $('.login').show();
                $('#nm').text(session.userName);
                $('.login-password').hide();
                if(1 == session.k6kt){
                	 try
                     {
                		 setTimeout('location.href="/newisolatepage/selectResult.do"', 1500)
                         loginK6ktSso();
                     }catch(x)
                     {
                     	
                     }
                } else {
                    $('#home').hide();
                }
               

            } else {
                if (data.message == '用户名非法' || data.message == 'accountError') {
                    $('.input-account').addClass('error');
                    $('.username-error').show();
                } else if(data.message.substr(0,3) == '验证码'){
                    $('.input-verycode').addClass('error');
                    $('.verycode-error').text(data.message);
                    $('.verycode-error').show();
                    $('#veryCodeSpan').show();
                    changeImg();
                } else if(data.message.substr(0,3) == '2秒内'){
                    //alert("2秒内不可重复登陆");
                } else if (data.message=='该学校已停用，请联系复兰管理员！') {
                    layer.alert(data.message);
                } else{
                    $('.input-password').addClass('error');
                    $('.password-error').show();
                    var strCookie = document.cookie;
                    var arrCookie = strCookie.split("; ");
                    for (var i = 0; i < arrCookie.length; i++) {
                        var arr = arrCookie[i].split("=");
                        if ("password" == arr[0]) {
                            var pwErCoun=arr[1];
                            if(pwErCoun>=3){
                                $('#veryCodeSpan').show();
                                changeImg();
                            }
                        }
                    }
                }
            }
            //$(".input-verycode").val("");
        },
        complete: function () {
            $('.login-btn').removeClass('active');
        }
    });
}

function logout() {
    $.ajax({
        url: "/user/logout.do",
        type: "post",
        dataType: "json",
        data: {
            'inJson': true
        },
        success: function (data) {
            window.location.href = "/";
        }
    });
    ssologout();
}


function ssologout () {
    var logoutURL = "http://221.214.55.21:6603/dsssoserver/logout";
    
    $.ajax({
        url: logoutURL,
        type: "GET",
        dataType: 'jsonp',
        jsonp: "callback",
        crossDomain: true,
        cache: false,
        success: function (html) {
           
        },
        error: function (data) {
        	
        }
    });
  }



/*function playMovie(url) {
    var $player_container = $("#intro-player");
    $player_container.fadeIn();

    var player = polyvObject('#player_div').videoPlayer({
        'width': '800',
        'height': '450',
        'vid': '570daef50cf2372af0f4b7c3'
    });
}*/

/*function playMovie(url) {
    try {
        SewisePlayer.toPlay(url, "", 0, true);
    } catch (e) {
        playerReady.videoURL = url;
        isFlash = true;
    }
    $("#sewise-div").fadeIn();
    $("#play_I").fadeIn();
    $(".close-dialog").fadeIn();
}

function closeMovie() {
    var $player_container = $("#intro-player");
    $player_container.fadeOut();
    $('#player_div').empty();

}*/


var isFlash = false;
function playMovie(url) {
    try {
        SewisePlayer.toPlay(url, "", 0, true);
    } catch (e) {
        playerReady.videoURL = url;
        isFlash = true;
    }
    $("#sewise-div").fadeIn();
    $("#play_I").fadeIn();
    $(".close-dialog").fadeIn();
}



function playerReady(name){
    if(isFlash){
        SewisePlayer.toPlay(playerReady.videoURL, "", 0, true);
    }
}
function closeMoviee() {
    var $player_container = $(".close-dialog");
    $player_container.fadeOut();
    $("#sewise-div").fadeOut();
    $("#play_I").fadeOut();
    /* $("#plaI").empty();*/
}



function getGoods(){
    var reqData = {};
    reqData.sortType = 8;
    reqData.page = 1;
    reqData.pageSize = 10;
    $.ajax({
        type: "GET",
        data:reqData,
        url: "/mall/goods.do",
        async: false,
        dataType: "json",
        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
        success: function(resp){
            showGoods(resp.list);
        }

    })
}

function showGoods(data){
    var html = '';
    for(var i=0;i<data.length; i++){
        html += '<li class="detail" id="'+data[i].goodsId+'">';
        html += '<div>';
        html += '<img src="'+data[i].suggestImage+'">';
        html += '<dl>';
        html += '<dd>'+data[i].goodsName+'</dd>';
        html += '<dt>￥&nbsp;'+data[i].price/100+'</dt>';
        html += '<em></em>';
        //html += '<a id="'+data[i].goodsId+'" class="addToCar"></a>';
        html += '</dl>';
        html += '</div>';
        html += '</li>';
    }
    $('#goodsList').append(html);

}

function dialog(){
    if($('.bg').length>0){
        $('.bg').fadeToggle();
        $('.store-bg').fadeToggle();
    } else {
        location.href="/newisolatepage/selectResult.do";
    }

}



