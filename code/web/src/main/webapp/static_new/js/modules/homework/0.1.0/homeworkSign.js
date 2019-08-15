'use strict';
define(['jquery', 'doT', 'easing', 'common', 'pagination', 'sharedpart', 'uploadify', 'fancybox_seajs', "layer"], function (require, exports, module) {
    /**
     *初始化参数
     */

    var homeworkSign = {},
        Common = require('common');
    require('uploadify');
    require('fancybox_seajs');
    require('pagination');
    require('layer');
    var homeworkSignData = {};
    var someFileFailed = false;
    var userName = "";
    var uid = "";
    homeworkSign.init = function () {
        Common.getData('/homework/getUid.do', "", function (rep) {
            uid = rep.message;
        })
        $("body").on("click", ".ctt", function () {
            var par = {};
            par.id = $(this).attr("ids");
            if (layer.confirm('确认删除！', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    Common.getData('/homework/deleteCom.do', par, function (rep) {

                    })
                    $("#count").html(parseInt($("#count").html())-1);
                    layer.closeAll('dialog');
                    homeworkSign.getCommentInfo($('#hwId').val(), 1);
                })) {
            }
        })

        jeDate({
            dateCell: "input.datep",
            format: "YYYY-MM-DD",
            minDate: "2015-09-19",
            isinitVal: true,
            isDisplay: true,
            displayCell: ".discls",
            isTime: false,
            festival: false, //显示节日
            trigger: "click",
            choosefun: function (date) { //选择日期完毕的回调
                homeworkSign.selHomework(date, 1);
            }
        })
        homeworkSign.uploadVideo();
        homeworkSign.uploadImage();
        homeworkSign.uploadImag();
        $('.monthprev').click(function () {
            var date = $('.jedateyear').attr('data-year') + "-" + $('.jedatemonth').attr('data-month') + "-01";
            homeworkSign.selHomework(date, 1);
        });
        $('.monthnext').click(function () {
            var date = $('.jedateyear').attr('data-year') + "-" + $('.jedatemonth').attr('data-month') + "-01";
            homeworkSign.selHomework(date, 1);
        });
        $(".player-close-btn").click(function () {
            $('.player-container').hide();
        });
        homeworkSignData.page = 1;
        homeworkSignData.pageSize = 10;
        $('.t-I').addClass('homework-li');
        $('.t-II').removeClass('homework-li');
        $('.sign').click(function () {
            homeworkSign.selHomeworkSignList(1);
        });
        $('.prev-a-list').click(function () {
            homeworkSign.selHomework($('#selDate').val(), 1);
            $('.right-I').show();
            $('.right-II').hide();
            $('.right-III').hide();
        });
        $('.prev-a-detail').click(function () {
            homeworkSign.getHomeworkInfo($(this).attr('hwid'));
            homeworkSign.getCommentInfo($(this).attr('hwid'), 1);
            $('.right-I').hide();
            $('.right-II').show();
            $('.right-III').hide();
        });

        $('.addcomment').click(function () {
            homeworkSign.addcomment();
        });
        $('.homework-right li').click(function () {
            $(this).addClass('homework-li').siblings().removeClass('homework-li')
        })
        homeworkSign.selHomework(getNowFormatDate(), 1);
    }

    homeworkSign.addcomment = function () {
        var imgs = "";
        var types = "";
        var names = "";
        var paths = "";
        homeworkSignData.id = $('#hwId').val();
        homeworkSignData.comment = $('#comment').val();
        if ($('#comment').val() == '') {
            layer.alert("回复不能为空！");
            return;
        }
        $('.work-sign-ul li').each(function (i) {
            names += $(this).attr('nm') + ",";
            types += $(this).attr('tp') + ",";
            paths += $(this).attr('pth') + ",";
            imgs += $(this).attr('img') + ",";
        });
        homeworkSignData.names = names;
        homeworkSignData.types = types;
        homeworkSignData.paths = paths;
        homeworkSignData.imgs = imgs;
        Common.getPostData('/homework/addComment.do', homeworkSignData, function (rep) {
            if (rep.code == '200') {
                layer.msg("评论成功！");
                $('#comment').val('');
                $('.work-sign-ul').empty();
                homeworkSign.getCommentInfo($('#hwId').val(), 1);
                homeworkSign.getHomeworkInfo($('#hwId').val());

            } else {
                layer.msg("评论失败！");
            }
        });
    }
    homeworkSign.getCommentInfo = function (id, page) {
        homeworkSignData.id = id;
        homeworkSignData.page = page;
        homeworkSignData.pageSize = 6;
        Common.getPostData('/homework/getComments.do', homeworkSignData, function (rep) {
            if (rep.code == '200') {
                $('.text-li').empty();
                if (rep.message.rows != null && rep.message.rows.length != 0) {
                    var str = "";
                    for (var i = 0; i < rep.message.rows.length; i++) {
                        var data = rep.message.rows[i];
                        var files = "<div class='sign-listt'>";

                        if (data.fileDtos != null && data.fileDtos.length != 0) {
                            for (var j = 0; j < data.fileDtos.length; j++) {
                                var file = data.fileDtos[j];
                                if (file.type == 1) {
                                    var img = file.filePath + "?imageView/1/h/80/w/80";
                                    files += '<a class="fancybox" href="' + file.filePath + '"data-fancybox-group="home" title="预览"><img class="content-img" title="点击查看大图" src="' + img + '"></a>';
                                } else if (file.type == 2) {
                                    var img = file.imageUrl + "?imageView/1/h/80/w/80";
                                    files += '<div class="sign-videoo"><img class="content-img videoshow2" vurl="' + file.filePath + '" src="' + img + '"><img src="/img/play.png" class="video-play-btn" url="' + file.filePath + '" onclick="tryPlayYCourse(\'' + file.filePath + '\')"></div>';
                                } else if (file.type == 9) {
                                    var img = file.filePath + "?imageView/1/h/80/w/80";
                                    files += '<a class="hom-a" href=' + file.filePath + '>'+file.name+'</a>';
                                }
                                else {
                                    files += '<a class="voice" onclick="playVoice(\'' + file.filePath + '\')" url="' + file.filePath + '" style="cursor: pointer;"><img src="/img/yuyin.png" style="width:160px;height:22px;"><i class="i-play">播放</i></a>';
                                }
                            }
                        }

                        files += "</div>";
                        str += '<li class="pos"><img class="sign-imm" src="' + rep.message.rows[i].userImage + '"><span class="pos-a">' + rep.message.rows[i].role + '</span><div class="sign-cla w600"><em class="mt10">[' + rep.message.rows[i].userName + ']</em><i class="i-im i-p ctt" uid=' + rep.message.rows[i].userId + ' ids=' + rep.message.rows[i].id + '></i><br><span class="sign-time">' + rep.message.rows[i].time + '</span><br><p class="p-t">' + rep.message.rows[i].content + '</p>' + files + '</div></li>';
                    }
                    $('.fancybox').fancybox();
                    $('.text-li').append(str);
                    $('.page-list2').hide();
                    $('.page-list1').hide();
                    $('.page-list3').show();
                    $('.page-list3').jqPaginator({
                        totalPages: Math.ceil(rep.message.count / rep.message.pageSize) == 0 ? 1 : Math.ceil(rep.message.count / rep.message.pageSize),//总页数
                        visiblePages: 6,//分多少页
                        currentPage: parseInt(page),//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (n != page) {
                                page = n;
                                homeworkSign.getCommentInfo(id, page);
                            }
                        }
                    });
                }
            }
        });
        $(".ctt[uid="+uid+"]").show();
    }

    homeworkSign.selHomework = function (selDate, page) {
        homeworkSignData.time = selDate;
        homeworkSignData.page = page;
        homeworkSignData.pageSize = 6;
        Common.getPostData('/homework/selHomeworks.do', homeworkSignData, function (rep) {
            if (rep.code == '200') {
                $('.right-I').show();
                $('.right-II').hide();
                $('.right-III').hide();
                $('#selDate').val(selDate);
                var flg = false;
                var timeAry = rep.message.times;
                if (timeAry != null && timeAry.length != 0) {
                    for (var i = 0; i < timeAry.length; i++) {
                        if (selDate == timeAry[i]) {
                            flg = true;
                        }
                    }
                }
                if (flg) {
                    $('.cur-green').show();
                    $('.cur-red').hide();
                    $('.cur-stu').hide();
                } else {
                    $('.cur-green').hide();
                    if ($('#roleName').val() == 'stu' || $('#roleName').val() == 'par') {
                        $('.cur-red').hide();
                        $('.cur-stu').show();
                    } else {
                        $('.cur-red').show();
                        $('.cur-stu').hide();
                    }


                }
                $('.jedaul li').map(function () {
                    var dm = $(this).attr('data-m');
                    var db = $(this).attr('data-d');
                    if (timeAry != null && timeAry.length != 0) {
                        if (dm == timeAry[0].substring(5, 7)) {
                            for (var i = 0; i < timeAry.length; i++) {
                                if (db == timeAry[i].substring(8, 10)) {
                                    $(this).append('<img src="/static_new/images/homework/memo-rig.jpg" class="sign-ok">');
                                }
                            }
                        }

                    }
                })
                $('#homeworkList').empty();
                var data = rep.message.rows;
                if ($('#roleName').val() == "stu" || $('#roleName').val() == "par") {
                    $('.page-list2').hide();
                    $('.page-list1').hide();
                    $('.page-list3').hide();
                    $('.head-top').show();
                    $('#year').text(selDate.substring(0, 4));
                    $('#date').text(selDate.substring(5, 7) + "月" + selDate.substring(8, 10) + "日");
                    var a = new Array("日", "一", "二", "三", "四", "五", "六");
                    var week = new Date(selDate).getDay();
                    $('#week').text("星期" + a[week]);
                    var str = "";
                    if (data != null && data.length != 0) {
                        for (var i = 0; i < data.length; i++) {
                            var fileStr = "";
                            if (data[i].fileCount > 0) {
                                fileStr = '<i class="i-files"></i>';
                            }
                            var delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span>';
                            if ($('#userRole').val() == 4) {
                                if (data[i].sign == 1) {
                                    delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span><i class="i-im3" hwid=' + data[i].id + '>已签到</i>';
                                } else {
                                    delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span><i class="i-im2" hwid=' + data[i].id + '>签到</i>';
                                }
                            }
                            str += '<li nm=' + data[i].userName + ' hwid=' + data[i].id + '><p><em>【' + data[i].subjectName + '】</em>' + delStr + '</p><div><span>' + data[i].count + '人回复</span><span class="prev-spp">' + data[i].signCount + '已签/' + data[i].allCount + '</span>><span class="time">' + data[i].time + '</span></div></li>';
                        }
                        $('#homeworkList').append(str);
                    }
                } else if ($('#roleName').val() == "head") {
                    $('.head-top').show();
                    $('#year').text(selDate.substring(0, 4));
                    $('#date').text(selDate.substring(5, 7) + "月" + selDate.substring(8, 10) + "日");
                    var a = new Array("日", "一", "二", "三", "四", "五", "六");
                    var week = new Date(selDate).getDay();
                    $('#week').text("星期" + a[week]);
                    var str = "";
                    if (data != null && data.length != 0) {
                        for (var i = 0; i < data.length; i++) {
                            var fileStr = "";
                            if (data[i].fileCount > 0) {
                                fileStr = '<i class="i-files"></i>';
                            }
                            var delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span>';
                            if ($('#userId').val() == data[i].userId) {
                                delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span><i class="i-im" hwid=' + data[i].id + '></i>';
                            }
                            str += '<li nm=' + data[i].userName + ' hwid=' + data[i].id + '><p><em>【' + data[i].subjectName + '】</em>' + delStr + '</p><div><span>' + data[i].count + '人回复</span><span class="prev-spp">' + data[i].signCount + '已签/' + data[i].allCount + '</span>><span class="time">' + data[i].time + '</span></div></li>';
                        }
                        $('#homeworkList').append(str);
                    }
                    $('.page-list2').show();
                    $('.page-list1').hide();
                    $('.page-list3').hide();
                    $('.page-list2').jqPaginator({
                        totalPages: Math.ceil(rep.message.count / rep.message.pageSize) == 0 ? 1 : Math.ceil(rep.message.count / rep.message.pageSize),//总页数
                        visiblePages: 6,//分多少页
                        currentPage: parseInt(page),//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (n != page) {
                                page = n;
                                homeworkSign.selHomework(selDate, page);
                            }
                        }
                    });
                } else if ($('#roleName').val() == "tea") {
                    $('.page-list2').hide();
                    $('.page-list1').hide();
                    $('.page-list3').hide();
                    $('.head-top').hide();
                    var str = "";
                    if (data != null && data.length != 0) {
                        for (var i = 0; i < data.length; i++) {
                            var fileStr = "";
                            if (data[i].fileCount > 0) {
                                fileStr = '<i class="i-files"></i>';
                            }
                            var delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span>';
                            if ($('#userId').val() == data[i].userId) {
                                delStr = fileStr + '<span class="i-span" hwid=' + data[i].id + '>' + data[i].contentTxt + '</span><i class="i-im" hwid=' + data[i].id + '></i>';
                            }
                            str += '<li nm=' + data[i].userName + ' hwid=' + data[i].id + '><p></em>' + delStr + '</p><div><span>' + data[i].count + '人回复</span><span class="prev-spp">' + data[i].signCount + '已签/' + data[i].allCount + '</span><span class="time">' + data[i].time + '</span></div></li>';
                        }
                        $('#homeworkList').append(str);
                    }
                }
                $('.i-im2').click(function () {
                    var id = $(this).attr('hwid') + ",";
                    if (layer.confirm('确认签到！', {
                            btn: ['确定', '取消'] //按钮
                        }, function () {
                            homeworkSignData.ids = id;
                            Common.getPostData('/homework/signHomework.do', homeworkSignData, function (rep) {
                                layer.closeAll('dialog');
                                if (rep.code == '200') {
                                    homeworkSign.selHomework($('#selDate').val(), 1);
                                } else {
                                    layer.alert(rep.message);
                                }
                            });
                        })) {
                    }
                });
                $('.i-im').click(function () {
                    var id = $(this).attr('hwid');
                    if (layer.confirm('确认删除！', {
                            btn: ['确定', '取消'] //按钮
                        }, function () {
                            homeworkSignData.id = id;
                            Common.getPostData('/homework/delHomework.do', homeworkSignData, function (rep) {
                                layer.closeAll('dialog');
                                if (rep.code == '200') {
                                    homeworkSign.selHomework($('#selDate').val(), 1);
                                } else {
                                    layer.alert(rep.message);
                                }
                            });
                        })) {
                    }
                });
                $('.i-span').click(function () {
                    userName = $(this).parent().parent().attr("nm");
                    $("#hddd").html(userName)
                    homeworkSign.getHomeworkInfo($(this).attr('hwid'));
                    homeworkSign.getCommentInfo($(this).attr('hwid'), 1);
                });
            }
        });
    }
    homeworkSign.deleteHomework = function (id) {
        homeworkSignData.id = id;
        Common.getPostData('/homework/delHomework.do', homeworkSignData, function (rep) {
            if (rep.code == '200') {
                homeworkSign.selHomework($('#selDate').val(), 1);
            }
        });
    }
    homeworkSign.getHomeworkInfo = function (id) {
        homeworkSignData.id = id;
        Common.getPostData('/homework/getHomeworkInfo.do', homeworkSignData, function (rep) {
            var pic = "png,jpg,bmp,gif,psd,jpeg";
            if (rep.code == '200') {
                $('#hwId').val(rep.message.id);
                $('#content').html(rep.message.content);
                $('#count').text(rep.message.count);
                $('#hwtime').text(rep.message.time);
                $('#signCount').text(rep.message.signCount);
                $('#allCount').text(rep.message.allCount);
                $('.docs-pictures').empty();
                $('.docs-files').empty();
                if (rep.message.fileDto != null && rep.message.fileDto.length != 0) {
                    var str = "";
                    for (var i = 0; i < rep.message.fileDto.length; i++) {
                        var name = rep.message.fileDto[i].name;
                        var fileidex = name.split(".");
                        var last = fileidex[fileidex.length - 1];
                        var rs = pic.indexOf(last);
                        if (rs >= 0) {
                            str += '<a class="fancybox" href="' + rep.message.fileDto[i].value + '" data-fancybox-group="home" title="预览"><li><img class="content-img" title="点击查看大图" src="' + rep.message.fileDto[i].value + '?imageView/1/h/80/w/80"></li></a>';
                        }
                        var fileName = rep.message.fileDto[i].name;
                        var href = rep.message.fileDto[i].value;
                        var fileKey = href.substring(href.lastIndexOf('/') + 1);
                        var fileIndex = href.substring(href.lastIndexOf('.') + 1);
                        if (fileIndex == 'm3u8') {
                            href = '/commonupload/m3u8ToMp4DownLoad.do?filePath=' + href + '&fileName=' + fileName;
                        } else {
                            href = '/commonupload/doc/down.do?type=2&fileKey=' + fileKey + '&fileName=' + fileName;
                        }
                        var str2 = "<span>" + rep.message.fileDto[i].name + "<a href=" + href + " target='_blank' style='color:blue;'>下载</a></span>";
                        $('.docs-files').append(str2);
                    }
                    $('.docs-pictures').append(str);
                    $('.fancybox').fancybox();
                }
                $('.right-I').hide();
                $('.right-II').show();
            }
        });
    }
    homeworkSign.selHomeworkSignList = function (page) {
        homeworkSignData.ids = $('#hwId').val() + ",";
        homeworkSignData.page = page;
        homeworkSignData.pageSize = 10;
        Common.getPostData('/homework/getSignList.do', homeworkSignData, function (rep) {
            if (rep.code == '200') {
                $('.signlist').html('');
                Common.render({tmpl: $('#signlist_templ'), data: rep, context: '.signlist'});
                $('.page-list').show();
                $('.page-list2').hide();
                $('.page-list3').hide();
                $('.page-list').jqPaginator({
                    totalPages: Math.ceil(rep.message.count / 10) == 0 ? 1 : Math.ceil(rep.message.count / 10),//总页数
                    visiblePages: 6,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (n != page) {
                            page = n;
                            homeworkSign.selHomeworkSignList(page);
                        }
                    }
                });
                $('.right-I').hide();
                $('.right-II').hide();
                $('.right-III').show();
                $('.head-li').show();
            }
        });
    }
    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }


    homeworkSign.uploadVideo = function () {
        $('#file_upload').uploadify({
            'swf': "/static/plugins/uploadify/uploadify.swf",
            'uploader': '/homeschool/video/upload.do',
            'method': 'post',
            'buttonText': '',
            'fileTypeDesc': '视频文件',
            'fileSizeLimit': '300MB',
            'fileTypeExts': '*.avi; *.mp4; *.mpg; *.flv; *.wmv; *.mov; *.mkv',
            'multi': true,
            'fileObjName': 'Filedata',
            'onUploadSuccess': function (file, response, result) {
                try {
                    var json = $.parseJSON(response);
                    if (json.flg) {
                        var imgUrl = json.vimage;
                        if (imgUrl == "") {
                            imgUrl = '/img/K6KT/video-cover.png';
                        }
                        $('.work-sign-ul').append('<li img="' + imgUrl + '" pth="' + json.vurl + '" tp="2" nm="' + json.vname + '">' + json.vname + '<i class="tab-x">删除</i></li>');
                        // $('.imglist').append('<tr img="'+imgUrl+'" vurl="'+json.vurl+'"><td><span><img src="'+imgUrl+'" class="img1" vurl="'+json.vurl+'"><img src="/static_new/images/electronicClassCard/close.png" class="tab-x"></span></td><td><textarea></textarea></td></tr>');
                    } else {
                        someFileFailed = true;
                    }
                    /*关闭图片*/
                    $(".tab-x").click(function (event) {
                        $(this).parent().remove();
                    });
                } catch (err) {
                }
            },
            'onQueueComplete': uploadComplete,
            'onUploadError': function (file, errorCode, errorMsg, errorString) {
                someFileFailed = true;
            }
        });
    }
    function uploadComplete(queueData) {
        if (someFileFailed) {
            layer.msg("上传失败！")
            someFileFailed = false;
        }
    }

    homeworkSign.uploadImage = function () {
        $('#file_upload2').uploadify({
            'swf': "/static/plugins/uploadify/uploadify.swf",
            'uploader': '/dzmedia/pic/upload.do',
            'method': 'post',
            'buttonText': '',
            'fileTypeDesc': '选择图片',
            'fileSizeLimit': '300M',
            'fileTypeExts': '*.png; *.jpg;*.jpeg;*.gif;',
            'multi': true,
            'fileObjName': 'Filedata',
            'onUploadSuccess': function (file, response, result) {
                try {
                    var json = $.parseJSON(response);
                    $('.work-sign-ul').append('<li img="" pth="' + json.path + '" tp="1" nm="' + json.name + '">' + json.name + '<i class="tab-x">删除</i></li>');
                    /*关闭图片*/
                    $(".tab-x").click(function (event) {
                        $(this).parent().remove();
                    });
                } catch (err) {
                }
            },
            'onQueueComplete': uploadComplete,
            'onUploadError': function (file, errorCode, errorMsg, errorString) {
                someFileFailed = true;
            }
        });
    }

    homeworkSign.uploadImag = function () {
        $('#file_upload3').uploadify({
            'swf': "/static/plugins/uploadify/uploadify.swf",
            'uploader': '/dzmedia/pic/upload.do',
            'method': 'post',
            'buttonText': '',
            'fileTypeDesc': '选择文档',
            'fileSizeLimit': '300M',
            'fileTypeExts': '*.doc; *.docx;*.xls;',
            'multi': true,
            'fileObjName': 'Filedata',
            'onUploadSuccess': function (file, response, result) {
                try {
                    var json = $.parseJSON(response);
                    $('.work-sign-ul').append('<li tp="9" img="" pth="' + json.path + '" nm="' + json.name + '">' + json.name + '<i class="tab-x">删除</i></li>');
                    /*关闭图片*/
                    $(".tab-x").click(function (event) {
                        $(this).parent().remove();
                    });
                } catch (err) {
                }
            },
            'onQueueComplete': uploadComplete,
            'onUploadError': function (file, errorCode, errorMsg, errorString) {
                someFileFailed = true;
            }
        });
    }


    homeworkSign.init();
});