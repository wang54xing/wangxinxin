/**
 * Created by Niu Xin on 14/10/22.
 */
function loginout(t) {
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
}

function getPrivateLetterCount() {
    $.ajax({
        url: "/letter/count.do",
        type: "post",
        data: {
            'inJson': true
        },
        success: function (data) {
            //$("#sl").html(data.messageCount);

            if (data > 0) {
                $('#message').css('background', 'url(/img/newmessage.png) no-repeat 5px 0px');
                $("#sl").html(data);
            } else {
                $('#message').css('background', 'url(/img/nomessage.png) no-repeat 10px 10px');
            }
        }
    });
}

//getPrivateLetterCount();
//setInterval("getPrivateLetterCount()", 60000);//定时刷新未读私信数

function playMovie() {
    var $player_container = $("#intro-player");
    $player_container.fadeIn();

    var player = polyvObject('#edu-player-div').videoPlayer({
        'width':'800',
        'height':'450',
        'vid' : '1a9bb5bed503c9d479b7119377d7abb6_1'
    });
    $('.dialog-bg').fadeIn('fast');
}

function closeMovie() {
    var $player_container = $("#intro-player");
    $player_container.fadeOut();
    $('#edu-player-div').empty();
    $('.dialog-bg').fadeOut('fast');
}

//$.ajaxSetup({cache: false });