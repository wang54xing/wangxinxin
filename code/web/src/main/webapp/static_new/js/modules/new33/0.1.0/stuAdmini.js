/**
 * Created by albin on 2018/3/7.
 */
define('stuAdmini', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var stuAdmini = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var studentList = new Array();
    var gradeId = "";
    var classId = "";

    stuAdmini.getTbodyList=function(){
    	$.each(studentList, function (i, obj) {
            $("#stu2List tr[userId=" + obj.userId + "]").attr("ids", obj.id);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("xqid", obj.xqid);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("userId", obj.userId);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("userName", obj.userName);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("classId", obj.classId);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("className", obj.className);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("gradeId", obj.gradeId);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("gradeName", obj.gradeName);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("studyNumber", obj.studyNumber);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("level", obj.level);
            $("#stu2List tr[userId=" + obj.userId + "]").attr("sex", obj.sex);
        })
    }
    
    stuAdmini.getStudentByName = function(){
    	var par = {};
    	par.name = $("#userName").val();
        par.xqid = $("#term .cur").attr("ids");
        par.cid = $("#cla .cur").attr("ids");
        if(par.xqid != "" && par.xqid != undefined && par.xqid != null && par.cid != "" && par.cid != undefined && par.cid != null){
        	Common.getData('/IsolateStudent/getStudentByName.do', par, function (rep) {
                if(rep.message.length==0){
                    $("#none_png").show();
                    $("#content").hide();
                }
                else{
                    $("#none_png").hide();
                    $("#content").show();
                }
           	 	Common.render({tmpl: $('#studentList'), data: rep.message, context: '#stuList', overwrite: 1});
        	});
        }else{
        	Common.render({tmpl: $('#studentList'), data: [], context: '#stuList', overwrite: 1});
        }
    };
    
    stuAdmini.init = function () {
    	
    	$("body").on("click","#sosu",function(){
    		stuAdmini.getStudentByName();
    		//stuAdmini.getSchoolSelects();
    	});
    	
    	$("body").on("click", ".ss", function(){
    		$('.bg').show();
			$('.addn-popup').show();
    		$(".ad i").removeClass("ad-cur");
    		$(".ad i").attr("ids","*");
    		studentList = new Array();
    		stuAdmini.getStudentListByNameOrCn();
    		stuAdmini.getTbodyList();
    	})
    	
    	$("body").on("click", ".ad i", function(){
    		$(this).toggleClass('ad-cur');
    	})
    	
    	/*$("body").on("click", ".gge span", function(){
    		if($(this).text().indexOf('更改')!=-1){
				$(this).parent().find('option').removeAttr('disabled','disabled');
				$(this).text('确定');
				$(this).addClass("ggl");
			}else{
				$(this).parent().find('option').attr('disabled','disabled')
				$(this).text('更改');
			}
    	})*/
    	
    	$("body").on("change", ".levelSel", function(){
    		stuAdmini.updateLevel($(this));
    		stuAdmini.getStudentByName();
    		//stuAdmini.getSchoolSelects();
    	})
    	
    	$("body").on("click", "#grade em", function () {
    		// $("#userName").val("");
            $("#grade em").removeClass("cur");
            $(this).addClass("cur");
            stuAdmini.getIsolateStudent();
            stuAdmini.getClassList();
            stuAdmini.getStudentByName();
            //stuAdmini.getSchoolSelects();
        })
        
        $("body").on("click", "#cla em", function () {
        	// $("#userName").val("");
            $("#cla em").removeClass("cur");
            $(this).addClass("cur");
            stuAdmini.getIsolateStudent();
            stuAdmini.getStudentByName();
            //stuAdmini.getSchoolSelects();
        })
        
        $("body").on("click", "#term em", function () {
        	$("#userName").val("");
            $("#term em").removeClass("cur")
            $(this).addClass("cur")
            stuAdmini.getGradeList();
            stuAdmini.getClassList();
            stuAdmini.getIsolateStudent();
            stuAdmini.getStudentByName();
            //stuAdmini.getSchoolSelects();
        })
        
        $("body").on("click", ".tj", function () {
        	stuAdmini.addStudentList();
        	//stuAdmini.getSchoolSelects();
        })
        
         $("body").on("click", ".qd", function () {
        	$('.gg-popup,.bg').hide();
         	$('.gg-popup,.bg').hide();
         	if($('#uJXB').is(":checked")){
                stuAdmini.updateStudentAndJXB();
                stuAdmini.getStudentByName();
            }else{
                stuAdmini.updateStudent();
                stuAdmini.getStudentByName();
            }
        })
       /* $("body").on("click", ".sc", function () {
            $(".bg").show();
            $(".imstu-popup1").show();
        })*/

         $("body").on("click", ".tongbu", function () {
             if(flag){
                 layer.alert("课表已发布，不允许同步班级和学生");
                 return;
             }
             var par = {};
             par.xqid = $("#term .cur").attr("ids");
             par.gradeId =  $("#grade .cur").attr("ids");
             if(par.xqid != "" && par.xqid != undefined && par.xqid != null){
                 layer.confirm('重新同步数据会删除原始数据，是否同步？', {
                     btn: ['确定', '取消'] //按钮
                 }, function () {
                     $(".layui-layer").hide();
                     $(".jhh").show();
                     $('.bg').show();
                     $(".jhhSp").text("正在同步学生和班级，请稍候...");
                     setTimeout(function () {
                         Common.getData('/IsolateStudent/synchronizedStudent.do', par, function (rep) {
                             layer.msg("同步成功");
                             stuAdmini.getClassList();
                             stuAdmini.getStudentByName();
                             $(".bg").hide();
                             $(".jhh").hide();
                         });
                     },500);
                 },function () {
                 });
             }else{
                 layer.msg("同步学生时学期为空");
                 $(".bg").hide();
                 $(".jhh").hide();
             }

         })

        $("body").on("click", "#qddr", function () {
            var par = {};
            par.xqid =  $("#term .cur").attr("ids");
            if(!par.xqid || par.xqid == "" || par.xqid == null){
            	layer.msg("请选择一个学期");
            	return;
            }
            if ($("#file").val() != "") {
                var value = $("#file").val();
                var result = /\.[^\.]+/.exec(value);
                if (result == ".xls" || result == ".xlsx") {
                	$(".jhh").show();
                	$('.bg').show();
                	$(".imstu-popup1").hide();
                    $(".jhhSp").text("正在导入学生，请稍候...");
                    $.ajaxFileUpload({
                        url: '/IsolateStudent/addUserImpYu.do',
                        param: par,
                        secureuri: false,
                        fileElementId: 'file',
                        dataType: 'json',
                        async: false,
                        success: function (data) {
                        	$(".jhh").hide();
                       	 	$('.bg').hide();
                            stuAdmini.getStudentByName();
                            //stuAdmini.getSchoolSelects();
                            if(data.message.length > 0){
                                Common.render({tmpl: $('#errorList'), data: data.message, context: '#stuErrorList', overwrite: 1});
                                $(".bg").show();
                                $(".drsb-popup1").show();
                            }else{
                                layer.msg("导入成功");
                            }
                            var file = $("#file")
                            file.after(file.clone().val(""));
                            file.remove();
                            $(".file-name").html("");
                        },
                        error: function (e) {
                            var file = $("#file")
                            file.after(file.clone().val(""));
                            file.remove();
                            $(".file-name").html("");
                        	$(".jhh").hide();
                       	 	$('.bg').hide();
                            layer.msg("网络错误，请重试！！");
                        }
                    })
                   
                } else {
                    var file = $("#file")
                    file.after(file.clone().val(""));
                    file.remove();
                    $(".file-name").html("");
                    layer.msg("请使用excel格式导入");
                }
            } else {
                var file = $("#file")
                file.after(file.clone().val(""));
                file.remove();
                $(".file-name").html("");
                layer.msg("请选择文件");
            }
        })

        $("body").on("change","#file",function(){
            var _this = $(this);
            var nameArr = _this.val().split("\\");
            var filename = nameArr[nameArr.length - 1];
            $(".file-name").html(filename);
        });
        
        $("body").on("click", ".gg", function () {
        	var ids = $(this).attr("ids");
			stuAdmini.getStudentDto(ids);
			stuAdmini.getAllCombineName();
            if(flag){
                if($(".inp1").val() != "" && $(".inp1").val() != undefined && $(".inp1").val() != null){
                    layer.alert("课表已发布，不允许修改选科组合");
                    return;
                }
            }
            $('.bg').show();
            $('.gg-popup').show();
			
        })
        $("body").on("click", ".download", function () {
        	window.location.href="/IsolateStudent/exportTemplate.do";
        })
        
        $("body").on("click", ".opts", function () {
        	  var par = {};
        	  par.id = $(this).attr("ids");
                if(flag){
                    layer.alert("课表已发布，不允许删除学生");
                    return;
                }
              layer.confirm('确认删除？', {
                  btn: ['确定', '取消'] //按钮
              }, function () {
            	  Common.getData('/IsolateStudent/deleteStudent.do', par, function (rep) {
              		layer.msg(rep.message);
                      stuAdmini.getStudentByName();
                    //stuAdmini.getSchoolSelects();
                  });
              },function () {
              }); 
         });

        stuAdmini.getTermList();
        stuAdmini.getDefaultCi();
        var flag = getCiIdIsFaBu();
        stuAdmini.getGradeList();
        stuAdmini.getClassList();
        stuAdmini.getIsolateStudent();
        stuAdmini.getStudentByName();
        initGrade();

        //stuAdmini.getSchoolSelects();
        
    }

    stuAdmini.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term em").each(function () {
                if(rep.message.paikeci==$(this).attr("ids")){
                    $(this).addClass("cur");
                }
            });
        });
    }
    
    stuAdmini.getIsolateStudent = function () {
    	var par = {};
        par.xqid = $("#term .cur").attr("ids");
        if(par.xqid != "" && par.xqid != undefined && par.xqid != null){
        	Common.getData('/IsolateStudent/getIsolateStudent.do', par, function (rep) {
                
            });
        }else{
        	layer.msg("同步学生时学期为空");
        }
        
    }
    
    stuAdmini.getAllCombineName = function () {
    	var par = {};
    	par.termId = $("#term .cur").attr("ids");
        par.gradeId = $("#grade .cur").attr("ids");
        if(par.termId != "" && par.termId != undefined && par.termId != null  && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null){
        	 Common.getData('/IsolateStudent/getSelectLessonsList.do', par, function (rep) {
             	Common.render({tmpl: $('#sel_temp'), data: rep.message, context: '#sel', overwrite: 1});
             	$.each(rep.message, function (i, obj) {
                     $("#sel option:eq("+i+")").attr("subjectId1", obj.subjectId1Str);
                     $("#sel option:eq("+i+")").attr("subjectId2", obj.subjectId2Str);
                     $("#sel option:eq("+i+")").attr("subjectId3", obj.subjectId3Str);
                     $("#sel option:eq("+i+")").attr("name", obj.name);
                 })
             });
        }else{
        	Common.render({tmpl: $('#sel_temp'), data:[], context: '#sel', overwrite: 1});
        }
    }
    
    stuAdmini.updateStudent = function () {
    	var par = {};
    	par.id = $(".qd").attr("ids");
    	var value=$("#sel").val();
    	par.subjectId1 = $("#sel option[value="+value+"]").attr("subjectId1");
    	par.subjectId2 = $("#sel option[value="+value+"]").attr("subjectId2");
    	par.subjectId3 = $("#sel option[value="+value+"]").attr("subjectId3");
    	par.name = $("#sel option[value="+value+"]").attr("name");
        Common.getData('/IsolateStudent/updateStudent.do', par, function (rep) {
        	
        });
        // stuAdmini.getStudentList();
    }

    stuAdmini.updateStudentAndJXB = function () {
        var par = {};
        par.id = $(".qd").attr("ids");
        var value=$("#sel").val();
        par.subjectId1 = $("#sel option[value="+value+"]").attr("subjectId1");
        par.subjectId2 = $("#sel option[value="+value+"]").attr("subjectId2");
        par.subjectId3 = $("#sel option[value="+value+"]").attr("subjectId3");
        par.name = $("#sel option[value="+value+"]").attr("name");
        Common.getData('/IsolateStudent/updateStudentAndJXB.do', par, function (rep) {
            if(rep.message.nJxbName == ""){
                $('#stuName').text(rep.message.stuName);
                $('#combineName').text(rep.message.combine);
                if(rep.message.oJxbName == ""){
                    $('#oJxbName1').text("未找到原教学班");
                }else{
                    $('#oJxbName1').text(rep.message.oJxbName);
                }
                $('.gs-popup').show();
            }else{
                $('#stuName1').text(rep.message.stuName);
                if(rep.message.oJxbName == ""){
                    $('#oJxbName').text("未找到原教学班");
                }else{
                    $('#oJxbName').text(rep.message.oJxbName);
                }
                $('#nJxbName').text(rep.message.nJxbName);
                $('.gp-popup').show();
            }
        });
        // stuAdmini.getStudentList();
    }


    /*stuAdmini.getSchoolSelects = function () {
    	var par = {};
    	par.termId = $("#term .cur").attr("ids");
        par.gradeId = $("#grade .cur").attr("ids");
        if(par.termId != "" && par.termId != undefined && par.termId != null && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null){
        	Common.getData('/IsolateStudent/getSelectLessonsList.do', par, function (rep) {
            	if(rep.message.length > 0){
            		$(".gg").removeClass("imghide").addClass("imghides");
            	}
            });
        }
    }*/
    
    stuAdmini.getStudentDto = function (ids) {
    	var par = {};
        par.id = ids;
        Common.getData('/IsolateStudent/getStudent.do', par, function (rep) {
        	$(".inp1").val(rep.message.combiname);
        	$(".qd").attr("ids",rep.message.id);
        });
    }
  
    stuAdmini.updateLevel = function (select) {
    	var par = {};
        par.level = $(select).val();
        par.id = $(select).attr("ids");
        Common.getData('/IsolateStudent/updateLevel.do', par, function (rep) {
        	
        });
        stuAdmini.getStudentByName();
    }
    
    stuAdmini.addStudentList = function () {
    	var dtos = new Array();
    	$("#stu2List .ad-cur").each(function () {
            var dto = {};
            var tr=	$(this).parent().parent();
            dto.xqid = $("#term .cur").attr("ids");
            dto.userId = $(tr).attr("userId");
            dto.userName = $(tr).attr("userName");
            dto.classId = $(tr).attr("classId");
            dto.className = $(tr).attr("className");
            dto.gradeId = $(tr).attr("gradeId");
            dto.gradeName = $(tr).attr("gradeName");
            dto.studyNumber = $(tr).attr("studyNumber");
            dto.level = $(tr).attr("level");
            dto.sex = $(tr).attr("sex");
            dto.id = $(tr).attr("ids");
            dtos.push(dto);
        });
    	if(dtos.length > 0){
    		Common.getPostBodyData('/IsolateStudent/addStudentList.do', dtos, function (rep) {
    	        $('.addn-popup,.bg').hide();
    	        $('.add-popup,.bg').hide();
                stuAdmini.getStudentByName();
    	        $('.inp3').val("");
    	    });
    	}else{
    		layer.msg("请选择学生");
    		$('.bg').show();
			$('.addn-popup').show();
    		$(".ad i").removeClass("ad-cur");
    		$(".ad i").attr("ids","*");
    		studentList = new Array();
    		stuAdmini.getStudentListByNameOrCn();
    		stuAdmini.getTbodyList();
    	}
    }
    
    stuAdmini.getStudentListByNameOrCn = function () {
        var par = {};
        if($(".inp3").val() == null || $(".inp3").val() == "" || !$(".inp3").val()){
        	layer.msg("请填写学生姓名");
        	$('.add-popup').show();
    		$('.addn-popup').hide();
        	return;
        }
        par.userName = $(".inp3").val();
        par.xqid = $("#term .cur").attr("ids");
        par.classId = $("#cla .cur").attr("ids");
        par.gid = $("#grade .cur").attr("ids");
        if(!par.xqid || par.xqid == "" || par.xqid == null || !par.classId || par.classId == "" || par.classId == null || !par.gid || par.gid == "" || par.gid == null){
        	Common.render({tmpl: $('#student2List'), data: [], context: '#stu2List', overwrite: 1});
        	return;
        }
        Common.getData('/IsolateStudent/getIsolateStudentByNameOrCn.do', par, function (rep) {
        	 Common.render({tmpl: $('#student2List'), data: rep.message, context: '#stu2List', overwrite: 1});
        	 if(rep.message.length == 0){
        		 $('.add-popup').show();
        		 $('.addn-popup').hide();
        		 layer.msg("暂无对应的学生可添加");
        	 }
        	 $.each(rep.message, function (i, obj) {
                 var dto = {};
                 dto.id = obj.id;
                 dto.xqid = obj.xqid;
                 dto.userId = obj.userId;
                 dto.userName = obj.userName;
                 dto.classId = obj.classId;
                 dto.className = obj.className;
                 dto.gradeId = obj.gradeId;
                 dto.gradeName = obj.gradeName;
                 dto.studyNumber = obj.studyNumber;
                 dto.level = obj.level;
                 dto.sex = obj.sex;
                 studentList.push(dto);
             })
        })
    }
    
    stuAdmini.getStudentList = function () {
        var par = {};
        par.xqid = $("#term .cur").attr("ids");
        par.classId = $("#cla .cur").attr("ids");
        if(!par.xqid || par.xqid =="" || par.xqid == null || !par.classId || par.classId == "" || par.classId == null){
        	Common.render({tmpl: $('#studentList'), data: [], context: '#stuList', overwrite: 1});
        	return;
        }
        Common.getData('/IsolateStudent/getStudentDtoByClassId.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#studentList'), data: rep.message, context: '#stuList', overwrite: 1});
            
        })
    }
    
    stuAdmini.getClassList = function () {
        var par = {};
        par.xqid = $("#term .cur").attr("ids");
        par.gradeId = $("#grade .cur").attr("ids");
        if(!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null){
        	 Common.render({tmpl: $('#class_temp'), data: [], context: '#cla', overwrite: 1});
        	 return;
        }
        Common.getData('/new33classManage/getClassList.do', par, function (rep) {
            Common.render({tmpl: $('#class_temp'), data: rep.message, context: '#cla', overwrite: 1});
            $("#cla em:eq(0)").addClass("cur");
        })
        
        var hgt = $('#cla').height();
    	$("#Klass").parent().css('height',hgt);
    	$("#Klass").css('height',hgt);
    	$("#Klass").css('line-height',hgt+'px');
    }
    
    stuAdmini.getTermList = function () {
        Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
                //$("#term em:eq(0)").addClass("cur");
            }
        });
    }
    
    stuAdmini.getGradeList = function () {
        var xqid = $("#term .cur").attr("ids");
        if(!xqid || xqid == "" || xqid == null){
        	Common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
        	return;
        }
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            //$("#grade em:eq(0)").addClass("cur");
        })
    }

    function initGrade(){
        try {
            Common.userData("n33", function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                        if (obj.key == "classId") {
                            classId = obj.value;
                        }
                    })
                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("ids")){
                            $(this).click();
                        }
                    });
                    $("#cla em").each(function () {
                        if(classId==$(this).attr("ids")){
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
        var xqid = $("#term .cur").attr("ids");
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":xqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    module.exports = stuAdmini;
})