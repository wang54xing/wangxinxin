$(function() {
    initWindow();

    $('.login-btn').on('click', function() {
        loginIndex();
    });

    $('.input-account').focus(function() {
        $(this).removeClass('error');
        $('.username-error').hide();
        $(this).val('');
    });
    $('.input-password').focus(function() {
        $(this).removeClass('error');
        $('.password-error').hide();
        $(this).val('');
    });

    $('.input-password').keydown(function(event) {
        if (event.which == 13) {
            loginIndex();
        }
    });
    
    var arrStr = document.cookie.split(";");
    for(var i = 0;i < arrStr.length;i ++){ 
    	var temp = arrStr[i].split("="); 
    	if(temp[0] == "momcallme") {
    		if (decodeURI(temp[1])!=null && decodeURI(temp[1])!="\"\"") {
    			$(".input-account").val(decodeURI(temp[1]));
    		}
    		
    	}
    } 

    $('.company-info-content').find('br').after('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');

    $('.online-btn,.manage-btn').mouseenter(function() {
        $(this).animate({
            'bottom': '80px'
        }, 250).animate({
            'bottom': '57px'
        }, 250);
    });
});

window.onresize = function() {
    initWindow();
}

function initWindow() {

    var dheight = window.innerHeight;
    var dwidth = window.innerWidth;

    if (typeof dwidth != "number") {
        if (document.compatMode == "CSS1Compat") {
            dwidth = document.documentElement.clientWidth;
            dheight = document.documentElement.clientHeight;
        } else {
            dwidth = document.body.clientWidth;
            dheight = document.body.clientHeight;
        }
    }
    var cheight = dheight - 77;

    if (dwidth / cheight >= (1280 / 704)) {
        $('.main-container').css('backgroundSize', '100%');
    } else {
        $('.main-container').css('backgroundSize', 'auto ' + cheight + 'px');
    }
    $('.main-container').css('height', cheight);
    $('.content-container').css('height', cheight - 105);
}

function loginIndex() {
    $('.login-btn').addClass('active');
    $.ajax({
        url: "login.action",
        type: "post",
        dataType: "json",
        data: {
            'userName': $(".input-account").val(),
            'password': $(".input-password").val(),
            'schoolid': 211,
            'autologin': false,
            'inJson': true
        },
        success: function(data) {
            if (data.status == "ok") {
                if (!data.userInitialized) {
                    if (typeof data.geo == 'undefined') {
                        showActivate();
                    } else {
                        var prefix = document.domain.substring(0, document.domain.indexOf('.'));
                        var url = "http://" + document.domain.replace(prefix, data.geo);
                        if (typeof data.level == 'undefined') {
                            showActivate(url);
                        } else {
                            if (url.indexOf("com/" + data.geo) > 0) {
                                var currentUrl = url.replace("com/" + data.geo, "com/user");
                                showActivate(currentUrl);
                            } else {
                                showActivate(url + "/user");
                            }
                        }
                    }
                } else {
                    if (typeof data.geo == 'undefined') {
                        var prefix = document.domain.substring(0, document.domain.indexOf('.'));
                        var url = "http://" + document.domain.replace(prefix, data.geo);
                        window.location.href = url;
                    } else {
                        window.location = "/user";
                    }
                }
            } else {
                if (data.errorMessage == '用户名不存在') {
                    $('.input-account').addClass('error');
                    $('.username-error').show();
                } else {
                    $('.input-password').addClass('error');
                    $('.password-error').show();
                }
            }
        },
        complete: function() {
            $('.login-btn').removeClass('active');
        }
    });
}

function submitMessage() {
    validateContactus();
}

function validateContactus() {
    if (validateForm({
        name: {
            required: true,
            maxLength: 20
        },
        email_input: {
            required: false,
            email: true,
            maxLength: 255
        },
        phone: {
            required: false,
            maxLength: 11
        }
    }, {
        con: $('.showerror').find('span:last'),
        msg: ['用户名', '邮箱地址', '联系电话']
    })) {
        var name_content = $.trim($('#name').val());
        var message_content = $.trim($('#message_text').val());
        var email_content = $.trim($('#email_input').val());
        var phone_content = $.trim($('#phone').val());
        var valid = name_content && message_content && (email_content || phone_content);
        if (!valid) {
            if (!name_content) {
                $('#name-prompt').css('color', 'red');
                location.href = "#name-title";
            }
            if (!message_content) {
                $('#message-prompt').css('color', 'red');
            }

            if (!(email_content + phone_content)) {
                $('#email-prompt').css('color', 'red');
                $('#phone-prompt').css('color', 'red');
            }
            return;
        }
        var userMessage = {
            'userName': name_content,
            'mobileNumber': phone_content,
            'email': email_content,
            'message': message_content
        };
        $.ajax({
            url: "/callUs.action",
            type: "post",
            data: userMessage,
            success: function(data) {
                alert("我们已收到留言，会尽快处理，谢谢您的关注。");
                $('.contact-us-dl').find('input').val('');
                $('.contact-us-dl').find('textarea').text('');
                $('#submitBtn').attr('value', '提交');
                $('#submitBtn').css('cursor', 'pointer');
                $('#name-prompt').css('color', 'grey');
                $('#email-prompt').css('color', 'grey');
                $('#phone-prompt').css('color', 'grey');
            },
            error: function(e) {}
        });
        $('#submitBtn').attr('disabled', 'disabled');
        $('#submitBtn').attr('value', '提交中...');
        $('#submitBtn').css('cursor', 'wait');
    } else {}
}

var polyv_player;

function playMovie() {
    var id = "#intro-player";
    var vc = $(id);
    var url = 'http://player.polyv.net/videos/1a9bb5bed5a13cb439c3628a0e40bf48_1.swf';
    vc.html('<div id="mainv1a9bb5bed5fb74e2a8e3f4812421ded5_1_div" style="padding:15px 15px 10px;background-color: rgba(255, 255, 255, 0.5);box-shadow: 0 0 10px #666"></div><a class="close-dialog" style="color:black; opacity:0.7;position:absolute;top:0;right:2px;"><i class="fa fa-times-circle fa-lg"></i></a>');
    if (!polyv_player) {
        polyv_player = new polyvObject.swf(url, "mainv1a9bb5bed5fb74e2a8e3f4812421ded5_1", "800", "450", "#ffffff");
    } else {
        polyv_player.source = url;
        polyvObject.writePlayer(0);
    }
    vc.fadeIn();
    $('.close-dialog').on('click', function() {
        vc.empty();
        vc.fadeOut();
    });
}