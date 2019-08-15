$(function(){

    jQuery(document).ready(function($) {
        $('.file-line i').click(function(){
            $(this).parent().fadeOut();
        })
    })

    $.fn.zTree.init($('#classSubjectDirUl'),{
        async: {
            enable: true,
            url: '/dir/find.do?type=CLASS_LESSON'
        },
        data: {
            simpleData: {
                enable: true,
                pIdKey: 'parentId'
            }
        },
        callback: {
            beforeClick: function(treeId, node){
                if(node.virtual){
                    return false;
                }
                return autoCancelSelect(treeId, node);
            }
        }
    });

    $.fn.zTree.init($('#schoolDirUl'),{
        async: {
            enable: true,
            url: '/dir/find.do?type=SCHOOL_RESOURCE'
        },
        data: {
            simpleData: {
                enable: true,
                pIdKey: 'parentId',
                rootPId: 0
            }
        },
        callback: {
            beforeClick: autoCancelSelect
        }
    });
});

function autoCancelSelect(treeId, node){
    var tree = $.fn.zTree.getZTreeObj(treeId);
    if(tree.isSelectedNode(node)){
        tree.cancelSelectedNode(node);
    }else{
        tree.selectNode(node);
    }
    return false;
}

function addFlippedCourse(t, id, list) {
    //	t=typeof t==1?1:2;
    var name = $.trim($('#new-course-name').val());
    //var descr = $.trim($('#new-course-descr').val());
    var postdata = {
        'courseName': name,
        'classList': list,
        'gradeId': $('#grade-list').val(),
        'instype': t
        //'courseContent': descr
    };
    if (t == 2) {
        postdata.cloudCourseId = id;
    }
    if (!name /* || !descr*/ ) {
        ShowPromptDialog("请输入课程名！");
    } else {
        $.ajax({
            url: '/reverse/insertfzCourse.action',
            type: 'post',
            dataType: 'json',
            data: postdata,
            success: function(data) {
                location.href = '/courseedit/1/' + data.courseId;
            },
            complete: function(data) {}
        });
    }
}

function ResetCourseInfo() {
    $("#course-name").val(courseName);
    /* $("#course-descr").val(courseDescr);*/
    //$("#start-time").val(courseStartTime);
    //$("#end-time").val(courseEndTime);
}

function showCourseInfo(data) {
    courseName = data.name;
    document.title = '课程维护 - ' + courseName;
    $('#video-upload-section').show();
    ResetCourseInfo();
}

function showCourseWares(data) {
    var rows = data.coursewareList;
    $("#courseware-list").empty();
    $("#courseware-list5").empty();
    $("#courseware-list2").empty();
    $("#courseware-list3").empty();
    $("#courseware-list4").empty();
    for (var i = 0; i < rows.length; i++) {
        var cw = rows[i];
        var tuisong = '';
        if (cw.value5==0 && cw.value4!=2) {
            //tuisong = '<span class="ww-push" onclick="updateCourseWare(\'' + cw.id + '\')">推送</span>';
        }
        var cwHtml = '<div class="courseware">';
        cwHtml += '<a onclick="DeleteCourseWare(\'' + cw.id + '\')" class="removelink"><i class="fa fa-times-circle fa-lg"></i></a>';

        cwHtml += '<a href="' + cw.value + '" class="courselink"><img src="'+getWareType(cw.value)+'"></a>';
        cwHtml += '<div class="coursename"><a>' + cw.type + '</a></div>'+tuisong+'</div>';
        if (cw.value4==0) {
            $("#courseware-list").append(cwHtml);
        } else if (cw.value4==1) {
            $("#courseware-list2").append(cwHtml);
        } else if (cw.value4==2) {
            $("#courseware-list3").append(cwHtml);
        } else if (cw.value4==3) {
            $("#courseware-list4").append(cwHtml);
        } else if (cw.value4==4) {
            $("#courseware-list5").append(cwHtml);
        }
    }
}


function getWareType(link)
{
	var address="/img/docicon/unknow.png";
	if(link.indexOf(".doc")>0 || link.indexOf(".docx")>0) address="/img/coursedoc.png";
	if(link.indexOf(".pdf")>0 ) address="/img/coursepdf.png";
	if(link.indexOf(".xls")>0 || link.indexOf(".xlsx")>0) address="/img/coursexls.png";
	if(link.indexOf(".ppt")>0 || link.indexOf(".pptx")>0) address="/img/courseppt.png";
	if(link.indexOf(".swf")>0) address="/img/courseswf.png";
    if(link.indexOf(".doc")>0 || link.indexOf(".docx")>0) address="/img/docicon/doc.png";
    if(link.indexOf(".pdf")>0 ) address="/img/docicon/pdf.png";
    if(link.indexOf(".xls")>0 || link.indexOf(".xlsx")>0) address="/img/docicon/xls.png";
    if(link.indexOf(".ppt")>0 || link.indexOf(".pptx")>0) address="/img/docicon/ppt.png";
    if(link.indexOf(".swf")>0) address="/img/courseswf.png";
    if(link.indexOf(".avi")>0 ) address="/img/docicon/avi.png";
    if(link.indexOf(".css")>0 ) address="/img/docicon/css.png";
    if(link.indexOf(".eml")>0 ) address="/img/docicon/eml.png";
    if(link.indexOf(".mov")>0 ) address="/img/docicon/mov.png";
    if(link.indexOf(".mp3")>0 ) address="/img/docicon/mp3.png";
    if(link.indexOf(".png")>0 ) address="/img/docicon/png.png";
    if(link.indexOf(".raw")>0 ) address="/img/docicon/raw.png";
    if(link.indexOf(".txt")>0 ) address="/img/docicon/txt.png";
    if(link.indexOf(".zip")>0 || link.indexOf(".rar")>0) address="/img/docicon/zip.png";
	return address;
}



function showLessonVideos(response) {
    var html = template('video-list-template', response);
    $(".video-list-container").html(html);
}

function GetCourseInfo() {
    $.getJSON('/lesson/detail.do', {
            'lessonId': courseId
        },
        showCourseInfo);
}

function GetCourseWares() {
    $.getJSON('/lesson/detail.do', {
            'lessonId': courseId
        },
        showCourseWares);
}

function getLessonVideos() {
    $.ajax({
        url: '/lesson/detail.do',
        cache: false,
        dataType: 'json',
        data: {'lessonId': courseId},
        success: showLessonVideos
    });
}

function getLessonInformation() {
    $.getJSON('/lesson/detail.do', {
            'lessonId': courseId
        },
        function(data) {
            showCourseInfo(data);
            showCourseWares(data);
            showLessonVideos(data);
        });
}

function DeleteCourseWare(cwid) {
    ConfirmDialog('确定删除该课件？', 'DoDeleteCourseWare(\'' + cwid + '\')');
}
function updateCourseWare(cwid) {
    if (confirm("确定推送至学生课件列表？")) {
        $.ajax({
            url: '/lesson/ware/update.do',
            type: 'post',
            data: {'lessonId': courseId, 'wareId': cwid, 'sort': $('#ftype').val()},
            success: function (data) {
                if (data.code == 200) {
                    alert("推送成功！");
                    GetCourseWares(courseId);
                } else {
                    alert("已全部推送，不需重新推送！");
                }

            }
        });
    }
}

function DoDeleteCourseWare(cwid) {
    MessageBox('删除中...', 0);
    $.getJSON('/lesson/ware/remove.do', {
            'lessonId': courseId,
            'wareId': cwid
        },
        function(data) {
            GetCourseWares(courseId);
            ClosePromptDialog();
        }
    ).error(function(e) {
            MessageBox('服务器响应请求出错，请稍后再试。', -1);
        });
}

function getSelectedTreeIds() {
    try {
        var classNodes = $.fn.zTree.getZTreeObj("classSubjectDirUl").getSelectedNodes();
        var schoolNodes = $.fn.zTree.getZTreeObj("schoolDirUl").getSelectedNodes();

        return $.map(classNodes.concat(schoolNodes), function (node) {
            if (node.type != 'temp') {
                return node.id;
            }
        }).join();
    } catch (e) {
        return null;
    }
}

var exerciseId = null;

function updatecourse() {
    var name = $.trim($('#course-name').val());

    if (!name) {
        MessageBox("请输入课程名！", -1);
        return;
    }

    MessageBox('保存中...', 0);

    var pushDirIds = getSelectedTreeIds();
    var pushDeferred;
    if (pushDirIds != null && pushDirIds.length > 0) {
        pushDeferred = $.ajax({
            url: '/lesson/push.do',
            data: {
                lessonId: courseId,
                dirIds: pushDirIds
            },
            type: 'post'
        });
    }

    var saveDeferred = $.ajax({
        url: '/lesson/update.do',
        type: 'post',
        data: {
            'lessonId': courseId,
            'name': name,
            exercieId: exerciseId,
            'isSyn': $('#syncPush:checked').length > 0 ? 1 : 0
        }
    });
    $.when(pushDeferred, saveDeferred).done(function (pushRes, saveRes) {
        if (saveRes[0].code == 200) {
            if (GetQueryString("a")!="10000") {
                location.href="/microlesson/matchDetailInfo.do?matchid="+$("body").attr("mid");
            } else {
                location.href="/microlesson/matchDetailInfo.do?a=10000&matchid="+$("body").attr("mid");
            }


        } else {
            MessageBox(saveRes[0].message, -1);
        }
    }).fail(function (promise, status, message) {
        MessageBox(message, -1);
    });
}

function UpdateFZCourseInfo() {
    var name = $.trim($('#course-name').val());
    /* var descr = $.trim($('#course-descr').val());*/

    if (!name /* || !descr*/ ) {
        MessageBox("请输入课程名！", -1);
        return;
    }
    /*if(window.location.href.split('/')[4] == '3'){
    	var videoCount = $('.video-list-container').children('.video-container').length;
    	var courseCount = $('#courseware-list').find('.coursename').length;
    	var questionCount = $('.question-list-table').find('tr').length;
    	if(videoCount <= 0 && courseCount <= 0 && questionCount <= 1){
    		MessageBox("至少上传一个视频或者课件或者课后小练习", -1);
    		return;
    	}
    }*/



    MessageBox('保存中...', 0);

    var pushDirIds = getSelectedTreeIds();
    var pushDeferred;
    if (pushDirIds != null && pushDirIds.length > 0) {
        pushDeferred = $.ajax({
            url: '/lesson/push.do',
            data: {
                lessonId: courseId,
                dirIds: pushDirIds
            },
            type: 'post'
        });
    }

    var saveDeferred = $.ajax({
        url: '/lesson/update.do',
        type: 'post',
        data: {
            'lessonId': courseId,
            'name': name,
            exercieId: exerciseId,
            'isSyn': $('#syncPush:checked').length > 0 ? 1: 0
        }
    });
    $.when(pushDeferred, saveDeferred).done(function(pushRes, saveRes) {
        if (saveRes[0].code == 200) {
            var cookie = getCookie('saveclass');
            if(cookie){
                location.href = '/lesson/teacher.do';
            }else{
                if ($('#retype').val()==3) {
                    MessageBox('修改成功！', 1, 'save','',1);
                } else {
                    MessageBox('修改成功！', 2, 'save', '<ul><li>老师您好，如果您没有将视频推送给<strong class="msg-orange">班级课程</strong>，请返回<strong class="msg-orange">备课空间</strong>，点击刚才新建的该课程，选择工具栏上的<strong class="msg-orange">推送到班级</strong>之后，学生们才能在他们自己的<strong class="msg-orange">班级课程</strong>里看到这个视频哦</li><li>老师您的这个视频上传后<label style="color:#00bfff;">20分钟</label>之后才能被学生点击观看哦。因为我们的系统需要一点时间对所有用户上传的视频内容安全性做个审核啦~</li></ul>');
                }
            }

        } else {
            MessageBox(saveRes[0].message, -1);
        }
    }).fail(function(promise, status, message) {
        MessageBox(message, -1);
    });


    /*
    if($('#syncPush:checked').length>0){
        $.ajax({
            url:'/lesson/checkSync.do?lessonId='+courseId,
            type:'get',
            success:function(dataNew){
                if(dataNew == 'delete'){
                    if(confirm("同步后,将会删除已推送课程中的习题,及学生已做的解答,是否将习题修改一并推送?点击取消，只推送视频和课件。")){
                        data.syncDelete = true;
                        syncData(data);
                        return false;
                    }else{
                        data.syncPush = true;
                        syncData(data);
                    }
                }else if(dataNew == 'update'){
                    if(confirm("同步后,将会更新已推送课程中的习题,并删除学生已做的解答,是否将习题修改一并推送?点击取消,只推送视频和课件。")){
                        data.syncUpdate = true;
                        syncData(data);
                        return false;
                    }else{
                        data.syncPush = true;
                        syncData(data);
                    }
                }else if(dataNew == 'add'){

                        data.syncAdd = true;
                        syncData(data);
                        return false;
                }else if(dataNew == 'noChange'){
                        data.syncPush = true;
                        syncData(data);
                        return false;
                }
            },
            error:function(e){

            }
        });
    }else{

        syncData(data);
    }
    */

    //if($('#classSubjectDirUl').length){
    //    data.pushDirs = getSelectedNodeIds('classSubjectDirUl').concat(getSelectedNodeIds('schoolDirUl'));
    //}
    //
    //$.ajax({
    //    url: '/lesson/update.do',
    //    type: 'post',
    //    data: data,
    //    success: function(data) {
    //        if(data == 'success'){
    //            var cookie = getCookie('saveclass');
    //            if(cookie){
    //                location.href = '/teacher/course';
    //            }else{
    //            	if ($('#retype').val()==3) {
    //            		MessageBox('修改成功！', 1, 'save','',1);
    //            	} else {
    //            		MessageBox('修改成功！', 2, 'save', '<ul><li>老师您好，如果您没有将视频推送给<strong class="msg-orange">班级课程</strong>，请返回<strong class="msg-orange">备课空间</strong>，点击刚才新建的该课程，选择工具栏上的<strong class="msg-orange">推送到班级</strong>之后，学生们才能在他们自己的<strong class="msg-orange">班级课程</strong>里看到这个视频哦</li><li>老师您的这个视频上传后<label style="color:#00bfff;">20分钟</label>之后才能被学生点击观看哦。因为我们的系统需要一点时间对所有用户上传的视频内容安全性做个审核啦~</li></ul>');
    //            	}
    //            }
    //        }
    //
            
//            if (window.opener && window.opener.nav) {
//                window.opener.nav();
//                window.close();
//            } else {
//                location.href = '/teacher/course';
//            }
//        },
//        error: function(e) {
//            ClosePromptDialog();
//        }
//    });
    /*    $.getJSON('/reverse/updatefzCourse.action',
        {
            'courseId': courseId,
            'courseName': name,
            'courseContent': descr,
            'gradeId': $('#grade-list').val(),
            'startWatchTime': start,
            'endWatchTime': end
        },
        function(e){
            ShowPromptDialog('修改成功！', true);
            GetCourseInfo();
            setTimeout(function(){location.href = '/user';}, 1500);
        }
    );*/
}

function UpdateFZCourseInfoForHomework() {
    var name = $.trim($('#course-name').val());
    /* var descr = $.trim($('#course-descr').val());*/

    if (!name /* || !descr*/ ) {
        MessageBox("请输入课程名！", -1);
        return;
    }

    MessageBox('保存中...', 0);

    var pushDirIds = getSelectedTreeIds();
    var pushDeferred;
    if (pushDirIds != null && pushDirIds.length > 0) {
        pushDeferred = $.ajax({
            url: '/lesson/push.do',
            data: {
                lessonId: courseId,
                dirIds: pushDirIds
            },
            type: 'post'
        });
    }

    var saveDeferred = $.ajax({
        url: '/lesson/update.do',
        type: 'post',
        data: {
            'lessonId': courseId,
            'name': name,
            exercieId: exerciseId,
            'isSyn': $('#syncPush:checked').length > 0 ? 1: 0
        }
    });
    $.when(pushDeferred, saveDeferred).done(function(pushRes, saveRes) {
        if (saveRes[0].code == 200) {
            var cookie = getCookie('savehomework');
            if(cookie){
                location.href = '/homework/teacher.do';
            }else{
                if($(".video-list-container").children("div.video-container").length>0){
                    MessageBox('修改成功！', 2, 'savehomework', '<ul><li>老师您的这个视频上传后<label style="color:#00bfff;">20分钟</label>之后才能被学生点击观看哦。因为我们的系统需要一点时间对所有用户上传的视频内容安全性做个审核啦~</li></ul>',4);
                } else {
                    location.href = '/homework/teacher.do';
                }

            }

        } else {
            MessageBox(saveRes[0].message, -1);
        }
    }).fail(function(promise, status, message) {
        MessageBox(message, -1);
    });

}

function UpdateFZCourseInfoForHomework(type) {
    var name = $.trim($('#course-name').val());
    /* var descr = $.trim($('#course-descr').val());*/

    if (!name /* || !descr*/ ) {
        MessageBox("请输入课程名！", -1);
        return;
    }

    MessageBox('保存中...', 0);

    var pushDirIds = getSelectedTreeIds();
    var pushDeferred;
    if (pushDirIds != null && pushDirIds.length > 0) {
        pushDeferred = $.ajax({
            url: '/lesson/push.do',
            data: {
                lessonId: courseId,
                dirIds: pushDirIds
            },
            type: 'post'
        });
    }

    var saveDeferred = $.ajax({
        url: '/lesson/update.do',
        type: 'post',
        data: {
            'lessonId': courseId,
            'name': name,
            exercieId: exerciseId,
            'isSyn': $('#syncPush:checked').length > 0 ? 1: 0,
            type:type
        }
    });
    $.when(pushDeferred, saveDeferred).done(function(pushRes, saveRes) {
        if (saveRes[0].code == 200) {
            var cookie = getCookie('savehomework');
            if(cookie){
                location.href = '/homework/teacher.do?type=' + sessionStorage.getItem('qianhoutype');
            }else{
                if($(".video-list-container").children("div.video-container").length>0){
                    MessageBox('修改成功！', 2, 'savehomework', '<ul><li>老师您的这个视频上传后<label style="color:#00bfff;">20分钟</label>之后才能被学生点击观看哦。因为我们的系统需要一点时间对所有用户上传的视频内容安全性做个审核啦~</li></ul>',4);
                } else {
                    location.href = '/homework/teacher.do?type=' + sessionStorage.getItem('qianhoutype');
                }

            }

        } else {
            MessageBox(saveRes[0].message, -1);
        }
    }).fail(function(promise, status, message) {
        MessageBox(message, -1);
    });

}

function syncData(data){
	//alert(11);
    if($('#classSubjectDirUl').length){
        data.pushDirs = getSelectedNodeIds('classSubjectDirUl').concat(getSelectedNodeIds('schoolDirUl'));
    }

    $.ajax({
        url: '/lesson/update.do',
        type: 'post',
        data: data,
        success: function(data) {
            if(data == 'success'){
                var cookie = getCookie('saveclass');
                if(cookie){
                    location.href = '/teacher/course';
                }else{
                    if ($('#retype').val()==3) {
                        MessageBox('修改成功！', 1, 'save','',1);
                    } else {
                        MessageBox('修改成功！', 2, 'save', '<ul><li>老师您好，如果您没有将视频推送给<strong class="msg-orange">班级课程</strong>，请返回<strong class="msg-orange">备课空间</strong>，点击刚才新建的该课程，选择工具栏上的<strong class="msg-orange">推送到班级</strong>之后，学生们才能在他们自己的<strong class="msg-orange">班级课程</strong>里看到这个视频哦</li><li>老师您的这个视频上传后<label style="color:#00bfff;">20分钟</label>之后才能被学生点击观看哦。因为我们的系统需要一点时间对所有用户上传的视频内容安全性做个审核啦~</li></ul>');
                    }
                }
            }


//            if (window.opener && window.opener.nav) {
//                window.opener.nav();
//                window.close();
//            } else {
//                location.href = '/teacher/course';
//            }
        },
        error: function(e) {
            ClosePromptDialog();
        }
    });
}

function getSelectedNodeIds(treeId){
    return $.map($.fn.zTree.getZTreeObj(treeId).getSelectedNodes(), function(node){
        return node.id;
    });
}

function UploadCourseWare(form) {
    var courseWareName = $.trim($("#courseWareName").val());
    $("#courseWareName").val(courseWareName);
    if (!courseWareName) {
       // MessageBox('请输入课件名。', -1);
       // return;
    }
    if (!form.elements['doc'].value) {
        MessageBox('请选取课件文件。', -1);
        return;
    }

    MessageBox('上传中', 0);
    fileUpload(form, function(content) {
        var s = '';
        try {
            s = content;
            var json = $.parseJSON(s);
            if (json.code == "200") {
                MessageBox("上传成功!", 1);
            } else {
                MessageBox(json.message, -1);
            }
            form.reset();
            closeDialog(".inside-dialog");
            GetCourseWares();
        } catch (err) {
            if (s.length == 0) {
                MessageBox("上传完毕。", 1);
            } else if (s.length > 50) {
                MessageBox("服务器响应错误。", -1);
            } else {
                MessageBox(s, -1);
            }
        }
    });
}

function UploadCourseVideo(upload_button) {
    var form = upload_button.form;
    if (!form.elements['upload'].value) {
        MessageBox('请选择视频文件。', -1);
        return;
    }
    $('.inside-dialog').hide();
    MessageBox('上传中...', 0);
    fileUpload(form, function(content) {
        var s = '';
        try {
            s = $(content).text();
            var json = $.parseJSON(s);
            if (json.result == true) {
                MessageBox(json.uploadType, 1);
            } else {
                MessageBox(json.uploadType, -1);
            }
            upload_button.value = '重传';
        } catch (err) {
            if (s.length == 0) {
                MessageBox("上传完毕。", 1);
            } else if (s.length > 50) {
                MessageBox("服务器响应错误。", -1);
            } else {
                MessageBox(s, -1);
            }
        }
    });
}

function getGradeInfoOfTeacher(callback) {

}

function removeLessonVideo(dom_a) {
    var video_name = $(dom_a).parent().next();

    ConfirmDialog("确定删除视频<span class=\"orange\">"+ video_name.html() +"</span>？", "doRemoveLessonVideo('"+ video_name.attr('videoid') +"')");
}

function doRemoveLessonVideo(videoId) {
    MessageBox("删除中...", 0);
    $.getJSON("/lesson/video/remove.do", {'lessonId': courseId, 'videoId': videoId}, function(response){
        getLessonVideos();
        ClosePromptDialog();
    });
}
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
function coverError(img) {
    var uploadState = img.getAttribute('state');
    if (uploadState == 0) {
        img.src = "/img/converting_small.png";
    }
}