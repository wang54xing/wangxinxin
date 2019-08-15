

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
var myType = 0;//0-->精品课程   1-->我的精品课程     2-->已购买精品课程

$(function() {
    var $selectSubjectList = $('.subject-ul');
    var $selectGradeList = $('.grade-ul');
    var $knowledgeList = $('.knowledge-ul');
    
    $.ajax({
        url: '/cloud/infos1.do',
        type: 'POST',
        dataType: 'json',
        data: {},
        success: function(data) {
            showdata(data);
        },
        error: function() {
            console.log('infos error');
        }
    });

    $('body').on('click', '.retrieval-detail>img', function() {
        var content = $(this).parent().attr('data-status');
        $(this).parent().remove();
        removeActive(content);
    });

    $('.retrieval-search-form').keydown(function(event) {
        if (event.which == 13) {
            getSubjectCourseInfo($('.retrieval-search-form').val(), 1, 12);
        }
    });

    $('.retrieval-search-button').on('click', function() {
        getSubjectCourseInfo($('.retrieval-search-form').val(), 1, 12);
    });

    $('body').on('click', '#knowledge-down', function() {
        $('#retrieval-knowledge').css('height', 'auto');
        $(this).text('收起');
        $(this).removeClass('knowledge-down').addClass('knowledge-up').attr('id','knowledge-up');
    });
    $('body').on('click', '#knowledge-up', function() {
        $('#retrieval-knowledge').css('height', '39px');
        $(this).text('更多');
        $(this).removeClass('knowledge-up').addClass('knowledge-down').attr('id','knowledge-down');
    });

    // 演示js
    $('.retrieval-search').change(function() {
        var status = $('.retrieval-search').val();
        $('.retrieval-detail>img').click();
        $('.knowledge-ul>li').show();
        $('.knowledge-up').click();
        $('.knowledge-down').show();
        $.ajax({
            url: '/emarket/baseList.do',
            type: 'POST',
            dataType: 'json',
            data: {
                "stageIds": status,
                "subjectIds": ''
            },
            success: function(data) {
                showdata(data);
            },
            error: function() {
                console.log('baseList error');
            }
        });
        getSubjectCourseInfo('', 1, 12);
    });

    function showdata(data) {
    	var subjectList = data.subject;
        var gradeList = data.grade;
        var pointList = data.type;
        $selectSubjectList.empty();
        $selectGradeList.empty();
        $knowledgeList.empty();
        $('.retrieval-grade').show();
        for (var i = 0; i < subjectList.length; i++) {
            var content = '';
            content += "<li data-subjectId='" + subjectList[i].id + "'>" + subjectList[i].value + "</li>";
            //大学风采
            if(subjectList[i].subjectName == '大学风采' && window.location.href.indexOf('/cloudClass') > 0){
            	content += '<img src="/img/star.gif" class="gif-star"/>';
            }
            $selectSubjectList.append(content);
        }
        
        if (gradeList.length == 0) {
            $('.retrieval-grade').hide();
        } else {
            for (var i = 0; i < gradeList.length; i++) {
                var content = '';
                content += "<li data-status='" + gradeList[i].id + "'>" + gradeList[i].value + "</li>";
                $selectGradeList.append(content);
            }
        }
//        for (var i = 0; i < pointList.length; i++) {
//            var content = '';
//            content += "<li data-pointId='" + pointList[i].id + "'>" + pointList[i].chapter + "</li>";
//            $knowledgeList.append(content);
//        }

        var $selectSubjectLi = $('#subject-ul>li');
        var $selectGradeLi = $('#grade-ul>li');
        var $knowledgeLi = $('#knowledge-ul>li');

        $selectSubjectLi.on('click', function() {
            var subject = $(this).text();
            selectKnowledge(subject);
            changeSearchBar($(this), 'subject');
        });

        $selectGradeLi.on('click', function() {
            changeSearchBar($(this), 'grade');
        });

        $knowledgeLi.on('click', function() {
            changeSearchBar($(this), 'knowledge');
        });
    }
    // 演示js
    $('#y_courses .btn-try').click(function() {
        tryPlayYCourse();
    });
  
    var curtUrl = window.location.href;
    if(curtUrl.indexOf('type=2') > 0){
    	$('#boughtExclt').trigger('click');
    }else if(curtUrl.indexOf('type=1') > 0){
    	$('#myExclt').trigger('click');
    }else{
    	getSubjectCourseInfo('', 1, 12);
    }
    
/*    $('#example').bootstrapPaginator({
        currentPage: 1,
        totalPages: 1,
        itemTexts: function(type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "<";
                case "next":
                    return ">";
                case "last":
                    return "末页" + page;
                case "page":
                    return page;
            }
        },
        onPageClicked: function(e, originalEvent, type, page) {}
    });*/

    getGradeInfoOfTeacher(function(data) {
        if (data.classInfoList) {
            var normalClass = [];
            var interestClass = [];
            for (var i in data.classInfoList) {
                var v = data.classInfoList[i];

                switch (v.classtype) {
                    case 1:
                        {
                            normalClass.push(v);
                            break;
                        }
                    case 2:
                        {
                            interestClass.push(v);
                            break;
                        }
                }
            }
        }
    });


});


function getMyExcellentLesson(type,obj){
	$('.account-btn').removeClass('currentItem');
	$(obj).children('.account-btn').addClass('currentItem');
	myType = type;
	getSubjectCourseInfo('', 1, 12, myType);
}


function clickTreeNode(event, treeId, node) {
    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    treeObj.cancelSelectedNode();
    treeObj.checkNode(node, !node.getCheckStatus().checked, false);
}

function submitPushDir() {
    if (currentCourseId == null) {
        MessageBox("尚未选择课程！", -1);
    }
    var teacherTree = $.fn.zTree.getZTreeObj("teacherDirUl");
    //    var schoolTree = $.fn.zTree.getZTreeObj("schoolDirUl");
    var selectedNodes = teacherTree.getCheckedNodes(true);
    //    selectedNodes = selectedNodes.concat(schoolTree.getCheckedNodes(true));
    if (selectedNodes.length > 0) {
        MessageBox("推送中...", 0);
        var destDirs = selectedNodes.map(function(node) {
            return node.id;
        }).join(',');
        $.ajax({
            url: "/directory/pushLessonFromCloud",
            type: "post",
            data: {
                "lessonId": currentCourseId,
                "destDirIds": destDirs
            },
            success: function(response) {
            	if(getCookie('pushclass') == "pushT"){
            		MessageBox('推送完成',1);
            	}else{
            		MessageBox('推送完成', 2, 'push', '老师您好，您还需要返回 <strong class="msg-orange">备课空间</strong>，点击刚才新建的该课程，选择工具栏上的<strong class="msg-orange">推送到班级</strong>之后，学生们才能在他们自己的<strong class="msg-orange">班级课程</strong>里看到这个视频哦~~');
            	}
            	closeDialog(newCourseclass);
            },
            error: function(e) {
                MessageBox('推送失败!', -1);
            }
        })
    } else {
        MessageBox("请选择至少一个文件夹进行推送！", -1);
    }

}


var polyv_player = null;

function selectKnowledge(subject) {
    switch (subject) {
        case "语文":
            {
                /*$('#knowledge-ul>li').hide();*/
                $('.chinese').show();
                break;
            }
        case "数学":
            {
                /* $('#knowledge-ul>li').hide();*/
                $('.math').show();
                break;
            }
        case "英语":
            {
                /*$('#knowledge-ul>li').hide();*/
                $('.english').show();
                break;
            }
        case "物理":
            {
                /*  $('#knowledge-ul>li').hide();*/
                $('.physics').show();
                break;
            }
        case "化学":
            {
                /*  $('#knowledge-ul>li').hide();*/
                $('.chemistry').show();
                break;
            }
        case "生物":
            {
                /*$('#knowledge-ul>li').hide();*/
                $('.biology').show();
                break;
            }
        case "政治":
            {
                /*$('#knowledge-ul>li').hide();*/
                $('.politics').show();
                break;
            }
        case "历史":
            {
                /* $('#knowledge-ul>li').hide();*/
                $('.history').show();
                break;
            }
        case "地理":
            {
                /* $('#knowledge-ul>li').hide();*/
                $('.geography').show();
                break;
            }
        case "其他":
            {
                /* $('#knowledge-ul>li').hide();*/
                $('.etc').show();
                break;
            }
        case "科学":
            {
                /*  $('#knowledge-ul>li').hide();*/
                $('.science').show();
                break;
            }
        case "音美体":
            {
                /*$('#knowledge-ul>li').hide();*/
                $('.emt').show();
                break;
            }
    }
}


function removeActive(str) {
    if (str == 'subject') {
        $('#subject-ul').find('.active').removeClass('active');
        $('#knowledge-ul>li').show();
        $('.knowledge').remove();
        $('#knowledge-ul').find('.active').removeClass('active');
        //checkHight();
    } else if (str == 'grade') {
        $('#grade-ul').find('.active').removeClass('active');
    } else {
        $('#knowledge-ul').find('.active').removeClass('active');
    }
    refreshCloud();
    getSubjectCourseInfo('', 1, 12);
}

function changeSearchBar(element, str) {

    if (!element.hasClass('active')) {
        element.parent().find('.active').removeClass('active');
        element.addClass('active');
        if ($('.retrieval-search-bar').prevAll('.' + str).length == 0) {
            $('.retrieval-search-bar').before('<div class="retrieval-detail ' + str + '" data-status=' + str + '><span>' + element.text() + '</span><img src="/img/error-grey.png"></div>');
        } else {
            $('.retrieval-search-bar').prevAll('.' + str).find('span').text(element.text());
        }
        if (str == 'subject') {
            $('.knowledge').remove();
            $('.knowledge-ul').find('.active').removeClass('active');
            refreshCloud();
        }
        getSubjectCourseInfo('', 1, 12);
    }
}

function refreshCloud() {
    var $knowledgeList = $('.knowledge-ul');
    var status = $('.retrieval-search').val();
    var subjectId = $('#subject-ul .active').attr('data-subjectId') || null;
    $.ajax({
        url: '/cloud/infos1.do',
        type: 'POST',
        dataType: 'json',
        data: {
            "stageId": status,
            "subjectId": subjectId
        },
        success: function(data) {
            var pointList = data.examPointList;
            $knowledgeList.empty();
            $('.retrieval-knowledge').show();
            if (pointList.length == 0) {
                $('.retrieval-knowledge').hide();
            } else {
                for (var i = 0; i < pointList.length; i++) {
                    var content = '';
                    content += "<li data-pointId='" + pointList[i].id + "'>" + pointList[i].chapter + "</li>";
                    $knowledgeList.append(content);
                }
            }
            var $knowledgeLi = $('.knowledge-ul>li');
            $knowledgeLi.on('click', function() {
                changeSearchBar($(this), 'knowledge');
            });
            //checkHight();
        },
        error: function() {
            console.log('infos error');
        }
    });
}

function gotoHelpPage(){
	  location.href = "/business/reverse/user/userHelp.jsp";
}

function getSubjectCourseInfo(word, page, size, mytype) {
    if (word != null) {
        getSubjectCourseInfo.searchTerm = word;
    }
    if(mytype){
    	myType = mytype;
    }
    $('#y_courses').addClass('hardloadingb');
    $('#y_courses').html('');
    var subjectId = $('.subject-ul .active').attr('data-subjectid') || null;
    var schoolLevel = $('.retrieval-search').val();
    var gradeId = $('.grade-ul .active').attr('data-status') || null;
    var knowledgePointId = $('#knowledge-ul .active').attr('data-pointId') || null;
    if(myType == 1){
    	size = 15;
    }
   $.ajax({
	   url:'/emarket/selLesson.do', 
	   type:'post',
	   dataType:'json',
	   data:{
        "gradeid": gradeId,
        "schoolLevel": schoolLevel,
        "subjectid": subjectId,
        "knowledgePointid": knowledgePointId,
        "keyword": getSubjectCourseInfo.searchTerm,
        "ismytype": myType,
        "page": page,
        "pageSize": size
	   },
	   
   	   success:function(data){
	   		$('#y_courses').removeClass('hardloadingb');
	        var html ='';
	        if(myType == 0){
	        	html = getAllExcellent(data);
	        }else if(myType == 1){
	        	html = getMyExcellent(data);
	        }else if(myType == 2){
	        	html = getBoughtExcellent(data);
	        }else if(myType == 3){
                html = getAllExcellent(data);
            }
	        $('#y_courses').html(html);
	        if(myType == 1){
	        	renderToolbar();
	        }
	        resetPaginator(Math.ceil(data.total/data.pageSize));
   	   }
   });
}



function getAllExcellent(data){
	var course = data.rows;
	var html = '';
	for(var i = 0; i < course.length; i ++){
		var imgUrl = '/img/default_cover.jpg';
		if(course[i].image != null && course[i].image != ''){
			imgUrl = course[i].image;
		}
		var personInfo = '';
        if(course[i].isopen == '1'){
            personInfo = '<div class="course-ownner ellipsis" title="'+ course[i].user.value +'">'+ course[i].user.value +'</div>'+
            '<div class="location ellipsis" title="'+course[i].school.value+'">'+course[i].school.value+'</div>';
        }else{
            personInfo = '<div class="course-ownner ellipsis" title="">匿名</div>'+
            '<div class="location ellipsis" title="">匿名</div>';
        }
    	html += '<div class="col-xs-3 subject-container" onclick="showBugPage(\''+course[i].id+'\');">'+
			        '<div class="subject-img-container">'+
			            '<img class="subject-img" src="'+imgUrl+'"></div>'+
			        '<div class="name_price_ctn"><span class="sale-coursename ellipsis" title="'+course[i].name+'">'+course[i].name+'</span><span class="sale-price"><label style="font-size:14px">￥</label>'+course[i].price+'</span></div>'+
			        personInfo +
			    '</div>';
    };
    return html;
}

function getMyExcellent(data){
	var lesson = data.rows;
	var html = '';
	for(var i = 0; i < lesson.length; i ++){
		var imgSrc = '/img/K6KT/filecourse.png';
        if(lesson[i].image != null && lesson[i].image != ''){
			imgSrc = lesson[i].image;
		}
    	html += '<div class="course-container lesson" style="margin:7px 15px;" file-id="'+lesson[i].id+'" title="'+lesson[i].name+'">'+
				    '<div class="file-cover" onclick="play(\''+lesson[i].lessonid+'\')">'+
				        '<img class="file-img" src="'+imgSrc+'">'+
				        '<div class="file-info">'+
				            '<div class="hover-title">'+
				                '<span class="ellipsis" style="max-width: 10em;">'+lesson[i].name+'</span>'+
				            '</div>'+
				            '<div class="files">'+
				                '<div>'+
				                    '<span>视频:'+lesson[i].videoCount+'</span>'+
				                '</div>'+
				                '<div>'+
				                    '<span>文档:'+lesson[i].docCount+'</span>'+
				                '</div>'+
				                '<div>'+
				                    '<span>习题:'+lesson[i].questionCount+'</span>'+
				                '</div>'+
				            '</div>'+
				            '<div class="play">'+
				                '<img src="/img/play_icon.png"/>'+
				            '</div>'+
				        '</div>'+
				    '</div>'+
				    '<div class="file-title ellipsis">'+lesson[i].name+'</div>'+
				    '<ul class="file-toolbar"></ul>'+
				'</div>';
    };
    return html;
}

function getBoughtExcellent(data){
	var course = data.rows;
	var html = '';
	//var nowDate = getNowTime();
	for(var i = 0; i < course.length; i ++){
		var imgUrl = '/img/default_cover.jpg';//默认图片
		if(course[i].image != null && course[i].image != ''){
			imgUrl = course[i].image;
		}
		var op = '';
		if(!course[i].isexpired){
			op = 'class="recmd-buy">详情';
		}else{
			op = 'class="overdue"> 过期';
		}
    	html += '<div class="col-xs-3 subject-container" onclick="buyview(\''+course[i].lessonid+'\');">'+
			        '<div class="subject-img-container">'+
			            '<img class="subject-img" src="'+ imgUrl +'"></div>'+
			        '<div class="sale-coursename ellipsis">'+course[i].name+'</div>'+
			        '<button style="width:55px;height: 26px;" '+ op +'</button>'+
			        '<div class="sale-price" style="font-size:14px;margin-top:0;line-height:0.8;display: block;text-align: left;margin-left: 5px;">￥'+course[i].price+'</div>'+
			    '</div>';
    };
    return html;
}

/*function getNowTime(){
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth()+1;
	var date = now.getDate();
	var hour = now.getHours();
	var minite = now.getMinutes();
	return year+'-'+month+'-'+date+' '+hour+':'+minite;
}*/
function showBugPage(courseId){
	window.location.href='/emarket/buyLessonDetail.do?goodId='+courseId;
}
var currentpage = 1;
function resetPaginator(totalPages) {
    if (totalPages <= 0) {
        totalPages = 1;
    }
    if(currentpage >= totalPages){
		currentpage = totalPages;
	}
    $('#example').bootstrapPaginator("setOptions", {
        currentPage: currentpage,
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
        	currentpage = page;
            getSubjectCourseInfo(null, page, 12,myType);
        }
    });
}


/*function showSubjectCourseInfo(data) {
    var target = $('#y_courses');
    if (data.rows) {
        var html = "";
        for (var i in data.rows) {
            var co = data.rows[i];
            html += '<div class="col-xs-3 subject-container"><div class="subject-img-container"><img class="subject-img" src="' + co.imageURL + '"></div>';
            html += '<div class="context">' + co.coursename + '</div><a class="btn btn-default btn-try" onclick="tryPlayYCourse($(this));" vurl="' + co.path + '" vid="'+co.id+'">试看</a>';
            html += '<a class="btn btn-primary btn-use" onclick="currentCourseId=' + co.id + ';showDialog(newCourseclass);">推送</a></div>';
        }
    }
    target.html(html);
}*/

function getImage(subjectName) {
    var url = '';
    switch (subjectName) {
        case '语文':
            {
                url = '/img/course/chinese.png';
                break;
            }
        case '数学':
            {
                url = '/img/course/math.png';
                break;
            }
        case '英语':
            {
                url = '/img/course/english.png';
                break;
            }
        case '物理':
            {
                url = '/img/course/physics.png';
                break;
            }
        case '化学':
            {
                url = '/img/course/chemistry.png';
                break;
            }
        case '生物':
            {
                url = '/img/course/biology.png';
                break;
            }
        case '地理':
            {
                url = '/img/course/geography.png';
                break;
            }
        case '历史':
            {
                url = '/img/course/history.png';
                break;
            }
        case '政治':
            {
                url = '/img/course/politics.png';
                break;
            }
        case '音美体':
            {
                url = '/img/course/art.png';
                break;
            }
        case '科学':
            {
                url = '/img/course/others.png';
                break;
            }
        case '其他':
            {
                url = '/img/course/others.png';
                break;
            }
    }
    return url;
}
var vid;
function tryPlayYCourse(ob) {
    var id = "#YCourse_player";
    var vc = $(id);
    var url = ob.attr('vurl');
    vid = ob.attr('vid');
    vc.html('<div id="mainv1a9bb5bed580b310a07cb758887f0d89_1_div" style="padding:10px;background:#fff;border-radius:4px;"></div><a class="close-dialog" onclick="closeDialog(' + "'" + id + "'" + ')" style="background:#000;opacity:0.8;position:absolute;color:white;top:6px;right:0px;border-radius: 100px;padding: 2px 5px;">X</a>');
    if (!polyv_player) {
        polyv_player = new polyvObject.swf(url, "mainv1a9bb5bed580b310a07cb758887f0d89_1", "860", "605", "#ffffff");
    } else {
        polyv_player.source = url;
        polyvObject.writePlayer(0);
    }
    showDialog('#YCourse_player');

    var timer = setTimeout(function cloudCourseScore(){
        $.ajax({
            url: '/experience/studentScoreLog.do',
            type: 'POST',
            dataType: 'json',
            async:false,
            data: {
            	'relateId': vid,
            	'scoretype' : 0
            },
            success: function(data) {
            	if (data.resultcode==0) {
            		scoreManager(data.desc,data.score);
            	}
            },
            error: function() {
            }
        });

    },1000);


    $('.close-dialog').on('click', function() {
    	clearTimeout(timer);
        vc.empty();
    });
}


function addCourseClass(list) {
    var content = "<ul class='add-course-class'>";
    for (var i = 0; i < list.length; i++) {
        content += "<li><input type='checkbox' data-id='" + list[i].classtype + "," + list[i].id + "'>" + list[i].classname + "</li>";
    }
    content += "</ul>";
    return content;
}
