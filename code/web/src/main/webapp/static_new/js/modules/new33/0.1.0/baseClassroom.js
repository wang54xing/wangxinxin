/**
 * Created by albin on 2017/7/25.
 */

define('baseClassroom', ['jquery', 'doT', 'common', 'pagination', 'layer'], function (require, exports, module) {
    var baseClassroom = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    Common = require('common');
    /**全局变量**/
    var classroomMap = new Map();
    var dtos = new Array();
    var gradeId = "";
    var ciId = "";
    baseClassroom.init=function(){
    	baseClassroom.syncTerm();
        baseClassroom.getTermList();
        baseClassroom.getDefaultCi();
    	baseClassroom.syncOther($("#term").val());
    	baseClassroom.getgrade();
        initGrade();
    	baseClassroom.getList();
    	baseClassroom.initGradeWeekRangeEntry();
        var flag = getCiIdIsFaBu();
    	
		$('.wind .d1 em,.wind .dbtn .btn-no').click(function(){
			$('.wind,.bg').fadeOut();
		})
		$('.cou-x em').click(function(){
			$('.reset').show();
		});
        // $('body').on('click','.ul-jxl li',function(){
        //     $(this).addClass('active').siblings('li').removeClass('active');
        //     $(this).find('span').addClass('r-icon').parent().siblings().find('span').removeClass('r-icon')
        // })
		$('.d-sty em').click(function(){
			$(this).addClass('cur').siblings().removeClass('cur');
		});
		$("#term").change(function(){
            baseClassroom.getgrade();
			baseClassroom.getList();
			baseClassroom.syncOther($(this).val());
            baseClassroom.initGradeWeekRangeEntry();

		});
		$("#grade").change(function(){
			baseClassroom.getList();
		});
		// 选择教室
		$("#select_classroom").click(function(){

            if(flag){
                layer.alert("课表已发布,不允许修改教室");
                return;
            }
			baseClassroom.getSchoolLoop();
			 baseClassroom.getRoomListBySchoolId();
			$('#select_classroom_dialog,.bg').fadeIn();
		});
		// 点击教学楼
    	$("body").on("click","#loop .loop",function(){
            $(this).parent().addClass('active').siblings('li').removeClass('active');
            $(this).parent().find('span').addClass('r-icon').parent().siblings().find('span').removeClass('r-icon')
    		$("#roomlist li[name='"+$(this).attr("eid")+"']").show();
    		$("#roomlist li[name!='"+$(this).attr("eid")+"']").hide();
    		if($("#roomlist li[name='"+$(this).attr("eid")+"']").length==0){
				$("#tip").show();
			}
			else{
                $("#tip").hide();
			}
    	});
    	$("body").on("click","#classlist .del",function(){
    		if(flag){
    			layer.alert("课表已发布,不允许删除");
    			return;
			}
			var eid = $(this).attr("eid");
            layer.confirm('是否确定要删除', {
                btn: ['确定', '取消'] //按钮
            }, function () {

                baseClassroom.remove(eid);
            },function () {
            });

    	});
    	// 设置班级
    	$("body").on("click","#classlist .lset",function(){
            if(flag){
                layer.alert("课表已发布,不允许设置");
                return;
            }
    		$("#class_select_save").attr("eid",$(this).attr("eid"));
    		baseClassroom.getClassList();
    		$('#class_select_dialog,.bg').fadeIn();
    		$("#class_select option[value=" + $(this).attr("cid") + "]").attr("selected","selected");
    	});
    	// 确定班级
    	$("#class_select_save").click(function(){
    		baseClassroom.updateClassNameId($("#class_select_save").attr("eid"));
    		$('#class_select_dialog,.bg').fadeOut();
    		$("#class_select_save").attr("eid","");
    	});
    	// 是否可以安排
    	$("body").on("click","#classlist .arranged",function(){
    		if($(this).is(":checked")){
    			baseClassroom.arrange($(this).attr("eid"),1);
    		}
    		else{
    			baseClassroom.arrange($(this).attr("eid"),0);
    		}
    	});
    	// 点击编辑
    	$("body").on("click","#classlist .edit",function(){
    		if(flag){
    			layer.alert("课表已发布,不允许修改");
    			return;
			}
    		$("#edit_save").attr("eid",$(this).attr("eid"));
    		$("#description").val($(this).attr("desc"));
            $("#capacity").val($(this).attr("cap"));
            $("#room_type").val($(this).attr("type"))
    		$('#edit_dialog,.bg').fadeIn();
    	});
    	// 编辑保存
    	$("#edit_save").click(function(){
    		baseClassroom.updateDesc($(this).attr("eid"),$("#description").val(),$("#capacity").val(),$("#room_type").val());
    		$('#edit_dialog,.bg').fadeOut();
    		
    	});
    	// 教室选择弹窗确定
    	$("#update_classroom").click(function(){
    		baseClassroom.updateClassRoomList();
    		$('#select_classroom_dialog,.bg').fadeOut();
    	});
    	// 教室复选框
    	$("body").on("click","#roomlist .check_room",function(){
    		if($(this).is(":checked")){
    			var dto = {};
    			dto.xqid=$("#term").val();
    			dto.gradeId = $("#grade").val();
    			dto.roomId = $(this).val();
    			dto.roomName = $(this).attr("ename");
    			dtos.push(dto);
    		}
    		else{
    			for(var i in dtos){
    				if(dtos[i].roomId == $(this).val()){
    					dtos.splice(i,1);
    					break;
    				}
    			}
    		}
    	});

	}

    baseClassroom.initGradeWeekRangeEntry = function(){
        var xqid = $("#term").val();
        Common.getData('/n33_gradeweekrange/initGradeWeekRangeEntry.do', {"xqid":xqid}, function (rep) {

        })
    }
    baseClassroom.getgrade = function(){
    	var xqid = $("#term").val();
    	if(xqid != undefined && xqid != null && xqid != ""){
            Common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
                Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
            })
		}
    }
    baseClassroom.getTermList = function(){
    	Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
  		  if(rep.code==200){
  			Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
  		  }
  	  });
    }
    baseClassroom.getList = function () {
    	var xueqi = $("#term").val();
    	var gradeId = $("#grade").val();
    	  Common.getData('/classset/getListByXqGrade.do', {"xqid":xueqi,"gradeId":gradeId}, function (rep) {
              if(rep.length==0){
                  $("#none_png").show();
                  $("#content").hide();
              }
              else{
                  $("#none_png").hide();
                  $("#content").show();
              }
    		  Common.render({
  				tmpl : "#classlist_temp",
  				context : "#classlist",
  				data : rep,
  				overwrite : 1
  			});
    		 dtos = rep;
    		 var flag = getCiIdIsFaBu();
              if(flag){
                  $(".arranged").attr("disabled",true)
              }
    	  });
    }

    baseClassroom.getSchoolLoop = function() {
    	Common.getData('/classset/getSchoolLoopList.do?page=1&pageSize=100', {}, function(resp) {
    		if (resp && resp.code == '200') {
    			Common.render({
    				tmpl : "#loopTmpl",
    				context : "#loop",
    				data : resp.message,
    				overwrite : 1
    			});
    			
    		}  
    	})
    }
    
    // 同步学期
    baseClassroom.syncTerm = function(){
    	Common.getData('/new33isolateMange/getIsolate.do', {}, function(resp) {
    		
    	});
    }
    baseClassroom.syncOther = function(xqid){
    	Common.getData('/new33isolateMange/getIsolateList.do', {xqid:xqid}, function(resp) {
    		
    	});
    }
    // 删除一个
    baseClassroom.remove = function(id){
    	Common.getData('/classset/removeClassRoom.do', {"id":id}, function(resp) {
    		baseClassroom.getList();
            layer.msg("删除成功")
        });
    }
    baseClassroom.getClassList = function(){
    	var gradeId = $("#grade").val();
        var xqid = $("#term").val();
    	Common.getData('/new33classManage/getClassList.do', {"gradeId":gradeId,"xqid":xqid}, function(resp) {
    		var list = new Array();
    		var map = {};
    		map.classId = "";
    		map.gname = "";
    		list.push(map);
    		$.each(resp.message,function (i,st) {
    			list.push(st);
            })
    		Common.render({tmpl: $('#class_select_templ'), data: list, context: '#class_select',overwrite:1});
    	});
    }
    baseClassroom.getRoomListBySchoolId = function(){
    	Common.getData('/classset/getRoomListBySchoolId.do', {}, function(resp) {
    		classroomMap = resp;
    		var arr = [];
    		$.each(resp,function(key,value){
    			arr.push.apply(arr,value)
    		})
    		Common.render({tmpl: $('#roomlist_templ'), data: arr, context: '#roomlist',overwrite:1});
    		if(dtos.length!=0){
    			for(var i in dtos){
    				$("#roomlist input[value='"+dtos[i].roomId+"']").prop("checked",true);
    			}
    		}
    	});
    }
    //保存教室选择
    baseClassroom.updateClassRoomList = function(){
    	Common.getPostBodyData('/classset/updateClassRoomList.do?xqid='+$("#term").val()+"&gradeId="+$("#grade").val(), dtos, function(resp) {
    		baseClassroom.getList();
    	});
    }
    baseClassroom.updateClassNameId = function(id){
    	Common.getData('/classset/updateClassNameId.do', {"id":id,"className":$("#class_select").find("option:selected").text(),"classId":$("#class_select").val()}, function(resp) {
    		baseClassroom.getList();
    	});
    }
    baseClassroom.arrange = function(id,arranged){
    	Common.getData('/classset/updateArrange.do', {"id":id,"arranged":arranged}, function(resp) {
    		
    	});
    }
    baseClassroom.updateDesc = function(id,description,cap,type){
    	Common.getData('/classset/updateDesc.do', {"id":id,"description":description,"cap":cap,"type":type}, function(resp) {
    		baseClassroom.getList();
    	});
    }

    baseClassroom.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
        	ciId = rep.message.paikeci;
            $("#defaultTerm").text(rep.message.paikeciname);
           	$("#term").val(rep.message.paikeci);
        });
    }
    function initGrade(){
        try {
            Common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "type") {
                            type = obj.value;
                        }


                    })
                        if(gradeId!=""){
                            $("#grade").val(gradeId).change();
                        }
                } else {

                }
            });
        } catch (x) {

        }
    }
    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":ciId}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    module.exports = baseClassroom;
})