

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
////////////推送到精品课程/////////////////
var newCourseclass = '.inside-dialog';
var currentCourseId = 0;
var courseContent ='';
var intReg = /^[0-9]*[1-9][0-9]*$/;
var floatReg = /^-?\d+[\.\d]?\d{0,2}$/;

function checkIsBlanck(){
	var stageUl = $('#stages-ul li input:checked').length;
	var gradeUl = $('#grades-ul li input:checked').length;
	var coursePrice = $('#course-price').val();
	//var courseValidate = $('#course-validity').val();
	//var content = ue.getContent();
	
	var courseName = $('#course-name').val();
	if($('#course-name').length > 0 && !$('#course-name').is(':hidden')){
		if(courseName == ''){
			$('#course-name').addClass('error-border');
			$('#course-name-require').show();
			return false;
		}else{
			$('#course-name').removeClass('error-border');
			$('#course-name-require').remove();
		}
	}
	if(stageUl < 1){
		$('#stage-require').show();
		$('#retrieval-stage').addClass('error-border');
		return false;
	}else{
		$('#stage-require').hide();
		$('#retrieval-stage').removeClass('error-border');
	}
	if(gradeUl < 1){
		$('#grade-require').show();
		$('#retrieval-grade').addClass('error-border');
		return false;
	}else{
		$('#grade-require').hide();
		$('#retrieval-grade').removeClass('error-border');
	}
	
	var flag2 = false;
	if(coursePrice == null || coursePrice == ''){
		$('#course-price').addClass('error-border');
    	flag2 = msgShow($('#price-require'),'*  输入提取金额');
	}else{
		/*if(!floatReg.test(coursePrice)){
			$('#course-price').addClass('error-border');
			flag2 = msgShow($('#price-require'),'*  数值必须为整数或小数，小数点后不超过2位');
		}else{
			if(parseFloat(coursePrice) <= 0){
		    	$('#course-price').addClass('error-border');
		    	flag2 = msgShow($('#price-require'),'*  提取金额必须大于等于0.01');
			}else{
		    	$('#course-price').removeClass('error-border');
		    	flag2 = msgHide($('#price-require'));
			}
		}*/
		flag2 = true;
	}
	
	/*var flag3 = false;
	if(courseValidate == ''){
		$('#course-validity').val(0);
	}else{
		if(!intReg.test(courseValidate) && parseInt(courseValidate) != 0){
			$('#course-validity').addClass('error-border');
			flag3 = msgShow($('#validity-require'),'*  输入整数');
		}else{
		    $('#course-validity').removeClass('error-border');
		    flag3 = msgHide($('#validity-require'));
		}
	}*/
	if(flag2){
		return true;
	}else{
		return false;
	}
}
$(function() {

	//var $selectStyStionList = $('#stages-ul');
    var $selectSubjectList = $('#subjects-ul');
    var $selectGradeList = $('#grades-ul');
    var $knowledgeList = $('#knowledges-ul');
    
    var $selectElem;
    var elemHtml='';
    var stageHtml = '';
    var gradeHtml = '';
    var subjectHtml = '';

    initCoursePage();
    
    function initCoursePage($selectElem,elemHtml,stageHtml,gradeHtml,subjectHtml){
    	var stageList = '';
    	$('#stages-ul li input').each(function(){
    		if($(this).prop('checked')){
    			stageList += ($(this).val() + ',');
    		}
    	});
    	var subjectIdList = '';
    	$('#subjects-ul li input').each(function(){
    		if($(this).prop('checked')){
    			subjectIdList += ($(this).val() + ',');
    		}
    	});
    	$.ajax({
        	url:'/emarket/baseList.do',
            type: 'POST',
            dataType: 'json',
            data: {'stageIds':stageList,'subjectIds':subjectIdList},
            success: function(data) {
                showdata(data);
                if(saleCourseDialog){
                	if($selectElem.prop('id') != 'stages-ul'){
                		if($selectElem.prop('id') != 'grades-ul'){
                			if($selectElem.prop('id') != 'subjects-ul'){
                				$('#select-course').find($selectElem).html(elemHtml);
                			}
                			$('#select-course').find('#subjects-ul').html(subjectHtml);
                		}
                		$('#select-course').find('#grades-ul').html(gradeHtml);
                	}
                	$('#select-course').find('#stages-ul').html(stageHtml);
                }
            },
            error: function() {
                console.log('selCloudList error');
            }
        });
    }
    
    $('body').on('click','#subjects-ul li input, #grades-ul li input, #stages-ul li input',function(){
    	if($(this).prop('checked') == false){
    		$(this).attr('checked',false);
    	}else{
    		$(this).attr('checked',true);
    	}
    	stageHtml = $('#stages-ul').html();
    	gradeHtml = $('#grades-ul').html();
    	subjectHtml = $('#subjects-ul').html();
    	$selectElem = $(this).parent().parent();
    	elemHtml = $selectElem.html();
    	initCoursePage($selectElem,elemHtml,stageHtml,gradeHtml,subjectHtml);
    });

/*    $('body').on('click', '.retrieval-detail>img', function() {
        var content = $(this).parent().attr('data-status');
        $(this).parent().remove();
        removeActive(content);
    });*/

    $('body').on('click', '#knowledges-down', function() {
        $('#retrieval-knowledges').css('height', 'auto');
        $(this).text('收起');
        $(this).removeClass('knowledge-down').addClass('knowledge-up').attr('id','knowledges-up');
    });
    $('body').on('click', '#knowledges-up', function() {
        $('#retrieval-knowledges').css('height', '39px');
        $(this).text('更多');
        $(this).removeClass('knowledge-up').addClass('knowledge-down').attr('id','knowledges-down');
    });

    function showdata(data) {
    	var styStionList = data.styStionList;
        var subjectList = data.subject;
        var gradeList = data.grade;
        var pointList = data.examPointList;
                
        $selectSubjectList.empty();
        $selectGradeList.empty();
        $knowledgeList.empty();
        
        
        for (var i = 0; i < subjectList.length; i++) {
            var content = '';
            content += "<li data-subjectId='" + subjectList[i].id + "'>"+ "<input type='checkbox' value='" + subjectList[i].id + "'/>" + subjectList[i].value + "</li>";
            $selectSubjectList.append(content);
        }
        
        if (gradeList.length == 0) {
            $('#retrieval-grade').hide();
        } else {
            for (var i = 0; i < gradeList.length; i++) {
                var content = '';
                content += "<li data-status='" + gradeList[i].id + "'>"+ "<input type='checkbox' value='" + gradeList[i].id + "'/>" + gradeList[i].value + "</li>";
                $selectGradeList.append(content);
            }
        }
//        for (var i = 0; i < pointList.length; i++) {
//        	var content = '';
//            content += "<li data-pointId='" + pointList[i].id + "' data-subjectId='"+ pointList[i].subjectId +"'>"+ "<input type='checkbox' value='" + pointList[i].id + "'/>" + pointList[i].chapter + "</li>";
//            $knowledgeList.append(content);
//        }
    }
    

    $('#example').bootstrapPaginator({
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
    });

    $('#retrieval-search').change(function() {
        var status = $('#retrieval-search').val();
        $('.retrieval-detail>img').click();
        $('.knowledge-ul>li').show();
        $('.knowledge-up').click();
        $('.knowledge-down').show();
    	$.ajax({
        	url:'/emarket/baseList.do',
            type: 'POST',
            dataType: 'json',
            data: {'stageIds':status,'subjectIds':''},
            success: function(data) {
                showdata(data);
            },
            error: function() {
                console.log('selCloudList error');
            }
        });
        getSubjectCourseInfo('', 1, 12);
    });
    
});

function editExcellentLesson(courseID){
	$('#select-course').show();
	//$('#courseName').show();
	getTeacherIntro();
	$.ajax({
		url:'/emarket/selSingleLesson.do',
		data:{'goodid':courseID},
		type:'post',
		success:function(data){
			$('#stages-ul li input').prop('checked',false);
			var stage = data.stargArgs;
			for(var i = 0; i < stage.length; i++){
				$('#stages-ul li input').each(function(){
					if(stage[i] == $(this).val()){
						$(this).prop('checked',true);
					}
				});
			}
			var grade = data.gradeArgs;
			for(var i = 0; i < grade.length; i++){
				$('#grades-ul li input').each(function(){
					if(grade[i] == $(this).val()){
						$(this).prop('checked',true);
					}
				});
			}
			var subject = data.subjectArgs;
			var selectedSubject = new Array();
			for(var i = 0; i < subject.length; i++){
				$('#subjects-ul li input').each(function(){
					if(subject[i] == $(this).val()){
						$(this).prop('checked',true);
						selectedSubject.push($(this).val());
					}
				});
			}
			/*var knowledges = data.examoutlineArgs;
			if(knowledges.length > 0){
				for(var i = 0; i < knowledges.length; i++){
					$('#knowledges-ul li input').each(function(){
						if(knowledges[i] == $(this).val()){
							$(this).prop('checked',true);
						}
					});
				}
			}
			$('#knowledges-ul li input').each(function(){
				if(selectedSubject.length > 0){
					var exist = $.inArray($(this).parent().attr('data-subjectId'),selectedSubject);
	            	if(exist < 0){
	            		$(this).parent().hide();
	            	}
	            }
			});	*/

			$('#course-price option').each(function(){
				if($(this).val() == data.price){
					$(this).attr('selected','selected');
				}
			});
			$('#course-name').val(data.courseName);
			var isOpenInfo = data.isopen;
			$('input[name="openinfo"]').each(function(){
				if($(this).val() == isOpenInfo){
					$(this).prop('checked',true);
				}
			});
			calculatePrice();
			ue.setContent(data.desc);
			courseContent = data.desc;
		}
	});
    saleCourseDialog = showPushingDialogStep1('编辑:推送至电子超市',$('#select-course'),courseID);
    saleCourseDialog.showModal();
    prependProtocal();
}
function getTeacherIntro(){
	$.ajax({
		url:'/emarket/introduction.do',
		data:{},
		success:function(data){
			$('#teach-descript').val(data.introduction);
			$('#teacherinfo').html('( '+data.teacherinfo+' )');
		}
	});
}
function showPushingDialogStep1(title,content,courseID){
	var d = dialog({
        title: title,
        content: content,
        width: 833,
        height: 546,
        okValue: '下一步',
        ok: function () {
        	saveExcellentCourse(courseID);
            return false;
        },
        cancelValue: '取消',
        cancel: function () {
        	$('input:checked').each(function(){
        		if($(this).prop('checked')){
        			$(this).prop('checked',false);
        		}
        	});
        	$('#course-price').val('');
        	$('#course-validity').val('');
        	$('#course-name').html('');
        	ue.setContent('');
            d.close();
            return false;
        }
    });
    return d;
}
function saveExcellentCourse(courseID){
	if(!$('#agree').prop('checked')){
		return;
	}
	if(checkIsBlanck()){
		var stageIdList ='';
		$('#stages-ul li input').each(function(){
			if($(this).prop('checked')){
				stageIdList += ($(this).val() + ',');
			}
		});
		var subjectIdList = '';
		$('#subjects-ul li input').each(function(){
			if($(this).prop('checked')){
				subjectIdList += ($(this).val() + ',');
			}
		});
		var gradeIdList = '';
		$('#grades-ul li input').each(function(){
			if($(this).prop('checked')){
				gradeIdList += ($(this).val() + ',');
			}
		});
		var examOutlineIdList = '';
		/*$('#knowledges-ul li input').each(function(){
			if($(this).prop('checked')){
				examOutlineIdList += ($(this).val() + ',');
			}
		});*/
		/*var expiretime;
		if ($('#course-validity').val()=='') {
			expiretime = 0;
		} else {
			expiretime = $('#course-validity').val();
		}*/
		var isOpen = '1';
		$('input[name="openinfo"]').each(function(){
			if($(this).prop('checked')){
				isOpen = $(this).val();
			}
		});
		$.ajax({
			url:'/emarket/addLesson.do',
			type:'post',
			data:{
				'lessonId':'',
				'goodid':courseID,
				'stageids':stageIdList,  
				'subjectIds':subjectIdList, 
				'gradeIds':gradeIdList,  
				'examOutlineIds':examOutlineIdList,  
				'price':$('#course-price').val(),  
				'expiretime':6,  
				'lessonContent':ue.getContent(),  
				'editType':2,//编辑
				'lessonName':$('#course-name').val(),
				'isopen':isOpen
				},
			success:function(data){
				saleCourseDialog.close();
				$('#course-name').html('');
				window.location.href = '/lesson/edit.do?lessonId='+data.lessonId;
			}
		});
	}
}

function saveContainer() {
	var descript = $('#teach-descript').val();
	$.ajax({
		url:'/emarket/updateIntroduction.do',
		type:'post',
		data:{
			'introduce':descript
		},
		success:function(data){
			if (data.resultCode==0) {
				//alert("教师介绍保存成功！");
				//$('#teach-descript').attr('disabled','disabled');
			} else {
				alert("教师介绍保存失败！");
			}
		}
	});
	
}
function fastPushExcellent(){
	$('#select-course').show();
	//$('li[data-stageid="1"]').children('input').prop('checked',true);
	$('#isOpen').children('input[value="1"]').prop('checked',true);
	$('#course-name').val('');
	$('#courseName').show();
	calculatePrice();	
	getTeacherIntro();
	saleCourseDialog = fastPushingDialogStep1('快速上传至电子超市',$('#select-course'));
    saleCourseDialog.showModal();
    prependProtocal();
}

function prependProtocal(){
	//$('.ui-dialog-button').prepend('');
	$('button[data-id="ok"]').removeClass('ui-dialog-autofocus').addClass('notallow');
}
function switchAgree(){
	if($('#agree').prop('checked')){
		$('button[data-id="ok"]').removeClass('notallow').addClass('ui-dialog-autofocus');
	}else{
		$('button[data-id="ok"]').removeClass('ui-dialog-autofocus').addClass('notallow');
	}
}

function fastPushingDialogStep1(title,content){
	var d = dialog({
        title: title,
        content: content,
        width: 833,
        height: 586,
        okValue: '下一步',
        ok: function () {
        	saveFastExcellent();
            return false;
        },
        cancelValue: '取消',
        cancel: function () {
        	$('input:checked').each(function(){
        		if($(this).prop('checked')){
        			$(this).prop('checked',false);
        		}
        	});
        	$('li[data-stageid="1"]').children('input').prop('checked',false);
        	$('#isOpen').children('input[value="1"]').prop('checked',false);
        	$('#course-price').val('');
        	$('#course-validity').val('');
        	$('#course-name').html('');
        	ue.setContent('');
        	$('#courseName').hide();
            d.close();
            return false;
        }
    });
    return d;
}
function saveFastExcellent(){
	if(!$('#agree').prop('checked')){
		return;
	}
	if(checkIsBlanck()){
		var stageIdList ='';
		$('#stages-ul li input').each(function(){
			if($(this).prop('checked')){
				stageIdList += ($(this).val() + ',');
			}
		});
		var subjectIdList = '';
		$('#subjects-ul li input').each(function(){
			if($(this).prop('checked')){
				subjectIdList += ($(this).val() + ',');
			}
		});
		var gradeIdList = '';
		$('#grades-ul li input').each(function(){
			if($(this).prop('checked')){
				gradeIdList += ($(this).val() + ',');
			}
		});
		var examOutlineIdList = '';
		var isOpen = '1';
		$('input[name="openinfo"]').each(function(){
			if($(this).prop('checked')){
				isOpen = $(this).val();
			}
		});
		$.ajax({
			url:'/emarket/addLesson.do',
			type:'POST',
			data:{
				'lessonId':'',
				'goodid':'',
				'stageids':stageIdList,  
				'subjectIds':subjectIdList, 
				'gradeIds':gradeIdList,  
				'examOutlineIds':examOutlineIdList,  
				'price':$('#course-price').val(),  
				'expiretime':6,  //默认6个月
				'lessonContent':ue.getContent(),  
				'editType':3,
				'lessonName':$('#course-name').val(),
				'isopen':isOpen
				},
			success:function(data){
				window.location.href = '/lesson/edit.do?lessonId='+data.lessonId;
				clearDialogData();
				$('#courseName').hide();
				saleCourseDialog.close();
			}
		});
	}
}

function calculatePrice(){
	var price = parseInt($('#course-price').val());
	if(!price){
		price = 0;
	}
	$('#actual-price').html((price*0.7).toFixed(2));
}