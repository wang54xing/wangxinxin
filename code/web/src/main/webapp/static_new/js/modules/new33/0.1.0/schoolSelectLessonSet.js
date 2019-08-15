/**
 * Created by albin on 2017/7/25.
 */

define('schoolSelectLessonSet', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var schoolSelectLessonSet = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    common = require('common');
	var ciId = "";
    //加载数据
    schoolSelectLessonSet.getList = function () {
    	var termId = $("#term em[class='cur']").attr("val");
    	var gradeId = $("#grade em[class='cur']").attr("val");
    	if(!termId||!gradeId||termId==""||gradeId==""){
    		common.render({tmpl: $('#subject'), data: [], context: '#tr_subject',overwrite:1}); 
    		common.render({tmpl: $('#tdTmpl122'), data: [], context: '#my_tbody',overwrite:1}); 
    		return;
    	}
        common.getData('/new33/subjects.do', {termId:termId,gradeId:gradeId}, function (rep) {
        	try
        	{
        		$("#subject_length").val(rep.message.length);
        		common.render({tmpl: $('#subject'), data: rep.message, context: '#tr_subject',overwrite:1}); 
        		
        		for(var i=0;i<rep.message.length;i++)
        		{
        			var obj=rep.message[i];
        			jQuery("#s"+(i+1)).val(obj.idStr);
        		}	
        	}catch(x)
        	{
        	}
        })
        common.getData('/new33school/set/list.do', {termId:termId,gradeId:gradeId}, function (rep) {
        	try
        	{
        		if(rep.message.list.length==0){
					$("#none_png").show();
                    $("#content").hide();
				}
				else{
                    $("#none_png").hide();
                    $("#content").show();
				}
        		jQuery("#selectId").val(rep.message.id);
                jQuery("#start").val(rep.message.start);
                jQuery("#end").val(rep.message.end);
        		common.render({tmpl: $('#tdTmpl122'), data: rep.message.list, context: '#my_tbody',overwrite:1}); 
        		jQuery("#my_tbody").find("input[type='checkbox']").each(function(){
        			jQuery(this).change(function(){
        				 var ischecked = $(this).prop("checked");
        				 var iInt=ischecked?1:0;
        				 var parm={"id":jQuery("#selectId").val(),"selectId":$(this).prop("id"),"state":iInt};
        				 common.getData('/new33school/set/update.do', parm, function (rep){});
        			})
        		});
                var flag = getCiIdIsFaBu();
                if(flag){
                    jQuery("#my_tbody").find("input[type='checkbox']").attr("disabled",true);
                    jQuery(".ck-all").attr("disabled",true);
                }
        	}catch(x)
        	{
        	}
        })
    }
    schoolSelectLessonSet.getTermList = function(){
    	common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
    		  if(rep.code==200){
    			common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
    			// $("#term em:eq(0)").addClass("cur");
    		  }
    	  });
    }
    schoolSelectLessonSet.getgrade = function(xqid){
    	common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
    		common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
    		// $("#grade em:eq(0)").addClass("cur");
    	})
    }
    
    schoolSelectLessonSet.updateStart = function(){
    	var start = $("#start").val();
    	var end = $("#end").val();
    	if(!start||!end||start==""||end==""){
    		layer.alert("时间不能为空");
    		return;
    	}
    	if(start>end){
    		layer.alert("开始时间不能晚于结束时间");
    		return;
    	}
    	if(!$("#selectId").val()||$("#selectId").val()==""){
    		layer.alert("必须选择一个学期年级");
    		return;
    	}
    	common.getData('/new33school/set/updateStartTime.do', {"id":$("#selectId").val(),"start":start,"end":end}, function (rep) {
    		if(rep.code==200){
    			layer.msg("保存成功");
                $('.stu-popup').hide();
                $('.bg').hide();
                // schoolSelectLessonSet.getTermList();
            	// schoolSelectLessonSet.getgrade($("#term .cur").attr("val"));
            	// schoolSelectLessonSet.getList();
    		}
    	})
    }
    schoolSelectLessonSet.initRome = function(){
    	rome(start);
        rome(end);
    }
    schoolSelectLessonSet.init=function(){
    	schoolSelectLessonSet.getTermList();
        schoolSelectLessonSet.getDefaultCi();
    	schoolSelectLessonSet.getgrade($("#term .cur").attr("val"));
    	schoolSelectLessonSet.getList();
    	schoolSelectLessonSet.initRome();
        initGrade();

        //全选
        $("body").on('click','.ck-all',function () {
            var ck=$(this).prop('checked');
            var ck2='';
            if(ck)ck2='checked';
            $.each($('#my_tbody input[type="checkbox"]'),function (i,item) {
                $(item).prop('checked',ck2)
            })

            jQuery("#my_tbody").find("input[type='checkbox']").each(function(){
            	var ischecked = $(this).prop("checked");
            	var iInt=ischecked?1:0;
            	var parm={"id":jQuery("#selectId").val(),"selectId":$(this).prop("id"),"state":iInt};
            	common.getData('/new33school/set/update.do', parm, function (rep){});
            });
        })

    	$("body").on("click","#term em",function(){
            $("#selectId").val("");
    		$(this).addClass('cur').siblings().removeClass('cur')
    		schoolSelectLessonSet.getgrade($(this).attr("val"));
    		schoolSelectLessonSet.getList();

    	});
    	$("body").on("click","#grade em",function(){
    		$(this).addClass('cur').siblings().removeClass('cur');
    		schoolSelectLessonSet.getList();
    	});
    	$('.add-bt').click(function(){
			$('.bg').show();
			$('.stu-popup').show();
		})
		$('.popup-top i,.qx').click(function(){
			$('.bg').hide();
			$('.stu-popup').hide();
		})
    	$("#update_start").click(function(){
            var flag = getCiIdIsFaBu();
            if(flag){
				layer.alert("课表已发布,不允许修改");
				return;
			}
    		schoolSelectLessonSet.updateStart();
    	});
    	$("body").on("change","#my_tbody input[type='text']",function () {
            updateNum($(this).attr("id"),$(this).val());
        });
	}
    schoolSelectLessonSet.getDefaultCi = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            ciId = rep.message.paikeci;
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


                    })

                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("val")){
                            $(this).click();
                        }
                    });
                } else {

                }
            });
        } catch (x) {

        }
    }
    
    function getCiIdIsFaBu() {
    	var flag = null;
        common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":ciId}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    function updateNum(id,num){
    	var num1 = "";
    	if(num != "" || num < 0){
			num1 = num;
		}else{
    		num1 = -1;
		}
        var parm={"id":jQuery("#selectId").val(),"selectId":id,"num":num1};
        common.getData('/new33school/set/updateNum.do', parm, function (rep){
        	if(rep.code == 200){
				layer.msg("设置成功");
			}
		});
	}
    module.exports = schoolSelectLessonSet;
})