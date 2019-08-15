

//-----------------------------------  浏览器兼容性代码   解决  IE10中  console  未定义错误！
if (!window.console || !console.firebug)

{
    var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml",
        "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

    window.console = {};
    for (var i = 0; i < names.length; ++i)
        window.console[names[i]] = function() {}
}

//------------------------------------

var newCourseclass = '.inside-dialog';
var currentCourseId = 0;
$(function() {
    var $selectSubjectList = $('.subject-ul');
    var $selectGradeList = $('.grade-ul');
    var $knowledgeList = $('.knowledge-ul');

    


});


function addDir(evt, treeId, node, cancel) {
   // var tree = $.fn.zTree.getZTreeObj(treeId);
    if (cancel || node.name.trim() == '') {
        /**
         * 取消编辑，删除文件夹
         */
        trees.removeNode(node, false);
        return;
    }
    var parentNode = node.getParentNode();
    var updateNode = function (id) {
        if (id) {
            node.id = id.message;
            node.type = null;
            trees.updateNode(node);
        }
    };
    if (parentNode.type == 'classSubject') {
        /**
         * 添加班级根文件夹
         */
        $.post('/directory/addCourseRootDir', {
            classSubjectId: parentNode.id,
            name: node.name
        }, updateNode);
    } else {
        /**
         * 添加普通文件夹
         */
        $.post('/dir/add.do', {
            parentdir: parentNode.id,
            name: node.name

        }, updateNode);
    }
}





function makeTreeDroppable(treeId) {
    var tree = $.fn.zTree.getZTreeObj(treeId);
    $('#' + treeId + ' a[treenode_a]:not(:has(.virtual_ico_docu,.virtual_ico_open,.virtual_ico_close))').droppable({
        accept: '.course-container',
        activeClass: 'node-hightlight',
        hoverClass: 'tmpTargetNode_inner',
        drop: function (evt, ui) {
            moveLesson(ui.draggable.attr('file-id'), tree.getNodeByTId($(this).closest('li').attr('id')).id);
        }
    });
}

function removeTreeNewButton(treeId, node) {
    $('#' + node.tId + '_add').unbind().remove();
}


function scrollNode(treeId, node) {
    $('#' + treeId).stop(true, false);
    $('#' + treeId).animate({
        scrollLeft: node.level * 18
    }, 'fast');
}

function cancelScrollNode(treeId) {
    $('#' + treeId).stop(true, false);
    $('#' + treeId).animate({
        scrollLeft: 0
    }, 'fast');
}

function renderTreeNewButton(treeId, node) {
    var aObj = $("#" + node.tId + "_a");
    if (node.id == 'new' || $("#" + node.tId + "_add").length > 0) return;
    var editStr = "<span id='" + node.tId + "_add' class='button add' title='新建文件夹' treenode_add> </span>";
    aObj.append(editStr);
    var btn = $("#" + node.tId + "_add");
    if (btn) btn.bind("click", function () {
    	
    	
    	/**
        var tree = $.fn.zTree.getZTreeObj(treeId);
    	
        tree.editName(tree.addNodes(node, {
            name: '',
            id: 'new',
            type: 'temp'
        })[0]);
        
        **/
    	

    	
    	trees.editName(trees.addNodes(node, {
            name: '',
            id: 'new',
            type: 'temp'
        })[0]);
        
        
    });
}

function cancelEdit(treeId) {
    $.fn.zTree.getZTreeObj(treeId).cancelEditName();
}


function clickTreeNode(event, treeId, node) {
    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    treeObj.cancelSelectedNode();
    treeObj.checkNode(node, !node.getCheckStatus().checked, false);
}



var polyv_player = null;


function gotoHelpPage(){
    location.href = "/business/reverse/user/userHelp.jsp";
}

var video_id;

var watch_timer = null, watchedTime = 0, videoLength = 0;
var isFlash = false;

function cloudResPlay(id)
{
	 var obj=jQuery("#"+id);
	 increase(id,0);
	 tryPlayYCourse(obj);
}


function tryPlaySwf(obj,id)
{
	 increase(id,0);
	 tryPlayYCourse(obj);
}




function increase(id,type)
{
	 $.ajax({
         url: "/cloudres/increase.do",
         type: "post",
         data: {
             "id": id,
             "type": type
         },
         success: function(response) {
        	 if(type==0)
        	 {
        		  var n=jQuery("#v_"+id).text();
        		  jQuery("#v_"+id).text(parseInt(n)+1)
        	 }
        	 if(type==1)
    		 {
    		  var n=jQuery("#p_"+id).text();
    		  jQuery("#p_"+id).text(parseInt(n)+1)
    		 }
         },
         error: function(e) {
         }
     })
}

function tryPlayYCourse(ob) {
    var url = ob.attr('vurl');
    video_id = ob.attr('vid');
    videoLength = (ob.attr('video-length')/ 1000||0);

    var videoType = getVideoType(url);
    switch (videoType) {
        case "FLASH":
            $('#sewise-div').hide();
            $('#player_div').show();
            $("#player-close-btn").show();
            $('#YCourse_player').addClass('flash-container');
            $("#player_div").addClass('flash-player-div');
            $("#player_div").html('<div id="swfobject_replace"></div>');
            var attributes = {
                id: 'flashViewer'
            };

            var params = {
                allowfullscreen: 'true',
                menu: 'false',
                wmode: 'transparent'
            };
            swfobject.embedSWF(url, "swfobject_replace", "100%", "100%", "9.0.0", '', {}, params, attributes);
            break;
        case "POLYV":
            $('#sewise-div').hide();
            $('#player_div').show();
            var _start = url.lastIndexOf('/') + 1, _end = url.lastIndexOf('.');
            var polyv_vid = url.substring(_start, _end);
            var player = polyvObject('#player_div').videoPlayer({
                'width':'100%',
                'height':'100%',
                'vid' : polyv_vid
            });
            break;
        case "HLS":
            $('#player_div').hide();
            $('#sewise-div').show();
            try {
                SewisePlayer.toPlay(url, "云课程", 0, true);
            } catch (e) {
                playerReady.URL = url;
                isFlash = true;
            }
            break;
    }

    $('#YCourse_player').fadeIn('fast');
    $('.dialog-bg').fadeIn('fast');
    watch_timer = setInterval(watchTimeCheck, 1000);
    vedioSize();
    if (!isStudent) {
        return;
    }
    switch (videoType) {
        case "FLASH":
            if (bowser.ios || bowser.android && typeof swfobject == 'undefined') {
            } else {
                clearTimeout(watch_timer);
                watch_timer = setTimeout(function cloudCourseScore(){
                    $.ajax({
                        url: '/experience/studentScoreLog.do',
                        type: 'POST',
                        dataType: 'json',
                        async:false,
                        data: {
                            'relateId': video_id,
                            'scoretype': 0
                        },
                        success: function(data) {
                            if (data.resultcode == 0) {
                                scoreManager(data.desc, data.score);
                            }
                        },
                        error: function() {
                        }
                    });

                },1000);
            }
            break;
        case "POLYV":
            watchedTime = 0;
            if (bowser.ios || bowser.android && typeof swfobject == 'undefined') {
                var video = $('#player_div video')[0];
                video.addEventListener('play', s2j_onPlayStart);
                video.addEventListener("pause", s2j_onVideoPause);
                video.addEventListener('ended', s2j_onPlayOver);
                video.play();
            }
            break;
        case "HLS":
            watchedTime = 0;
            break;
    }
}

function playerReady(name){
    if(isFlash){
        SewisePlayer.toPlay(playerReady.URL, "云课程", 0, true);
    }
}

function swfObjectCallback(e) {
    if(e.success){
        //alert("The embed was successful!");
    } else {
        //alert("The embed failed!");
    }
}

function closeCloudView() {
    $('#YCourse_player').fadeOut('fast', function() {
        $('#YCourse_player').removeClass('flash-container');
        $("#player_div").removeClass('flash-player-div');
        $("#player_div").empty();
    });
    $('.dialog-bg').fadeOut('fast');
    clearInterval(watch_timer);
    watchedTime = 0;
    try {
        SewisePlayer.doStop();
    } catch (e) {
    }
}


function addCourseClass(list) {
    var content = "<ul class='add-course-class'>";
    for (var i = 0; i < list.length; i++) {
        content += "<li><input type='checkbox' data-id='" + list[i].classtype + "," + list[i].id + "'>" + list[i].classname + "</li>";
    }
    content += "</ul>";
    return content;
}

function getVideoType(url) {
    if (url.indexOf('polyv.net') > -1) {
        return "POLYV";
    }
    if (url.endWith('.swf')) {
        return 'FLASH';
    }
    return 'HLS';
}

function s2j_onPlayOver() {
    clearInterval(watch_timer);
}

function s2j_onPlayStart() {
    if (!isStudent) {
        return;
    }
    clearInterval(watch_timer);
    watch_timer = setInterval(watchTimeCheck, 1000);
}

function s2j_onVideoPause() {
    clearInterval(watch_timer);
}

var onStop = s2j_onPlayOver;
var onStart = s2j_onPlayStart;
var onPause = s2j_onVideoPause;

function watchTimeCheck() {
    watchedTime++;
    console.log(watchedTime);
    console.log( videoLength* 0.8);
    if (watchedTime > videoLength* 0.8) {
        clearInterval(watch_timer);
        watchedTime = 0;
        $.getJSON("/microcourse/viewrecord/add.do",
            {
                'videoId': video_id,
                'viewType': 1
            },
            function (data) {
            });
        $.getJSON("/experience/studentScoreLog.do",
            {
                'relateId': video_id,
                'scoretype': 0
            },
            function (data) {
                if (data.resultcode == 0) {
                    scoreManager(data.desc, data.score);
                }
            });
    }
}

var pushDialog = null;

