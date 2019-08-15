/**
 * Created by NiuXin on 14-5-21.
 * 私信模块
 */
var Itemindex = 0;
$(function(){
    $('body').on('click','.contact-list-index',function() {
        var belong=$(this).attr('data-belong');
        if($(this).hasClass('active')) {
            $('.'+belong).css('display', 'none');
            $(this).removeClass('active');
        }else {
            $('.'+belong).css('display', 'block');
            $(this).addClass('active');
        }
    });
});

function showDialog1(selector) {
    $(selector).fadeIn();
    $('#bg').fadeIn();
}

function closeDialog1(selector) {
    $(selector).fadeOut();
    $('#bg').fadeOut();
}

function SendPM() {
    var recipient = $.trim($('#recipient').val());
    var pm = $.trim($('#pm-content').val());

    if (recipient == '' || pm == '') {
        ShowPromptDialog("请输入联系人和私信内容。");
        return;
    }
    MessageBox('发送中...', 0);
    $.ajax({
        url: "/letter/add.do",
        type: "post",
        data: {
            'recipient': recipient,
            'message': pm
        },
        success: function(data) {
            if (data.status == 'error') {
                MessageBox(data.errorMessage, -1);
            } else {
                closeDialog1("#sendPM");
                $('#recipient').val(recipient);
                $('#pm-content').val('');
                MessageBox('发送成功', 1);

                setTimeout(function() {
                    location.href = 'message'
                }, 1500);
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
}

function GetPMList(page) {
    var tpage = page || 1;
    var totalPage;
    $.ajax({
        url: "/letter/list.do",
        type: "get",
        dataType: "json",
        data: {
            page: tpage
        },
        success: function(data) {
            $('#message-list-container').empty();
            $('#message-list-container').removeClass('hardloadingb');
            if (typeof data.rows == 'undefined' || !data.rows.length) {
                $('#message-list-container').html('<p style="margin-top: 20px">暂无私信。</p>');
                return;
            }
            for (var i in data.rows) {
                var pm = data.rows[i];
                var html =
                    '<div> \
                        <div style="width:80px;height:80px;margin:20px 10px;float:left;background: url(\'' + pm.userImage + '\') no-repeat center center;background-size:80px;"></div>\
                    <div> \
                    <span style="font-family:Microsoft YaHei;font-size:15px;margin-top:18px;color:#ff6600;font-weight:bold;">'
                html += pm.senderName + '</span><span style="float:right;width:15px;height: 15px">';
                if (pm.unread==1 ) {
                    html += '<img src="/img/newsmessage.png" />';
                }
                html += '</span><span style="float:right;margin-top:18px;color:#949494;">' + pm.sendingTime + '</span></div>';
                html += '<a href="/replyMessage?replyId=' + pm.senderId + '"><div style="color:#949494;margin-left:100px;margin-right:10px;margin-top:5px;min-height: 50px;word-wrap: break-word">' + pm.content + '</div></a>';
                html += '<div style="text-align: right;margin-right: 10px;"> \
                    <span style="color:#949494;"><a style="color:#949494;" href="/replyMessage?replyId=' + pm.senderId + '">回复</a> | </span> '
                html += '<a onclick="ShowDeletePMDialog(\'' + pm.senderId + '\',\'' + pm.senderName + '\');"><img style="vertical-align: middle; margin:0 0 2px 3px" src="/img/dustbin.png" /></a> \
                </div> \
                    </div>';
                $('#message-list-container').append(html);
            }

            if (data.total % data.pageSize == 0) {
                totalPage = data.total / data.pageSize;
            } else {
                totalPage = data.total / data.pageSize + 1;
            }
            resetPaginator(data.page, totalPage);
        }
    });
}

function ClearPMList() {
    $.get("/letter/clear.do",
        function() {
            MessageBox("您的私信列表已清空。", 1);
            GetPMList();
        })
    closeDialog1('#clearPM');
    MessageBox("正在清空...", 0);
}

function ShowDeletePMDialog(rid, senderName) {
    $("#PMUserToDel").text(senderName);
    replyID = rid;
    showDialog1('#delPM');
}

function DeleteAllReply() {
    $.ajax({
        url: "/letter/deleteallreply.do",
        type: "get",
        data: {
            'replyId': replyID
        },
        success: function(data) {
            if (data.status == 'ok') {
                MessageBox('成功', 1);
                GetPMList();
            } else {
                //MessageBox(data.errorMessage, -1);
                MessageBox('请求错误。', -1);
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
    closeDialog1('#delPM');
    MessageBox("删除中...", 0);
}

function GetReplyList(tpage) {
    var page = tpage || 1;
    $.ajax({
        url: "/letter/getreplyletters.do",
        type: "get",
        data: {
            'replyId': rid,
            page:page
        },
        dataType: "json",
        success: function(data) {
            recipient = data.recipient;
            $('#recipient-name').text(recipient);
            $("#PMUserToDel").text(recipient);
            $('#message-list-container').empty();
            $('#message-list-container').removeClass('hardloadingb');
            var i = -1;
            for (i in data.rows) {
                var html = '';
                var margin_left = 10,
                    margin_right = 60;
                if (i != 0) {
                    html += '<hr width=680px size=1 color=#c0e2f1 style="margin-left:27px;" />';
                }
                var pm = data.rows[i];
                if (pm.senderName != recipient) {
                    margin_left = 50, margin_right = 10;
                }
                html +=
                    '<div> \
                        <div style="margin-left:' + 'px;width:80px;height:80px;margin:20px 10px 20px ' +margin_left+'px;float:left;background: url(\'' + pm.userImage + '\') no-repeat center center;background-size:80px;"></div>\
                    <div> \
                    <span style="font-family:Microsoft YaHei;font-size:15px;margin-top:10px;color:#ff6600;font-weight:bold;">'
                html += pm.senderName + '</span>';
                html += '<span style="float:right;margin-top:10px;color:#949494;">';
                html += pm.sendingTime + '</span></div>';
                html += '<div style="word-wrap:break-word; color:#949494;margin-right:' + margin_right + 'px;margin-left:' + (margin_left + 100) + 'px;margin-top:5px;min-height: 60px;">' + pm.content + '</div>';
                html += '<div style="margin-right: 10px;text-align: right">';
                html += '<a onclick="ShowDeleteReplyDialog(\'' + pm.letterId + '\');"><img style="vertical-align: middle; margin:0 0 2px 3px" src="/img/dustbin.png" /></a> \
                </div> \
                    </div>';
                $('#message-list-container').append(html);
            }
            if (i >= 0) {
                $('#message-list-container').css('border', '1px solid #c0e2f1');
            } else {
                $('#message-list-container').css('border', '0px');
            }
            if (data.total % data.pageSize == 0) {
                totalPage = data.total / data.pageSize;
            } else {
                totalPage = data.total / data.pageSize + 1;
            }
            resetPaginatorRe(page, totalPage);
        }
    });
}

function ClearReplyList() {
    $.ajax({
        url: "/letter/deleteallreply.do",
        type: "get",
        data: {
            'replyId': rid
        },
        success: function(data) {
            if (data.status == 'ok') {
                MessageBox("删除成功", 1);
                setTimeout(function() {
                    location.href = 'message'
                }, 1500);
            } else {
                //MessageBox(data.errorMessage, -1);
                MessageBox('请求错误。', -1);
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
    closeDialog1('#delPM');
    MessageBox("删除中...", 0);
}

function SendReply() {
    var pm = $.trim($('#pm-content').val());

    if (pm == '') {
        ShowPromptDialog("请输入回复内容。");
        return;
    }

    MessageBox('发送中...', 0);
    $.ajax({
        url: "/letter/reply.do",
        type: "post",
        data: {
            'recipient': rid,
            'message': pm
        },
        success: function(data) {
            if (data.status == 'error') {
                MessageBox(data.errorMessage, -1);
            } else {
                $('#pm-content').val('');
                MessageBox('发送成功', 1);
                GetReplyList();
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
}

function ShowDeleteReplyDialog(id) {
    mid = id;
    showDialog1('#delReply');
}

function DelReply() {
    $.ajax({
        url: "/letter/deletereply.do",
        type: "get",
        data: {
            'pairId': rid,
            'letterId': mid
        },
        success: function(data) {
            if (data.status == 'ok') {
                MessageBox('删除成功', 1);
                GetReplyList();
            } else {
                //MessageBox(data.errorMessage, -1);
                MessageBox('请求错误。', -1);
            }
        },
        error: function(e) {
            MessageBox('服务器响应请求失败，请稍后再试。', -1);
        }
    });
    closeDialog1('#delReply');
    MessageBox("删除中...", 0);
}

function SearchPM() {
    var search_word = $.trim($('#pm-search').val());
    if (search_word == '') {
        return;
    }
    MessageBox("搜索中", 0);
    $.ajax({
        url: "/user/searchPrivateLetterByWord.action",
        type: "post",
        data: {
            word: search_word
        },
        dataType: "json",
        success: function(data) {
        	console.log(data);
            $('#message-list-container').html('<p style="margin: 20px 0">搜索关键字<b style="color: red">' + search_word + '</b>共找到' + data.rows.length + '条私信。</p>');

            for (var i in data.rows) {
                var pm = data.rows[i];
                var html =
                    '<div> \
                        <div style="width:80px;height:80px;margin:20px 10px;float:left;background: url(\'' + pm.userImage + '\') no-repeat center center;background-size:80px;"></div>\
                    <div> \
                    <span style="font-family:Microsoft YaHei;font-size:15px;margin-top:18px;color:#ff6600;font-weight:bold;">'
                html += pm.senderName + '</span><span style="float:right;width:15px;height: 15px">';
                if (!pm.isRead) {
                    html += '<img src="/img/newsmessage.png" />';
                }
                html += '</span><span style="float:right;margin-top:18px;color:#949494;">' + pm.sendingTime + '</span></div>';
                html += '<div style="color:#949494;margin-right:10px;margin-top:5px;min-height: 50px;word-wrap: break-word;margin-left: 100px">' + pm.message + '</div>';
                html += '<div style="text-align: right;margin-right: 10px;"> \
                    <span style="color:#949494;"><a style="color:#949494;" href="business/reverse/user/replyMessage.jsp?replyId=' + pm.replyId + '">回复</a> | </span> '
                html += '<a onclick="ShowDeletePMDialog(' + pm.replyId + ',\'' + pm.senderName + '\');"><img style="vertical-align: middle; margin:0 0 2px 3px" src="/img/dustbin.png" /></a> \
                </div> \
                    </div>';
                $('#message-list-container').append(html);
            }
        },
        complete: function(data) {
            ClosePromptDialog();
        }
    });
}

function GetContactList() {

    var catalog = {
        'studentsList': '学生/同学',
        'teachersList': '老师',
        'presidentList': '校领导',
        'parentsList': '家长',
        'bureauList': '教育局',
        'friendList': '好友'
    };
    $.getJSON('/user/getAddressBookPc.do', function(data) {
        var html = '<span style="display: block; overflow-y: auto; overflow-x: hidden;">';
        for (var list in data) {
            if (catalog[list]) {
                html += '<span class="contact-list-index" data-belong="' + list + '">' + catalog[list] + '</span>';
                var people = data[list];

                for (var i in people) {
                    var person = people[i];
                    html += '<span onclick="AddMsgReceiver(\'' + person.userName + '\',\'' + person.id + '\')" style="cursor: pointer; line-height:30px;display:none;" class="' + list + '">' + person.nickName + '(' + person.userName + ')</span>';
                }
            }
        }
        html += '</span>';
        $('#contact-list').html(html);
    });
}

function AddMsgReceiver(username,userid) {
    var s = $.trim($('#recipient').val());
    s = s.replace(/^;+|;+$/gm, '');

    var reg = new RegExp('(^|;|\\s)' + username + '($|;|\\s)');
    if (reg.test(s)) {
        return;
    }
    if (s != '') s += ';';

    if(window.location.href.indexOf('message') < 0){//投票选举
    	$('textarea#recipient').val(s + username);
    	$('#recipient').append('<span userid="'+ userid +'">'+ ( s + username) + '</span><i class="fa fa-times-circle delete-candidate" onclick="deleteCandidate(this)"></i>');
    	$('.tp-candidate').val(s+ username);
    }else{//发私信
    	$('input#recipient').val(s + username);
    }
}

function resetPaginator(page, totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: page,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {  
            switch (type) {  
                case "first":  
                    return "首页";  
                case "prev":  
                    return "<";  
                case "next":  
                    return ">";  
                case "last":  
                    return "末页"+page;  
                case "page":  
                    return  page;  
            }                 
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            GetPMList(page);
        }
    });
}

function resetPaginatorRe(page, totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: page,
        totalPages: totalPages,
        itemTexts: function (type, page, current) {  
            switch (type) {  
                case "first":  
                    return "首页";  
                case "prev":  
                    return "<";  
                case "next":  
                    return ">";  
                case "last":  
                    return "末页"+page;  
                case "page":  
                    return  page;  
            }                 
        },
        onPageClicked: function(e, originalEvent, type, page) {
            currentPage = page;
            GetReplyList(page);
        }
    });
}

/*var polyv_player;

function playMovie(url) {
    var id = "#intro-player";
    var vc = $(id);
    //var url = 'http://player.polyv.net/videos/1a9bb5bed5a13cb439c3628a0e40bf48_1.swf';
    vc.html('<div id="mainv1a9bb5bed5fb74e2a8e3f4812421ded5_1_div" style="padding:15px 15px 10px;background-color: rgba(255, 255, 255, 0.5);box-shadow: 0 0 10px #666"></div><a class="close-dialog" style="color:black; opacity:0.7;position:absolute;top:3px;right:1px;"><i class="fa fa-times-circle fa-lg"></i></a>');
    if (!polyv_player) {
        polyv_player = new polyvObject.swf(url, "mainv1a9bb5bed5fb74e2a8e3f4812421ded5_1", "800", "450", "#ffffff");
    } else {
        polyv_player.source = url;
        polyvObject.writePlayer(polyvObject.players[0]);
    }
    vc.fadeIn();
    $('.close-dialog').on('click', function() {
        vc.empty();
        vc.fadeOut();
    });
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