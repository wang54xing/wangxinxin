/**
 * Created by albin on 2018/3/19.
 */
define('pkRuleSet', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pkRuleSet = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');
    var num=0;


    pkRuleSet.init = function () {
    	
    	pkRuleSet.getDefaultTerm();//获取次id
		pkRuleSet.getSubjectSetSubject();
        pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
        pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();

    	//点击教师设置
        $("body").on("click", "#teacherSet", function () {
        	pkRuleSet.getAutoTeaSet();
        	$('.gz_ct_2').show().siblings('.gz_ct').hide();
            $(this).addClass('active').siblings().removeClass('active');
        });
        
        //教师周任课天数-checkbox
        $("body").on("click", ".tc_zr_ck", function () {
			var m = $(this).hasClass('active');
			var other = $(this).siblings('.tc-zr-set');
			if(m){
				other.hide();
				pkRuleSet.updAutoSet('1','0');
			}else{
				other.show();
				pkRuleSet.updAutoSet('1','1');
			}
		});
        
        //教师周任课天数编辑弹窗打开
        $("body").on("click", ".tc_ts_edit", function () {
        	pkRuleSet.getAllTeachers('*');
        	pkRuleSet.getCTeaList();
			$('.ts-popup').show();
            $('.bg').show();
        });
        
        //教师周任课天数点击教师搜索框
		$("body").on('click','#searchTea',function () {
			var userName = $(this).prev().val();
			pkRuleSet.getAllTeachers(userName);
		});
		
		//教师周任课天数--添加教师
		$("body").on('click','.jsw_add',function () {
		    $.each($(".tc_bx option:selected"),function (i,item) {
				$('.tc_yx').append('<li teaId="' + $(item).attr('teaId') + '">' +$(item).text()+'<a class="tc_remove">&times;</a></li>')
            });
		    var param = {};
		    param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	param.weekCourse = $('#weekCourse').val() + '';
        	var userIds = '';
        	$.each($("#cTeaList li"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        	});
        	if(userIds == null || userIds == '' || userIds == undefined){
        		layer.msg('请选择教师');
        		return ;
        	}
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaWeek.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getAllTeachers('*');
        		}
            });
        });
		
		//教师周任课天数--弹窗删除已选教师
		$("body").on('click','.tc_remove',function (e) {
			$(this).parent().remove();
			var param = {};
			param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	param.weekCourse = $('#weekCourse').val() + '';
        	var userIds = '';
        	$.each($("#cTeaList li"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        	});
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaWeek.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getAllTeachers('*');
        		}
            });
		});
		
        //已选教师天数select发生改变
		$("body").on('change','#weekCourse',function () {
			pkRuleSet.getCTeaList();
        });
		
		
        //教师日最大连课节数-checkbox
        $("body").on("click", ".tc_lk_ck", function () {
            var m = $(this).hasClass('active');
            var other = $(this).siblings('.tc-lk-set');
            if(m){
                other.hide();
                pkRuleSet.updAutoSet('2','0');
            }else{
                other.show();
                pkRuleSet.updAutoSet('2','1');
            }
        });
        
        //教师日最大连课节数-编辑弹窗打开
        $("body").on("click", ".tc_lk_edit", function () {
        	pkRuleSet.getDTeachers('*');
        	pkRuleSet.getDCTeaList();
        	$('.lk-popup').show();
            $('.bg').show();
        });
        
        //教师日最大连课节数-点击教师搜索框
		$("body").on('click','#searchDTea',function () {
			var userName = $(this).prev().val();
			pkRuleSet.getDTeachers(userName);
		});
        
        //教师日最大连课节数--添加教师
		$("body").on('click','.lk_add',function () {
		    $.each($(".lk_bx option:selected"),function (i,item) {
				$('.lk_yx').append('<li teaId="' + $(item).attr('teaId') + '">' +$(item).text()+'<a class="tc_remove">&times;</a></li>')
            });
		    var param = {};
		    param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	param.courseNum = $('#courseNum').val() + '';
        	var userIds = '';
        	$.each($("#dCTeaList li"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        	});
        	if(userIds == null || userIds == '' || userIds == undefined){
        		layer.msg('请选择教师');
        		return ;
        	}
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaDay.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getDTeachers('*');
        			pkRuleSet.getDCTeaList();
        		}
            });
        });
		
		//教师日最大连课节数--弹窗删除已选教师
		$("body").on('click','.lk_remove',function (e) {
			$(this).parent().remove();
			var param = {};
			param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	param.courseNum = $('#courseNum').val() + '';
        	var userIds = '';
        	$.each($("#dCTeaList li"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        	});
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaDay.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getDTeachers('*');
        			pkRuleSet.getDCTeaList();
        		}
            });
		});
        
		//教师日最大连课节数-已选教师天数select发生改变
		$("body").on('change','#courseNum',function () {
			pkRuleSet.getDCTeaList();
        });
        
        
        //教师跨班连堂-checkbox
        $("body").on("click", ".tc_kb_kb", function () {
            var m = $(this).hasClass('active');
            if(m){
            	pkRuleSet.updAutoSet('3','0');
            }else{;
            	pkRuleSet.updAutoSet('3','1');
            }
        });
        
        //教师跨班进度一致-checkbox
        $("body").on("click", ".tc_kb_ck", function () {
            var m = $(this).hasClass('active');
            var other = $(this).siblings('.tc-kb-set');
            if(m){
                other.hide();
                pkRuleSet.updAutoSet('4','0');
                pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),false);
                pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
            }else{;
                other.show();
                pkRuleSet.updAutoSet('4','1');
                pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),true);
                pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
            }
        });
        
        //教师跨班进度一致--弹窗打开
        $("body").on("click", ".tc_kb_edit", function () {
        	pkRuleSet.getStepTeachers('*');
        	pkRuleSet.getTeaStep();
            $('.kb-popup').show();
            $('.bg').show();
        });
        
        //教师跨班进度一致-点击教师搜索框
		$("body").on('click','#searchTStep',function () {
			var userName = $(this).prev().val();
			pkRuleSet.getStepTeachers(userName);
		});
		
		
		
	    //教师跨班进度一致--弹窗删除已选教师
	    $("body").on('click','.kb_remove',function (e) {
	        $(this).parent().remove();
	        var param = {};
        	param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	var userIds = '';
        	$.each($("#stepCteaList li"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        	});
//        	if(userIds == null || userIds == '' || userIds == undefined){
//        		layer.msg('请选择教师');
//        		return ;
//        	}
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaStep.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getStepTeachers('*');
                	pkRuleSet.getTeaStep();
        		}
            });
	    }); 
		
	    //教师跨班进度一致--弹窗教师选择
		$("body").on('click','.step_add',function () {
	        $.each($(".kb_bx option:selected"),function (i,item) {
	            $('.kb_yx').append('<li teaId="' + $(item).attr('teaId') + '">' + $(item).text() + '<a class="kb_remove">&times;</a></li>');
	        });
	        var param = {};
        	param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	var userIds = '';
        	$.each($("#stepCteaList li"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        	});
//        	if(userIds == null || userIds == '' || userIds == undefined){
//        		layer.msg('请选择教师');
//        		return ;
//        	}
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaStep.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getStepTeachers('*');
                	pkRuleSet.getTeaStep();
        		}
            });
	    });
		
//		//教师跨班进度一致-添加或更新教师
//        $("body").on("click", "#stepQd", function () {
//        	var param = {};
//        	param.gradeId = $("#grade").val();
//        	param.ciId = $("#cxq").val();
//        	var userIds = '';
//        	$.each($("#stepCteaList li"),function (i,item) {
//        		userIds = userIds + ',' + $(item).attr('teaId');
//        	});
////        	if(userIds == null || userIds == '' || userIds == undefined){
////        		layer.msg('请选择教师');
////        		return ;
////        	}
//        	param.userIds = userIds;
//        	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdTeaStep.do', param, function (rep) {
//        		var json = eval('(' + rep + ')');
//        		if(json.code == 200){
//        			layer.msg('操作成功');
//        			pkRuleSet.getStepTeachers('*');
//        		}
//            });
//        });
        
        //教师规则-checkbox
        $("body").on("click", ".tc_gz_ck", function () {
            var m = $(this).hasClass('active');
            var other = $(this).siblings('.tc-gz-set');
            if(m){
                other.hide();
                pkRuleSet.updAutoSet('5','0');
            }else{
                other.show();
                pkRuleSet.updAutoSet('5','1');
            }
        });
        
        //教师互斥-checkbox
        $("body").on("click", ".tc_hc_ck", function () {
            var m = $(this).hasClass('active');
            var other = $(this).siblings('.tc-hc-set');
            if(m){
                other.hide();
                pkRuleSet.updAutoSet('6','0');
            }else{
                other.show();
                pkRuleSet.updAutoSet('6','1');
            }
        });
        
        //教师互斥-打开教师互斥表格
        $("body").on("click", ".open_tc_hc", function () {
        	pkRuleSet.getTeaNutex();
    		$('.tc_group').hide();
    		$('.tc_hc_group').show();
    	});
       
        //教师互斥-关闭教师互斥表格
    	$('#back_tc_hc').click(function () {
    		$('.tc_group').show();
    		$('.tc_hc_group').hide();
    	});
    	
    	//教师互斥-新建教师互斥组弹框
    	$("body").on("click", "#new_tc_hc", function () {
    		pkRuleSet.getAllTeas();
            $('.hc-popup').show();
            $('.bg').show();
        });
	    
	    $("body").on("click", "#myList li", function () {
	    	if($(this).hasClass('active')){
	    		$(this).removeClass('active');
	    	}else{
	    		$(this).addClass('active');
	    	}
	        if ($(this).find("em").hasClass("spe-cur")) {
	            $(this).find('em').removeClass('spe-cur');
	            num--;
	        }else{
	            if(num<2){
	                $(this).find('em').addClass('spe-cur');
	                num++;
	            }
	        }
	        $('#hcTeaherNum').text(num);
	    })
        
        
    	//教师互斥-添加或更新教师
        $("body").on("click", ".queDingRen", function () {
        	var param = {};
        	param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	var userIds = '';
        	var tnum = 0;
        	$.each($(".allTeaList li.active"),function (i,item) {
        		userIds = userIds + ',' + $(item).attr('teaId');
        		tnum++;
        	});
        	if(tnum < 2){
        		layer.msg('请选择两名教师');
        		return ;
        	}
        	param.userIds = userIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addTeaMutex.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getTeaNutex();
        		}
            });
        });
    
        //教师互斥-删除教师互斥组
    	$("body").on("click", "#delNuyex", function () {
    		var param = {};
    		param.id = $(this).attr('nutexId');
    		Common.getPostBodyData('/autoPk/autoTeaSet/delTeaMutex.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getTeaNutex();
        		}
            });
        });
        
    	
    	
    	//============================================ 其他设置 ================================================
    	
    	//其他设置 - 获取设置信息
    	$("body").on("click", "#otherSet", function () {
    		pkRuleSet.getAutoOtherSet();
        });
    	
    	//其他设置 -选择可排学科(默认系统识别)
    	$("body").on("click", ".qt_xk_ck", function () {
            var m = $(this).hasClass('active');
            var other = $(this).siblings('.qt-xk-set');
            if(m){
                other.hide();
                pkRuleSet.updOtherSet('1','0');
            }else{
                other.show();
                pkRuleSet.updOtherSet('1','1');
            }
        });
    	
    	//其他设置 -选择可排学科--弹窗打开
    	$("body").on("click", ".qt_xk_edit", function () {
    		pkRuleSet.getSubjectByType();
    		pkRuleSet.getOsubCheks();
            $('.qt-xk-popup').show();
            $('.bg').show();
        });

        //点击勾选走班学科轮次一致
        $("body").on("click", "#ZouBanPro", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#ZouBanPro').hasClass("active")){
                pkRuleSet.addZouBanSetTypeIs1($('#ZouBanPro').attr("type"),false);
            }else{
                pkRuleSet.addZouBanSetTypeIs1($('#ZouBanPro').attr("type"),true);
            }
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        });

        //点击勾选走班教室数量
        $("body").on("click", "#ZouBanCount", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#ZouBanCount').hasClass("active")){
                pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#ZouBanCount').attr("type"),false,0);
            }else{
                if($('#ZouBanCountNum').val() == ""){
                    pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#ZouBanCount').attr("type"),true,0);
                }else{
                    pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#ZouBanCount').attr("type"),true,$('#ZouBanCountNum').val());
                }
            }
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        });

        //設置完走班教室数量后移走光标
        $("#ZouBanCountNum").blur(function(){
            pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#ZouBanCount').attr("type"),true,$('#ZouBanCountNum').val());
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        })

        //点击勾选教学班人数上限
        $("body").on("click", "#jxbStuCount", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#jxbStuCount').hasClass("active")){
                pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#jxbStuCount').attr("type"),false,0);
            }else{
                if($('#jxbStuCountNum').val() == ""){
                    pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#jxbStuCount').attr("type"),true,0);
                }else{
                    pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#jxbStuCount').attr("type"),true,$('#jxbStuCountNum').val());
                }
            }
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        });

        //設置完教学班人数上限后移走光标
        $("#jxbStuCountNum").blur(function(){
            pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#jxbStuCount').attr("type"),true,$('#jxbStuCountNum').val());
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        })

        //点击勾选走班教室上下浮动人数上限
        $("body").on("click", "#roomStuCount", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#roomStuCount').hasClass("active")){
                pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#roomStuCount').attr("type"),false,0);
            }else{
                if($('#roomStuCountNum').val() == ""){
                    pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#roomStuCount').attr("type"),true,0);
                }else{
                    pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#roomStuCount').attr("type"),true,$('#roomStuCountNum').val());
                }
            }
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        });

        //設置完走班教室上下浮动人数上限后移走光标
        $("#roomStuCountNum").blur(function(){
            pkRuleSet.addZouBanSetTypeIs2Or3Or4($('#roomStuCount').attr("type"),true,$('#roomStuCountNum').val());
            pkRuleSet.getZouBanSetDTOByCiIdAndGradeId();
        })


    	//点击勾选学科进度一致
        $("body").on("click", "#subjectPro", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#subjectPro').hasClass("active")){
                pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),false);
                pkRuleSet.updAutoSet('4','0');
			}else{
                pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),true);
                pkRuleSet.updAutoSet('4','1');
            }
            $('.select-sub1 li').remove();
        });

        //确定学科进度一致学科
        $("body").on("click", "#qdsubject1", function (e) {
			e.stopPropagation();
			if($('#subjectPro').hasClass("active")){
				pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),true);
                pkRuleSet.updAutoSet('4','1');
            }else{
				pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),false);
                pkRuleSet.updAutoSet('4','0');
			}
            pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
            $('.popup').hide();
            $('.bg').hide();
            $('.select-sub1 li').remove();
        });

        //确定周课时一致学科
        $("body").on("click", "#qdsubject2", function (e) {
            e.stopPropagation();
            if($('#subjectPro').hasClass("active")){
                pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),true);
            }else{
                pkRuleSet.addSubjectSetTypeIs1Or2($('#subjectPro').attr("type"),false);
            }
            pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
            $('.popup').hide();
            $('.bg').hide();
            $('.select-sub1 li').remove();
        });

        //点击勾选周课时一致
        $("body").on("click", "#WeekKs", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            $('.select-sub1 li').remove();
        });

        //点击勾选上下午课时
        $("body").on("click", "#AmPm", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#AmPm').hasClass("active")){
                pkRuleSet.addSubjectSetType3($('#subjectPro').attr("type"),false);
            }
            $('.select-sub1 li').remove();
        });

        //点击勾选上下午课时学科
        $("body").on("click", "#qdsubject3", function (e) {
            e.stopPropagation();
            if($('#AmPm').hasClass("active")){
                pkRuleSet.addSubjectSetType3($('#AmPm').attr("type"),true);
            }else{
                pkRuleSet.addSubjectSetType3($('#AmPm').attr("type"),false);
            }
            pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
            $('.popup').hide();
            $('.bg').hide();
            $('.select-sub1 li').remove();
        });

        //点击勾选互斥学科
        $("body").on("click", "#mutexSubject", function (e) {
            e.stopPropagation();
            $(this).toggleClass('active');
            if(!$('#mutexSubject').hasClass("active")){
                pkRuleSet.addSubjectSetTypeIs4($('#mutexSubject').attr("type"),false);
            }
            $('.select-sub1 li').remove();
        });
        //删除互斥学科
        $('body').on("click",'.del',function () {
            pkRuleSet.deleteSubjectSetTypeIs4(true,$(this).attr('mutexSubId'));
            pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
        });

        //增加互斥学科组
        $("body").on("click", "#qdsubject4", function (e) {
            e.stopPropagation();
            if($('#mutexSubject').hasClass("active")){
                pkRuleSet.addSubjectSetTypeIs4($('#mutexSubject').attr("type"),true);
            }else{
                pkRuleSet.addSubjectSetTypeIs4($('#mutexSubject').attr("type"),false);
            }
            pkRuleSet.getSubjectSetDTOByCiIdAndGradeId();
            $('.popup').hide();
            $('.bg').hide();
            $('.select-sub1 li').remove();
        });


    	
    	//其他设置 -选择可排学科--弹窗打开
    	$("body").on("click", ".qt_xk_edit", function () {
    		pkRuleSet.getSubjectByType();
    		pkRuleSet.getOsubCheks();
            $('.qt-xk-popup').show();
            $('.bg').show();
        });
    	
    	//--添加学科选择
    	$("body").on("click", ".kpxk", function () {
            $.each($(".xk_bx option:selected"),function (i,item) {
                $('.xk_yx').append('<li subId="' + $(item).attr('subId') + '">' + $(item).text() + '<a class="xk_remove">&times;</a></li>');
            });
            var param = {};
        	param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	var subIds = '';
        	$.each($("#xkyxList li"),function (i,item) {
        		subIds = subIds + ',' + $(item).attr('subId');
        	});
        	if(subIds == null || subIds == '' || subIds == undefined){
        		layer.msg('请选择学科');
        		return ;
        	}
        	param.subIds = subIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOsubCheks.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getSubjectByType();
            		pkRuleSet.getOsubCheks();
        		}
            });
        });

        //--删除已选学科
        $("body").on('click','.xk_remove',function () {
            $(this).parent().remove();
            var param = {};
        	param.gradeId = $("#grade").val();
        	param.ciId = $("#cxq").val();
        	var subIds = '';
        	$.each($("#xkyxList li"),function (i,item) {
        		subIds = subIds + ',' + $(item).attr('subId');
        	});
        	param.subIds = subIds;
        	Common.getPostBodyData('/autoPk/autoTeaSet/addOsubCheks.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getSubjectByType();
            		pkRuleSet.getOsubCheks();
        		}
            });
        });
    	
//    	//其他设置-添加或更新学科
//        $("body").on("click", "#osubQd", function () {
//        	var param = {};
//        	param.gradeId = $("#grade").val();
//        	param.ciId = $("#cxq").val();
//        	var subIds = '';
//        	$.each($("#xkyxList li"),function (i,item) {
//        		subIds = subIds + ',' + $(item).attr('subId');
//        	});
//        	if(subIds == null || subIds == '' || subIds == undefined){
//        		layer.msg('请选择学科');
//        		return ;
//        	}
//        	param.subIds = subIds;
//        	Common.getPostBodyData('/autoPk/autoTeaSet/addOsubCheks.do', param, function (rep) {
//        		var json = eval('(' + rep + ')');
//        		if(json.code == 200){
//        			layer.msg('操作成功');
//        		}
//            });
//        });
        
        
        //选择可排格子-check
        $("body").on("click", ".qt_gz_ck", function () {
            var m = $(this).hasClass('active');
            var other = $(this).siblings('.qt-gz-set');
            if(m){
                other.hide();
                pkRuleSet.updOtherSet('2','0');
            }else{
                other.show();
                pkRuleSet.updOtherSet('2','1');
            }
        });
    	
        //--打开可排格子弹窗
        $("body").on("click", ".qt_gz_edit", function () {
        	pkRuleSet.getGzs();
            $('.qt-gz-popup').show();
            $('.bg').show();
        });
        
        //--选中或者取消格子
        $("body").on('click','.gz_tb .gz',function () {
            $(this).toggleClass('active');
            var param = {};
            param.ciId = $('#cxq').val();
        	param.gradeId = $('#grade').val();
            param.x = $(this).attr('x');
            param.y = $(this).attr('y');
            if($(this).hasClass('active')){
            	param.status = '1';
            }else{
            	param.status = '0';
            }
            Common.getPostBodyData('/autoPk/autoTeaSet/addGz.do', param, function (rep) {
        		var json = eval('(' + rep + ')');
        		if(json.code == 200){
        			layer.msg('操作成功');
        			pkRuleSet.getGzs();
        		}
            });
        });
        
        //连堂处理(大课间与午餐不作为连堂课)
        $("body").on("click", ".qt-lt-set", function () {
            var m = $(this).hasClass('active');
            if(m){
                pkRuleSet.updOtherSet('3','0');
            }else{
                pkRuleSet.updOtherSet('3','1');
            }
        });
        
        //其他设置-----分段设置
	    $("body").on("click", ".qt_fd_ck", function () {
	        var m = $(this).hasClass('active');
	        var other = $(this).siblings('.qt-fd-set');
            if(m){
                other.hide();
                pkRuleSet.updOtherSet('5','0');
            }else{
                other.show();
                pkRuleSet.updOtherSet('5','1');
            }
	    });
        
        //多功能教室数量设置
        $("body").on("click", ".qt_js_ck", function () {
            var m=$(this).hasClass('active');
            var other=$(this).siblings('.qt-js-set');
            if(m){
                other.hide();
                pkRuleSet.updOtherSet('4','0');
            }else{
                other.show();
                pkRuleSet.updOtherSet('4','0');
            }
        });
        
        //--打开多功能教室数量设置弹窗
        $("body").on("click", ".qt_js_edit", function () {
            $('.qt-js-popup').show();
            $('.bg').show();
        });


        $("body").on("click", ".qt_fd_edit", function () {//打开弹窗
            pkRuleSet.getPartClassInfo();
            $('.qt-fd-popup').show();
            $('.bg').show();
        });

        $("#selFenDuan").change(function () {
            pkRuleSet.updateMaxFenDuanSet();
        });


        $("body").on("click", ".qt-fd-popup .close", function () {//打开弹窗
            $('.popup').hide();
            $('.bg').hide();
        });

        $('body').on('click','#fenDuanList .fd_cs',function () {//调整分段
            var setId = $(this).attr("setId")||"";
            $('.qt-fd-tz-popup').attr("setId", setId);
            var maxFenDuan = $("#selFenDuan").val();
            var html = "";
            for(var fenDuan = 1; fenDuan<=maxFenDuan;fenDuan++){
                html+='<option value="'+fenDuan+'">第'+fenDuan+'段</option>';
            }
            $("#selNewFenDuan").html(html);
            $('.qt-fd-tz-popup').show();
        });

        $("body").on("click", ".qt-fd-tz-popup .close, .qt-fd-tz-popup .qxx", function () {//关闭弹窗
            $('.qt-fd-tz-popup').hide();
        });

        $("body").on("click", ".qt-fd-tz-popup .qd", function () {
            pkRuleSet.updatePartClassFenDuan();
        });
    };

    pkRuleSet.updateMaxFenDuanSet = function(){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.maxFenDuan = $("#selFenDuan").val();
        Common.getPostData('/autoPk/partClassSet/updateMaxFenDuanSet.do', param, function (rep) {
            if(rep.code ==200){
                pkRuleSet.getPartClassInfo();
            }
        });
    };

    pkRuleSet.getPartClassInfo = function(){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        Common.getPostData('/autoPk/partClassSet/getPartClassInfo.do', param, function (rep) {
            $('#fenDuanList').html("");
            if(rep.code ==200){
                var msg = rep.message;
                $("#selFenDuan").val(msg.maxFenDuan);
                var list = msg.fenDuanList;
                Common.render({tmpl: $('#fenDuanList_temp'), data: list, context: '#fenDuanList', overwrite: 1});
            }
        });
    };

    pkRuleSet.updatePartClassFenDuan = function(){
        var param = {};
        param.id = $('.qt-fd-tz-popup').attr("setId");
        param.fenDuan = $("#selNewFenDuan").val();
        Common.getPostData('/autoPk/partClassSet/updatePartClassFenDuan.do', param, function (rep) {
            if(rep.code ==200){
                $('.qt-fd-tz-popup').hide();
                pkRuleSet.getPartClassInfo();
            }
        });
    };

    //排课规则-学科设置
	pkRuleSet.addSubjectSetTypeIs1Or2 = function(type,flag){
		var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.type = type;
        param.flag = flag;
		var array = new Array();
        $('.select-sub1 li').each(function (s,ot) {
        	array.push($(this).attr("subId"))
        })
        param.subIds = array;
        Common.getPostBodyData('/pkRulesSubject/addSubjectSetTypeIs1Or2.do', param, function (rep) {

        });
	}

    pkRuleSet.addSubjectSetType3 = function (type,flag){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.type = type;
        param.flag = flag;
        var array = new Array();
        $('.select-sub1 li').each(function (s,ot) {
            array.push($(this).attr("subId"))
        })
        param.subIds = array;
        param.amCount = $('#amCount').val();
        param.pmCount = $('#pmCount').val();
        Common.getPostBodyData('/pkRulesSubject/addSubjectSetType3.do', param, function (rep) {

        });
    }

    pkRuleSet.addSubjectSetTypeIs4 = function (type,flag){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.type = type;
        param.flag = flag;
        var mutexSubject = new Array();
        $('.select-sub1 li').each(function (s,ot) {
            mutexSubject.push($(this).attr("subId"))
        })
        param.mutexSubject=mutexSubject;
        param.subIds = new Array();
        Common.getPostBodyData('/pkRulesSubject/addSubjectSetType4.do', param, function (rep) {

        });
    }

    //删除互斥学科中的某一个组合
    pkRuleSet.deleteSubjectSetTypeIs4 = function (flag,id){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.type = '4';
        param.flag = flag;
        param.deleteId = id;
        var mutexSubject = new Array();
        $('.select-sub1 li').each(function (s,ot) {
            mutexSubject.push($(this).attr("subId"))
        })
        param.mutexSubject=mutexSubject;
        param.subIds = new Array();
        Common.getPostBodyData('/pkRulesSubject/addSubjectSetType4.do', param, function (rep) {

        });
    }

    //查找年级所有的学科设置的规则
    pkRuleSet.getSubjectSetDTOByCiIdAndGradeId = function (){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        Common.getData('/pkRulesSubject/getSubjectSetDTOByCiIdAndGradeId.do', param, function (rep) {
            $.each(rep.message,function (i,st) {
                if(st.type == 1){
                    if(st.flag == true){
                        $('#subjectPro').addClass('active');
                        $('#su-fp-set').show();
                    }else{
                        $('#subjectPro').removeClass('active');
                        $('#su-fp-set').hide();
                    }
                }else if(st.type == 2){
                    if(st.flag == true){
                        $('#WeekKs').addClass('active');
                        $('#we-fp-set').show();
                    }else{
                        $('#we-fp-set').hide();
                    }
                }else if(st.type == 3){
                    if(st.flag == true){
                        $('#AmPm').addClass('active');
                        $('#ap-fp-set').show();
                        $('#amCount').val(st.amcount)
                        $('#pmCount').val(st.pmcount)
                    }else{
                        $('#ap-fp-set').hide();
                    }
                }else{
                    if(st.flag == true){
                        $('#mutexSubject').addClass('active');
                        $('#mu-hc-set').show();
                        Common.render({tmpl: $('#mutexSubject_temp'), data: st.mutexSubjectDTOS, context: '#mutexSubject1', overwrite: 1});
                    }else{
                        $('#mu-hc-set').hide();
                    }
                }
            })
        });
    }

    //查找年级所有的走班设置的规则
    pkRuleSet.getZouBanSetDTOByCiIdAndGradeId = function (){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        Common.getData('/pkRulesZouBan/getZouBanSet.do', param, function (rep) {
            $.each(rep.message,function (i,st) {
                if(st.type == 1){
                    if(st.flag == true){
                        $('#ZouBanPro').addClass('active');
                    }else{
                    }
                }else if(st.type == 2){
                    if(st.flag == true){
                        $('#ZouBanCount').addClass('active');
                        $('#ZouBanCountNum').val(st.count);
                        $('.zb-sx-set').show();
                    }else{
                        $('.zb-sx-set').hide();
                    }
                }else if(st.type == 3){
                    if(st.flag == true){
                        $('#jxbStuCount').addClass('active');
                        $('#jxbStuCountNum').val(st.count);
                        $('.zb-jxb-set').show();
                    }else{
                        $('.zb-jxb-set').hide();
                    }
                }else{
                    if(st.flag == true){
                        $('#roomStuCount').addClass('active');
                        $('#roomStuCountNum').val(st.count);
                        $('.zb-fd-set').show();
                    }else{
                        $('.zb-fd-set').hide();
                    }
                }
            })
        });
    }


    //排课规则-教师设置
    pkRuleSet.getAutoTeaSet = function (){
    	var param = {};
    	param.ciId = $('#cxq').val();
    	param.gradeId = $('#grade').val();
    	Common.getPostBodyData('/autoPk/autoTeaSet/getAutoTeaSet.do', param, function (rep) {
    		var jsonData = eval('(' + rep +')');
    		var json = jsonData.message;
    		if(json.teaWeek == '1'){
    			$('.tc_zr_ck').addClass('active');
    			var other = $('.tc_zr_ck').siblings('.tc-zr-set');
    			other.show();
    		}else{
    			$('.tc_zr_ck').removeClass('active');
    			var other = $('tc_zr_ck').siblings('.tc-zr-set');
    			other.hide();
    		}
    		if(json.teaDay == '1'){
    			$('.tc_lk_ck').addClass('active');
    			var other = $('.tc_lk_ck').siblings('.tc-lk-set');
    			other.show();
    		}else{
    			$('.tc_lk_ck').removeClass('active');
    			var other = $('.tc_lk_ck').siblings('.tc-lk-set');
    			other.hide();
    		}
    		if(json.teaOverClass == '1'){
    			$('.tc_kb_kb').addClass('active');
    		}else{
    			$('.tc_kb_kb').removeClass('active');
    		}
    		if(json.teaStep == '1'){
    			$('.tc_kb_ck').addClass('active');
    			var other = $('.tc_kb_ck').siblings('.tc-kb-set');
    			other.show();
    		}else{
    			$('.tc_kb_ck').removeClass('active');
    			var other = $('.tc_kb_ck').siblings('.tc-kb-set');
    			other.hide();
    		}
    		if(json.teaRule == '1'){
    			$('.tc_gz_ck').addClass('active');
    			var other = $('.tc_gz_ck').siblings('.tc-gz-set');
    			other.show();
    		}else{
    			$('.tc_gz_ck').removeClass('active');
    			var other = $('.tc_gz_ck').siblings('.tc-gz-set');
    			other.hide();
    		}
    		if(json.teaMutex == '1'){
    			$('.tc_hc_ck').addClass('active');
    			var other = $('.tc_hc_ck').siblings('.tc-hc-set');
    			other.show();
    		}else{
    			$('.tc_hc_ck').removeClass('active');
    			var other = $('.tc_hc_ck').siblings('.tc-hc-set');
    			other.hide();
    		}
        });
    };
    
    //更新教师设置checkbox勾选状态
    pkRuleSet.updAutoSet = function (num, status) {
    	var param = {};
    	param.ciId = $("#cxq").val();
    	param.gradeId = $("#grade").val();
    	param.num = num;
    	param.status = status;
    	Common.getPostBodyData('/autoPk/autoTeaSet/addOrupdAutoTeaSet.do', param, function (rep) {
    		
        });
    };
    
    //教师周任课天数-获取当前次下的所有教师
    pkRuleSet.getAllTeachers = function (userName) {
    	$("#subTeaList").html('');
        var param = {};
        param.xqid = $("#cxq").val();
        param.gradeId = $("#grade").val();
        param.userName = userName;
        Common.getPostBodyData('/autoPk/autoTeaSet/getAllTeachers.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#subTeaTempl'), data: json.message, context: '#subTeaList', overwrite: 1});
        });
    };
    
    //教师周任课天数-获取已选中的教师
    pkRuleSet.getCTeaList = function () {
    	$("#subCTeaList").html('');
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        param.weekCourse = $('#weekCourse').val() + '';
        Common.getPostBodyData('/autoPk/autoTeaSet/getCTeaList.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#cTeaTempl'), data: json.message.userList, context: '#cTeaList', overwrite: 1});
        });
    };
    
    
    //教师日最大连课节数-获取当前次下的所有教师
    pkRuleSet.getDTeachers = function (userName) {
    	$("#dTeaList").html('');
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        param.userName = userName;
        Common.getPostBodyData('/autoPk/autoTeaSet/getDTeachers.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#dTeaTempl'), data: json.message, context: '#dTeaList', overwrite: 1});
        });
    };
    
    //教师日最大连课节数-获取已选中的教师
    pkRuleSet.getDCTeaList = function () {
    	$("#dCTeaList").html('');
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        param.courseNum = $('#courseNum').val() + '';
        Common.getPostBodyData('/autoPk/autoTeaSet/getDCTeaList.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#dCTeaTempl'), data: json.message, context: '#dCTeaList', overwrite: 1});
        });
    };
    
    
    //教师跨班进度一致-获取当前次下的所有教师
    pkRuleSet.getStepTeachers = function (userName) {
    	$("#subTeaList").empty();
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        param.userName = userName;
        Common.getPostBodyData('/autoPk/autoTeaSet/getStepTeachers.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#stepTeaTempl'), data: json.message, context: '#stepTeaList', overwrite: 1});
        });
    };
    
    //教师跨班进度一致-获取当前次下的已选教师
    pkRuleSet.getTeaStep = function () {
    	$("#stepCteaList").empty();
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        Common.getPostBodyData('/autoPk/autoTeaSet/getTeaStep.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#stepCteaTempl'), data: json.message, context: '#stepCteaList', overwrite: 1});
        });
    };
    
    //教师互斥-获取所有的互斥组
    pkRuleSet.getTeaNutex = function () {
    	$("#nutexList").empty();
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        Common.getPostBodyData('/autoPk/autoTeaSet/getTeaNutex.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
        	console.log(json)
            Common.render({tmpl: $('#nutexTempl'), data: json.message, context: '#nutexList', overwrite: 1});
        });
    };
    
    //教师互斥-获取所有的教师
    pkRuleSet.getAllTeas = function () {
    	$(".allTeaList").empty();
    	$("#myList-nav").empty();
    	num=0;
    	$('#hcTeaherNum').text(0);
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        Common.getPostBodyData('/autoPk/autoTeaSet/getAllTeas.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
            Common.render({tmpl: $('#allTeaTempl'), data: json.message, context: '.allTeaList', overwrite: 1});
          $('#myList').listnav({
                includeOther: true,
                noMatchText: '',
                prefixes: ['the', 'a']
            });
            /*$("#myList-nav").append(" <div class='demo-search'> <input type='text'id='name'> <button id='sou'>搜索</button> </div>")*/
        });
    };
    
    //获取排课次id
    pkRuleSet.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            xqid = rep.message.paikeci;
            $('#cxq').val(xqid);
        });
    };

    
    //======================================= 其他设置 ======================================================
    
    //排课规则-其他设置
    pkRuleSet.getAutoOtherSet = function (){
    	var param = {};
    	param.ciId = $('#cxq').val();
    	param.gradeId = $('#grade').val();
    	Common.getPostBodyData('/autoPk/autoTeaSet/getAutoOtherSet.do', param, function (rep) {
    		var jsonData = eval('(' + rep +')');
    		var json = jsonData.message;
    		if(json.subCheck == '1'){
    			$('.qt_xk_ck').addClass('active');
    			var other = $('.qt_xk_ck').siblings('.qt-xk-set');
    			other.show();
    		}else{
    			$('.qt_xk_ck').removeClass('active');
    			var other = $('.qt_xk_ck').siblings('.qt-xk-set');
    			other.hide();
    		}
    		if(json.boxcheck == '1'){
    			$('.qt_gz_ck').addClass('active');
    			var other = $('.qt_gz_ck').siblings('.qt-gz-set');
    			other.show();
    		}else{
    			$('.qt_gz_ck').removeClass('active');
    			var other = $('.qt_gz_ck').siblings('.qt-gz-set');
    			other.hide();
    		}
    		if(json.ltCheck == '1'){
    			$('.qt-lt-set').addClass('active');
    		}else{
    			$('.qt-lt-set').removeClass('active');
    		}
    		if(json.classCheck == '1'){
    			$('.qt_js_ck').addClass('active');
    			var other = $('.qt-lt-set').siblings('.qt-js-set');
    			other.show();
    		}else{
    			$('.qt_js_ck').removeClass('active');
    			var other = $('.qt-lt-set').siblings('.qt-js-set');
    			other.hide();
    		}
    		if(json.periodCheck == '1'){
    			$('.qt_fd_ck').addClass('active');
    			var other = $(this).siblings('.qt_fd_ck');
    			other.show();
    		}else{
    			$('.qt_fd_ck').removeClass('active');
    			var other = $(this).siblings('.qt_fd_ck');
    			other.hide();
    		}
    		
        });
    };
    
    //其他设置-更新checkbox勾选状态
    pkRuleSet.updOtherSet = function (number, status) {
    	var param = {};
    	param.ciId = $("#cxq").val();
    	param.gradeId = $("#grade").val();
    	param.number = number;
    	param.status = status;
    	Common.getPostBodyData('/autoPk/autoTeaSet/addOtherSet.do', param, function (rep) {
    		
        });
    };
    
    //其他设置- 获取学科 
    pkRuleSet.getSubjectByType = function () {
        var par = {};
        par.xqid = $("#cxq").val();
        par.gid = $("#grade").val();
        par.subType = '0';
        Common.getData('/autoPk/autoTeaSet/getSubjectByType.do', par, function (rep) {
        	Common.render({tmpl: $('#xkbxTempl'), data: rep.message, context: '#xkbxList',overwrite:1});
        });
    };

    //學科设置- 获取学科
    pkRuleSet.getSubjectSetSubject = function () {
        var par = {};
        par.xqid = $("#cxq").val();
        par.gid = $("#grade").val();
        par.subType = '0';
        Common.getData('/n33_setJXB/getSubjectByType.do', par, function (rep) {
            Common.render({tmpl: $('#subject_temp1'), data: rep.message, context: '#subject1',overwrite:1});
            Common.render({tmpl: $('#subject_temp2'), data: rep.message, context: '#subject2',overwrite:1});
            Common.render({tmpl: $('#subject_temp3'), data: rep.message, context: '#subject3',overwrite:1});
            Common.render({tmpl: $('#subject_temp4'), data: rep.message, context: '#subject4',overwrite:1});
        });
    };
    
    //其他设置- 获取已选中的学科 
    pkRuleSet.getOsubCheks = function () {
        var par = {};
        par.ciId = $("#cxq").val();
        par.gradeId = $("#grade").val();
        Common.getPostBodyData('/autoPk/autoTeaSet/getOsubCheks.do', par, function (rep) {
        	var json = eval('(' + rep + ')');
        	Common.render({tmpl: $('#xkyxTempl'), data: json.message, context: '#xkyxList',overwrite:1});
        });
    };
    
    //其他设置- 获取不排课的格子 
    pkRuleSet.getGzs = function () {
    	$('.gz').removeClass('active');
        var param = {};
        param.ciId = $("#cxq").val();
        param.gradeId = $("#grade").val();
        Common.getPostBodyData('/autoPk/autoTeaSet/getGzs.do', param, function (rep) {
        	var json = eval('(' + rep + ')');
        	var dataList = json.message;
        	$.each(dataList,function (i,item) {
        		console.log(item.x,item.y);
        		$('.gz_tb tbody').find('tr').eq(parseInt(item.y)-1).find('td').eq(item.x).addClass('active');
        	});
        });
    };

    //走班设置学科轮次一致
    pkRuleSet.addZouBanSetTypeIs1 = function (type,flag){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.type = type;
        param.flag = flag;
        Common.getData('/pkRulesZouBan/addZouBanSet.do', param, function (rep) {

        });
    }

    //走班设置走班教室数量或者教学班人数上限或者走班教室上下浮动人数上限
    pkRuleSet.addZouBanSetTypeIs2Or3Or4 = function (type,flag,count){
        var param = {};
        param.ciId = $('#cxq').val();
        param.gradeId = $('#grade').val();
        param.type = type;
        param.flag = flag;
        param.count= count;
        Common.getData('/pkRulesZouBan/addZouBanSet.do', param, function (rep) {

        });
    }



    
    module.exports = pkRuleSet;
});