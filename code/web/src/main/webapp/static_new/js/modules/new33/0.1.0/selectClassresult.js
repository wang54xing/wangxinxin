/**
 * Created by albin on 2017/7/25.
 */

define('selectClassresult', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var selectClassresult = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');
	var gradeId ="";
	var classId = "";
   
    selectClassresult.init=function(){
    	selectClassresult.getTermList();
        selectClassresult.getDefaultCi();
    	selectClassresult.getgrade($("#term .cur").attr("val"));
    	selectClassresult.getClass();
    	selectClassresult.getStuSelectByClass();
		initGrade();

    	$("body").on("click","#term em",function(){
    		$("#stuName").val("");
    		$(this).addClass('cur').siblings().removeClass('cur');
    		selectClassresult.getgrade($(this).attr("val"));
    		selectClassresult.getClass();
    		selectClassresult.getStuSelectByClass();
    	});
    	$("body").on("click","#grade em",function(){
    		$("#stuName").val("");
    		$(this).addClass('cur').siblings().removeClass('cur');
    		selectClassresult.getClass();
    		selectClassresult.getStuSelectByClass();
    	});
    	$("body").on("click","#klass em",function(){
    		$("#stuName").val("");
    		$(this).addClass('cur').siblings().removeClass('cur');
    		selectClassresult.getStuSelectByClass();
    	});
    	$("body").on("click","#sosu",function(){
    		selectClassresult.getStuSelectByClass();
    	});
    	
	}
    selectClassresult.getTermList = function(){
    	common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
    		  if(rep.code==200){
    			common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
    			// $("#term em:eq(0)").addClass("cur");
    		  }
    	  });
    }
    selectClassresult.getgrade = function(xqid){
    	common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
    		common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
    		// $("#grade em:eq(0)").addClass("cur");
    	})
    }
    selectClassresult.getClass = function(){
    	var xqid = $("#term em[class='cur']").attr("val");
    	var gradeId = $("#grade em[class='cur']").attr("val");
    	
    	if(!xqid||!gradeId||xqid==""||gradeId==""){
    		return;
    	}
    	common.getData('/new33classManage/getClassList.do', {"xqid":xqid,"gradeId":gradeId}, function (rep) {
    		common.render({tmpl: $('#klass_temp'), data: rep.message, context: '#klass',overwrite:1});
    		// $("#klass em:eq(0)").addClass("cur");
    	})
    	
    	var hgt = $('#klass').height();
    	$("#Klass").parent().css('height',hgt);
    	$("#Klass").css('height',hgt);
    	$("#Klass").css('line-height',hgt+'px');
    }
    selectClassresult.getStuSelectByClass = function(){
    	var termId = $("#term em[class='cur']").attr("val");
    	var gid = $("#grade em[class='cur']").attr("val");
    	var classId = $("#klass em[class='cur']").attr("val");
    	if($("#stuName").val()!=""){
    		var name = $("#stuName").val();
    	}
    	if(!termId||!gid||termId==""||gid==""||!classId||classId==""){
    		common.render({tmpl: $('#student_temp'), data: [], context: '#student',overwrite:1});
    		return;
    	}
    	common.getData('/new33school/set/getStuSelectByClass.do', {"xqid":termId,"gradeId":gid,"classId":classId,"name":name}, function (rep) {
    		common.render({tmpl: $('#student_temp'), data: rep, context: '#student',overwrite:1});
    	})
    }
    selectClassresult.getDefaultCi = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term em").each(function () {
                if(rep.message.paikeci==$(this).attr("val")){
                    $(this).addClass("cur");
                }
            });
        });
    }

    function initGrade(){
        try {
            common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "type") {
                            type = obj.value;
                        }
                        if (obj.key == "classId") {
                            classId = obj.value;
                        }

                    })

                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("val")){
                            $(this).click();
                        }
                    });
                    $("#klass em").each(function () {
                        if(classId==$(this).attr("val")){
                            $(this).click();
                        }
                    });
                } else {

                }
            });
        } catch (x) {

        }
    }
    module.exports = selectClassresult;
})