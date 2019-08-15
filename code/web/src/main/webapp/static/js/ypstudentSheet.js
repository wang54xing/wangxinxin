var currentPageID = 0;
var currentPage = 1;
var audioUploadAction = 'audiosave.jsp?userId=' + '${currentUser.id}' + '&voicetype=0';
var nb_role = '${session.currentUser.role}';

function getstudentInfo() {
    var ret = [];

    return ret;
}
///user/studenttopfive/
function getStudentRank(async) {
    var ret = [];
    async = typeof async == 'undefined' ? false : true;
    $.ajax({
        url: "/user/studenttopfive",
        type: "get",
        dataType: "json",
        async: async,
        data: {

        },
        success: function(data) {
            ret = data;
            showStudentRank(data);
        },
        complete: function() {}
    });
    return ret;
}

function showStudentRank(data) {
    var target = $('#fc_stu_rank');
    var tmp = document.getElementById('fc_stu_rank_all');
    if (!tmp && typeof(tmp) != "undefined" && tmp != 0) {

    } else {
        div.parentNode.removeChild(div);
    }
    if (data.rows) {
        for (var i in data.rows) {
            var html = '';
            var r = data.rows[i];
            html += '<div id="fc_stu_rank_all" class="fc_stu_rank_all" style="border-bottom:1px solid #D0D0D0;"><p><img class="fc_stu_img" role="0" target-id="' + r.id + '" src="' + r.minImageURL + '" ></p>';
            html += '<div class="info-card"></div>';
            html += '<span style="margin-right:28px;margin-left:15px;width:48px;">' + r.userName + '</span>';
            html += '<span style="margin-right:10px;">经验值</span><span class="exprience" style="width:28px;text-align:center;">' + r.experiencevalue + '</span><p></p></div>';
            target.append(html);
        }
    }
}

function getCourses(page, size, type, async) {
    var ret = [];
    async = typeof async == 'undefined' ? false : true;
    $.ajax({
        url: "/reverse/getStudentCourseList.action",
        type: "post",
        dataType: "json",
        async: async,
        data: {
            'page': page,
            'pageSize': size,
            endType: type,
            pageType: 1
        },
        success: function(data) {
            ret = data;
            showCourses(data);
        },
        complete: function() {}
    });
    return ret;
}

function showCourses(data) {
    var html = '';
    if (data.rows) {
        for (var i = 0; i < data.rows.length; i++) {
            var co = data.rows[i];
            //              var cimg=co.instype==2?'/img/cloud_course.png':co.imageUrl;
            var cimg = co.videoNum == 0 ? '/img/filecourse_newest.png' : co.imageUrl;
            if (i % 2 == 0) {
                html += '<div class="course_detail" style="padding-right:10px;">';
            } else {
                html += '<div class="course_detail">';
            }
            html += '<a href="/courseview/' + co.id + '"><img src="' + cimg + '" /></a>';
            html += '<span class="course_des ellipsis">' + co.courseName + '</span></div>';
        }
    }
    var tdiv = $('#fc_course_list');
    tdiv.html(html);
}


/* 获取讨论列表  ********************************************/
function getDiscusses(cid, classtype, page, size) {
    $('#discuss_list').html('');
    $('#discuss_list').addClass('hardloadingb');
    $.ajax({
        url: "/reverse/selTeacherMessageInfo.action",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'classId': cid,
            'classtype': classtype,
            'mesgtype': $('.chosen').attr('index'),
            'page': page,
            'pageSize': size
        },
        success: function(data) {
            ret = data;
            showDiscusses(data, page);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginator(totalPages);
            }
        },
        complete: function() {
            $('#discuss_list').removeClass('hardloadingb');
            if (!ret.rows || ret.rows.length == 0) {
                $('#example').hide();
            } else {
                $('#example').show();
            }
            $('#reply_section').hide();
            $("#reply_list").hide().html('');
        }
    });
}

function showDiscusses(data, cpage) {
    var target = $('#discuss_list');
    var html = '';
    if (data.rows) {
        var txt = $(".chosen").attr('index');
        txt = txt == 1 ? '通知' : '作业';
        for (var i in data.rows) {
            var ds = data.rows[i];
            var lastreply = "";
            if (ds.time) {
                var lastreplyTime = getAdaptTimeFromNow(ds.time);
                lastreply = '<span style="margin-left:15px;">最后回复' + lastreplyTime + '</span>';
            }
            var isbold = 'normal';
            if (!ds.isread) {
                isbold = 'bold';
            }
            var viewtxt = ds.viewnum == 0 ? '' : '已回复' + ds.viewnum + '条';
            var attach = '';
            if (ds.isvoice) {
                attach += '<img src="/img/mic_work.png" style="margin-left:10px;"/>';
            }
            if (ds.isfile) {
                attach += '<img src="/img/fileattach.png" style="margin-left:10px;"/>';
            }
            html += '<div class="discuss-info" style="cursor:pointer;" msgid="' + ds.id + '"><img class="discuss-info-img" src="' + ds.minImageURL + '" target-id="' + ds.userid + '" role="' + ds.role + '"/>';
            html += '<div class="info-card"></div>'
            html += '<p style="margin-bottom:5px;font-weight:' + isbold + ';word-break: break-all;" onclick="getDiscussDetail(' + ds.id + ',' + cpage + ');"><a style="margin-right:10px;">[' + txt + ']</a>';
            html += ds.mesgname + attach + '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.nickName + '</span>';
            html += '<span>于' + ds.createtime + '发表</span>' + lastreply + '<span style="margin-left:10px;">' + viewtxt + '</span></p>';
            html += '</div>';
        }
    }
    target.html(html);
}

function resetPaginator(totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
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
            getDiscusses($('#classes span.class_selected').attr('cid'), $('#classes span.class_selected').attr('classtype'), page, 8);
        }
    });
}

function resetPaginatorRe(totalPages) {
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: 1,
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
            getReplyList($("#discuss_list >div:nth(1)").attr('msgid'), page, 8);
        }
    });
}


function resetSubmitForm() {
    $('#reply_section input[type=text]').val('');
    $('#reply_section textarea').val('');
    $('.uploadedFiles').remove();
    $('.play_button').hide();
    $('#upload-photo-container').empty();
    if (FWRecorder.recorder) {
        FWRecorder.defaultSize();
        $("#save_button object").css('opacity', 0);
    }
}

/**** 提交回复  ***************************************************/
function submitReply() {
    var content = $('#reply_section textarea').val();

    var flist = '';

    $('.uploadedFiles').each(function() {
        var localname = $(this).text();
        flist += $(this).attr('realname') + ',' + localname + ';';
    });

    for (var i = 0; i < $('.img-photo-take').length; i++) {
        var localname = '拍摄照片' + i;
        flist += $('.img-photo-take').eq(i).attr('realname') + ',' + $('.img-photo-take').eq(i).attr('textname') + ';';
    }

    if (content.length > 0) {
        $.ajax({
            url: "/reverse/addMesgReplyInfo.action",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                'homeworkcontent': content,
                'mesgid': $("#discuss_list >div:nth(1)").attr('msgid'),
                'filenamelist': flist
            },
            success: function(data) {
                ret = data;
                getDiscussDetail($("#discuss_list >div:nth(1)").attr('msgid'));
            },
            complete: function() {
                $('#submitloading').hide();
                resetSubmitForm();
            }
        });
    } else {
        alert("请输入内容！");
    }
}

/*  显示通知&作业详细  **************************************/
function getDiscussDetail(mid, cpage) {
    $('#discuss_list').html('');
    $('#discuss_list').addClass('hardloadingb');
    $.ajax({
        url: "/reverse/selMessageDetailInfo.action",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'messageId': mid
        },
        success: function(data) {
            ret = data;
            showDiscussDetail(data, mid, cpage);
        },
        complete: function() {
            $("#uploadForm").attr('action', audioUploadAction + '&mesgid=' + mid);
            if ($(".chosen").length > 0 && $(".chosen").attr('index') == 2) {
                $('#reply_section').show();
                getReplyList(mid, 1, 8);
                $('#example').show();
            } else {
                $('#reply_section').hide();
                $('#example').hide();
            }
            getNotifyNum($('#classes span.class_selected').attr('cid'), $('#classes span.class_selected').attr('classtype'), $('#classes span.class_selected').attr('ctype'));
            $("#reply_list").html('').show();
            $('#discuss_list').removeClass('hardloadingb');
        }
    });
}

function showDiscussDetail(data, mid, cpage) {
    var target = $('#discuss_list');
    var html = '';
    var ds = '';
    var cid = $('#classes span.class_selected').attr('cid');
    var classtype = $('#classes span.class_selected').attr('classtype');
    html += '<div style="cursor:pointer;" onclick="getDiscusses(' + cid + ',' + classtype + ',' + cpage + ',8);"><a>返回></a></div>';
    if (data.fzMesgDetailInfo) {
        var txt = $(".chosen").attr('index');
        txt = txt == 1 ? '通知' : '作业';

        ds = data.fzMesgDetailInfo;
        html += '<div class="discuss-info" msgid="' + ds.id + '"><img class="discuss-info-img" src="' + ds.minImageURL + '" target-id="' + ds.userid + '" role="' + ds.role + '"/>';
        html += '<div class="info-card"></div>';
        html += '<p style="margin-bottom:5px;word-break: break-all;"><a style="margin-right:10px;">[' + txt + ']</a>';
        html += ds.mesgname + '</p><p style="margin-top:5px"><span style="margin-right:10px;">' + ds.nickName + '</span>';
        html += '<span>于' + ds.createtime + '发表</span></p>';
        html += '</div>';
    }

    if (data.attachmentInfoList) {
        html += '<div style="border-bottom:none;"><p style="margin-bottom:20px;text-indent:2em;">' + ds.mesgcontent + '</p>';
        if (ds.voicename) {
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a onclick="PlayedMusic($(this),' + ds.id + ');" url="' + ds.voicename + '"><img src="/img/yuyin.png">播放</a></p>';
        }
        for (var i in data.attachmentInfoList) {
            var at = data.attachmentInfoList[i];
            html += '<p style="margin-left:30px;margin-bottom:10px;"><a href="/reverse/FileDownload?fileName=' + encodeURI(encodeURI(at.pathname)) + '"><img src="/img/fileattach.png"/>附件' + (parseFloat(i) + 1) + ':' + at.name + '</a></p>';
        }
        html += '</div>';
    }
    target.html(html);
}

/***  获取回复列表    ********************/

function getReplyList(mid, page, size) {
    $("#reply_list").addClass('hardloadingb');
    $("#reply_list").show();
    $.ajax({
        url: "/reverse/selStuOrTeacherTaskInfo.action",
        type: "get",
        dataType: "json",
        async: true,
        data: {
            'mesgid': mid,
            'page': page,
            'pageSize': size
        },
        success: function(data) {
            ret = data;
            showReplyList(data);
            var total = data.rows.length > 0 ? data.total : 0;
            var to = Math.ceil(total / size);
            totalPages = to == 0 ? 1 : to;
            if (page == 1) {
                resetPaginatorRe(totalPages);
            }
            if (total > 0) {
                $('#example').show();
            } else {
                $('#example').hide();
                $("#reply_list").hide();
                $('#discuss_list >div:nth(2)').css('border-bottom', '');
            }
        },
        complete: function() {
            $("#reply_list").removeClass('hardloadingb');
        }
    });
}

function showReplyList(data) {
    var target = $('#reply_list');
    var html = "";
    if (data.rows) {
        for (var i in data.rows) {
            var hf = data.rows[i];
            var timeStr = getAdaptTimeFromNow(hf.createtime);

            html += '<div style="position:relative;"><div class="reply_detail"><div style="margin-right:15px;"><img src="' + hf.minImageURL + '" style="width:45px;height:45px;"/></div>';
            html += '<div><p><span style="font-size: 16px;color:#696969;margin-right:20px;">' + hf.nickName + '</span><span style="color:#9a9a9a;">发表于' + timeStr + '</span></p>';
            if (hf.isview == 1) {
                html += '<p>' + hf.homeworkcontent + '</p>';
                if (hf.voicename) {
                    html += '<p><a onclick="PlayedMusic($(this),' + hf.id + ');" url="' + hf.voicename + '"><img src="/img/yuyin.png">播放</a></p>';
                }
                for (var j in hf.fileList) {
                    var file = hf.fileList[j];
                    html += '<p><a href="/reverse/FileDownload?fileName=' + encodeURI(encodeURI(file.pathname)) + '"><img src="/img/fileattach.png" />附件' + (parseFloat(j) + 1) + '：' + file.name + '</a></p>';
                }
            } else {
                html += '<p>该作业已提交！</p>';
            }
            html += '</div><a class="delreply" onclick="delReply(' + hf.id + ');"><img candel="' + hf.ismytask + '" src="img/dustbin.png" title="删除" style="display:none;position:absolute;top:25px;right:25px;"/></a>';
            html += '</div><a style="padding:2px 5px;position:absolute;top:0px;right:25px;background:rgb(225,225,225);">' + hf.floor + '#</a></div>';
        }
    }
    target.html(html);

    $('#reply_list >div').hover(function() {
        var ob = $(this).find('.delreply img');
        if (ob.attr('candel') == 1 && nb_role != 4) {
            $(this).find('.delreply img').show();
        }

    }, function() {
        $(this).find('.delreply img').hide();
    });
}

function delReply(id) {
    if (confirm('确定删除这条记录？')) {
        $.ajax({
            url: "/reverse/delStuTaskInfo.action",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                'mesgreplyid': id,
            },
            success: function(data) {
                ret = data;
                getDiscussDetail($("#discuss_list >div:nth(1)").attr('msgid'));
            },
            complete: function() {}
        });
    }
}

$(function() {
    $('.loading').show();
    getstudentInfo();
    getCourses(1, 12, 1);
    getStudentRank();
    $('#classes >div').width($('#classes span').length * 123).find('span:nth(0)').addClass('class_selected');
    $(".class_item").each(function(i) {
        $(this).bind('click', function() {
            $(this).siblings().removeClass('class_selected');
            $(this).addClass('class_selected');
            var cid = $(this).attr('cid');
            var classtype = $(this).attr('classtype');
            var coursetype = $(this).attr('ctype');
            resetSubmitForm();
            getStudentRank(true, cid);
            getCourses(1, 12, 1);
            getNotifyNum(cid, classtype,coursetype);
            $(".my_notice").click();
            currentClassId = cid;
        });
    });
    /* 作业通知切换 */
    $(".tab_button").each(function(i) {
        if (i == 0) {
            $(this).addClass('chosen');
        } else {
            $(this).addClass('notchosen');
        }
        $(this).bind('click', function() {
            if (!$(this).hasClass('chosen')) {
                $(this).addClass('chosen').removeClass('notchosen');
                $(this).find('span').removeClass('notity_b');
            }
            $(this).siblings().removeClass('chosen').addClass('notchosen');
            $(this).siblings().find('span').addClass('notity_b');
            resetSubmitForm();
            var cid = $('#classes span.class_selected').attr('cid');
            var classtype = $('#classes span.class_selected').attr('classtype');
            var coursetype = $('#classes span.class_selected').attr('ctype');
            getNotifyNum(cid, classtype,coursetype);
            if (i == 3) {
                getStudentExamPaper(1,8);
                $('.answer-sheet').hide();
                $('.mycomment-container').hide();
                $('.friend-container').hide();
                $('#submit_section').hide();
                $('.newexam_container').show();
                $('#discuss_list').hide();
                $('.exam_list').show();
                $('.myexam_bar>span').text("我的考试");
                $('.newexam_form').hide();
                $('.viewexam-container').hide();
                $('.correctexam-container').hide();
                $('.one_blog_container').hide();
                $('#example').hide();
                $('#reply_list').hide();
                $('#reply_section').hide();
            } else if (i == 0) {
                $('.answer-sheet').hide();
                $('#reply_section').hide();
                $('.mycomment-container').hide();
                $('.friend-container').show();
                $('#submit_section').hide();
                $('#discuss_list').hide();
                $('.newexam_container').hide();
                $('.order-bar .active').click();
                $('.one_blog_container').hide();
            } else {
                getDiscusses(cid, classtype, 1, 8);
                $('#submit_section').show();
                $('.newexam_container').hide();
                $('#discuss_list').show();
                $('.friend-container').hide();
                $('.mycomment-container').hide();
                $('.one_blog_container').hide();
                $('.answer-sheet').hide();
            }
        });
    });


    if ($('#classes span').length < 5) {
        $("#move_right").remove();
        $("#move_left").remove();
    } else {
        $('#classes >div').width($('#classes span').length * 123 + ($('#classes span').length - 1) * 20).find('span:nth(0)').addClass('class_selected');
    }
    $('#classes >div span:not(:last-child)').css('margin-right', '20px');

    /*  班级导航左右滑动 ***********************************/
    $("#move_right").bind('click', function() {
        if (typeof $("#classes").data("left") == 'undefined') {
            $("#classes").data("left", 0);
        }
        var left = $("#classes").data("left");
        var l = $('#classes span').length;
        if ((l - 4) > Math.abs(left / 123)) {
            if ($("#classes").data("left")) {
                left = $("#classes").data("left") - 123;
            } else {
                left -= 123;
            }
            $("#classes").data("left", left);
            $("#classes div").animate({
                left: left + 'px'
            }, "slow");
        } else {
            $("#classes >div").effect('shake', {}, 300);
        }
    });

    $("#move_left").bind('click', function() {
        if (typeof $("#classes").data("left") == 'undefined') {
            $("#classes").data("left", 0);
        }
        var left = $("#classes").data("left");
        if (left < 0) {
            if ($("#classes").data("left")) {
                left = $("#classes").data("left") + 123;
            } else {
                left += 123;
            }

            $("#classes").data("left", left);
            $("#classes div").animate({
                left: left + 'px'
            }, "slow");
        } else {
            $("#classes >div").effect('shake', {}, 300);
        }
    });


    /*  上传附件   ****************************************/
    $("#faf input").bind('change', function() {
        if ($(this).val().length > 0) {
            $('#fileuploadLoading').show();
            fileUpload($('#faf')[0], function(content) {
                if (content) {
                    var ob = $.parseJSON($(content).text());
                    if (ob.uploadType == 0) {
                        alert("附件上传失败！");
                    }
                    // $('#file_attach').attr('realname',ob.realname);
                    var uf = $('<p style="padding:5px 0;cursor:pointer;" class="uploadedFiles" realname="' + ob.realname + '">' + $('#file_attach').val().match(/[^\\]*$/)[0] + '</p>').hover(function() {
                        if ($(this).find('img').length == 0) {
                            $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");

                        } else {
                            $(this).find('img').show();
                        }
                    }, function() {
                        $(this).find('img').hide();
                    });
                    $("#reply_section").append(uf);
                }
                /*  $('#file_attach').val(''); */
                $('#fileuploadLoading').hide();
            });
        }
    });

    getNotifyNum($('#classes span.class_selected').attr('cid'), $('#classes span.class_selected').attr('classtype'),$('#classes span.class_selected').attr('ctype'));

    $(".tab_button:nth(0)").click();
    //getDiscusses('${session.classId}',1,8);

    $('#example').bootstrapPaginator({
        currentPage: 1,
        totalPages: 1,
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
        onPageClicked: function(e, originalEvent, type, page) {}
    });

    $('.loading').fadeOut(1000);

    // 拍照
    webcam.set_quality(90);
    webcam.set_shutter_sound(true);
    webcam.set_hook('onComplete', 'uploadPhoto');

    $('.not-found-close').on('click', function() {
        $('.not-found-camera').hide();
        $('.alert-bg').hide();
    });

    $('.take-photo').on('click', function() {
        $('.alert-bg').show();
        $('.take-photo-container').show();
        $('.take-photo-btn').show();
    });

    $('.take-photo-btn').on('click', function() {
        webcam.freeze();
        $('.take-photo-btn').hide();
        $('.take-photo-again').show();
        $('.take-photo-upload').show();
    });
    $('.take-photo-again').on('click', function() {
        webcam.reset();
        resetPhoto();
    });

    $('.take-photo-upload').on('click', function() {
        webcam.dump();
        $('.photo-close').click();
    });

    $('.photo-close').on('click', function() {
        $('.alert-bg').hide();
        $('.take-photo-container').hide();
        resetPhoto();
    });

    $('#upload-photo-container').on('click', '.delete-photo', function() {
        $(this).parent().remove();
    });
});

function resetPhoto() {
    $('.take-photo-btn').show();
    $('.take-photo-again').hide();
    $('.take-photo-upload').hide();
}

function uploadPhoto(base64) {
    var target = $('#upload-photo-container');
    src = 'data:image/jpeg;base64,' + base64;
    $.ajax({
        url: '/commonupload/base64image.do',
        type: 'post',
        dataType: 'json',
        data: {
            base64ImgData: base64
        },
        success: function(data) {
            console.log(data);
            target.append('<div class="upload-photo-img"><img class="img-photo-take" src="' + src + '" realname="' + data.path + '" textname="'+data.name+'"><img class="delete-photo" src="/img/error-grey.png"></div>');
        },
        error: function() {
            console.log('convertImages error');
        }
    });
    webcam.reset();
}