$(function() {
    // 名片
    $('body').on('mouseenter', '.info-user-img', function() {
        showCard($(this), -278);
    });
    $('body').on('mouseleave', '.info-user-img', function() {
        $('.info-card').hide();
    });
    $('body').on('mouseenter', '.info-card', function() {
        $(this).show();
    });
    $('body').on('mouseleave', '.info-card', function() {
        $(this).hide();
    });

    // 学生排名名片
    $('body').on('mouseenter', '.fc_stu_img', function() {
        showCard($(this), 46);
    });
    $('body').on('mouseleave', '.fc_stu_img', function() {
        $('.info-card').hide();
    });

    // 我的回复名片
    $('body').on('mouseenter', '.mycomment-user-img', function() {
        showCard($(this), -278);
    });

    $('body').on('mouseleave', '.mycomment-user-img', function() {
        $('.info-card').hide();
    });

    // 回复框名片
    $('body').on('mouseenter', '.replier-img', function() {
        showCard($(this), -278);
    });

    $('body').on('mouseleave', '.replier-img', function() {
        $('.info-card').hide();
    });

    // 作业通知名片
    $('body').on('mouseenter', '.discuss-info-img', function() {
        showCard($(this), -278);
    });

    $('body').on('mouseleave', '.discuss-info-img', function() {
        $('.info-card').hide();
    });

    // 学生列表名片
    $('body').on('mouseenter', '.stu-list-img', function() {
        showCard($(this), -278);
    });

    $('body').on('mouseleave', '.stu-list-img', function() {
        $('.info-card').hide();
    });

    // 校长 老师列表
    $('body').on('mouseenter', '.teacher-img', function() {
        showCard($(this), -278);
    });

    $('body').on('mouseleave', '.teacher-img', function() {
        $('.info-card').hide();
    });

    $('body').on('click', '.info-card-send', function() {
        var userid = $(this).attr('target-id');
        $('.alert-bg').show();
        $('.info-card').hide();
        $('.letter-input').val('');
        $('.letter-title').text('发送私信给 ' + $(this).prevAll('.info-card-content').find('div').eq(0).text());
        $('.letter-container').show();

        $('.letter-release').unbind('click');
        $('.letter-release').on('click', function() {
            sendMessage(userid, $('.letter-input').val());
        });
    });

    $('.letter-close').on('click', function() {
        $('.alert-bg').hide();
        $('.letter-container').hide();
    });

    $('body').on('click', function() {
        $('.info-card').hide();
    });
});

// 名片
function getOffset(e) {
    var x = 0;
    var y = 0;
    var position = {};
    var wwidth = document.documentElement.clientWidth;
    var wheight = document.documentElement.clientHeight;
    var offsetTop = 0;
    if (!!window.ActiveXObject || "ActiveXObject" in window) {
        offsetTop = document.documentElement.scrollTop;
    } else {
        //在firefox下会出现名片不跟随的现象,经排查,是$('body').scrollTop()在firefox上无效.在这里加条判断.
        if (navigator.userAgent.indexOf('Firefox') >= 0){
            offsetTop = document.documentElement.scrollTop;
        }
        else
        {
            offsetTop = $('body').scrollTop();
        }
//        offsetTop = $('body').scrollTop();
    }
    x += e.offset().left;
    y += e.offset().top;
    if ((y + 170) > (wheight + offsetTop)) {
        position = {
            tleft: x,
            ttop: (wheight + offsetTop - 170)
        }
    } else if (y < offsetTop) {
        position = {
            tleft: x,
            ttop: offsetTop
        }
    } else {
        position = {
            tleft: x,
            ttop: y
        }
    }
    return position;
}

function showCard(obj, pleft) {
    var userid = obj.attr('target-id');
    var role = obj.attr('role');
    var po = getOffset(obj);
    var target = $('.info-card');
    target.removeClass('student');
    target.removeClass('teacher');
    target.removeClass('master');
    target.removeClass('parent');
    target.removeClass('other');
    if (role == 0) target.addClass('student');
    else if ((role == 1) || (role == 6) || (role == 5)) target.addClass('teacher');
    else if (role == 2 || role == 9) target.addClass('master');
    else if (role == 4) target.addClass('parent');
    else target.addClass('other');
    target.css({
        "left": po.tleft + pleft,
        "top": po.ttop
    });
    getCardInfo(userid, target);
}

function getCardInfo(userid, target) {

    /*
    $.ajax({
        url: '/reverse/getPersonalCardInfo.action',
        type: 'POST',
        dataType: 'json',
        data: {
            userid: userid
        },
        success: function(data) {
            target.empty();
            var html = '';
//            if (data.role == 0) {
                html += '<div style="position:relative;height:160px;overflow:hidden;">';
//            }
            html += '<div class="info-card-person"><img class="info-card-img" src="' + data.imageUrl + '"/>';
            html += '<div class="info-card-content"><div>' + data.userName + '</div>';
            html += '<div>' + data.schoolName + ' ' + data.positionName + '</div></div>';
            html += '<div class="info-card-send" target-id="' + data.userid + '">发送私信给ta</div></div>';
            if ((data.role == 0) || (data.role == 4) || (data.role == 1) || (data.role == 6) || (data.role == 2) || (data.role == 9)) {
                html += '<div class="card-student-detail">';
                if (data.petname == null) {
                    html += '<div class="card-student-petname ellipsis" title="'+ data.userName +'">' + data.userName + '的成长树</div>';
                } else {
                    html += '<div class="card-student-petname ellipsis" title="'+ data.userName +'">' + data.petname + '</div>';
                }
                html += '<div class="card-student-exp">经验值：' + data.experiencevalue + '</div></div>';
                //非当前宠物集合
                html += '<div class="pets-area"><ul>';
                
                if(data.petimageList){
                	petlist = data.petimageList;
                	var showlength = petlist.length;
                	if(petlist.length > 4){
                		showlength = 4;
                	}
                    for(var i = 0; i < showlength; i++){
                    	html += '<li><img src="'+ petlist[i] +'" class="small-pets"/></li>';
                    }
                }
                
                
                html +=	'</ul></div>';
                //当前宠物
                if (data.experiencevalue >= 0 && data.experiencevalue < 50) {
                    html += '<img class="card-student-pet" src="/img/tree-1.png"/>';
                } else if (data.experiencevalue >= 50) {
                    html += '<img class="card-student-pet" src="'+data.petimage+'"/>';
                }
                html += '</div>';
               
            } 
            target.append(html);
            target.show();
        }
    });

    */
}

function sendMessage(userid, message) {
    var pm = $.trim(message);

    if (pm == '') {
        ShowPromptDialog("请输入回复内容。");
        return;
    }
    $.ajax({
        url: '/letter/add.do',
        type: 'post',
        dataType: 'json',
        data: {
            recipient: userid,
            message: message
        },
        success: function(data) {
            if (data.status == 'ok') {
                $('.send-result-bg').show();
                $('.send-result').show();
                $('.send-result-bg').fadeOut(3000);
                $('.send-result').fadeOut(3000);
            } else {
                alert(data.errorMessage);
            }
        },
        error: function() {
            console.log('addPrivateLetter error');
            alert('发送失败');
        },
        complete: function() {
            $('.alert-bg').hide();
            $('.letter-container').hide();
        }
    });
}