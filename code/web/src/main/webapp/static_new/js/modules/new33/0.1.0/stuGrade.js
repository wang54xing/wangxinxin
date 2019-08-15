/**
 * Created by albin on 2018/3/7.
 */
define('stuGrade', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var stuGrade = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var stuList = new Array();
    var gradeId = "";
    stuGrade.addClassDto = function () {
    	if($(".inp8").val() == null || $(".inp8").val() == "" || $(".inp8").val() == undefined){
    		layer.msg("班级名称必填");
    		return;
    	}
    	var dto = {};
        dto.className = $(".inp8").val();
        dto.xh = $(".inp7").val();
        dto.xqid = $("#term .cur").attr("ids");
        dto.gid = $("#grade .cur").attr("ids");
        if(!dto.xqid || dto.xqid == "" || dto.xqid == null || !dto.gid || dto.gid == "" || dto.gid == null){
        	layer.msg("添加班级失败");
        	return;
        }
        Common.getPostBodyData('/new33classManage/addClassDto.do', dto, function (rep) {
        	$(".inp8").val("");
        	$(".inp7").val("");
            $('.addg-popup,.bg').hide();
        })
        stuGrade.getClassList();
    }
  
    stuGrade.init = function () {
    	$("body").on("click", ".ss", function(){
    		stuGrade.addClassDto();
    	})
    	
    	$("body").on("click", "#grade em", function () {
            $("#grade em").removeClass("cur")
            $(this).addClass("cur")
            stuGrade.getIsolateClass();
            stuGrade.getClassList();
        })
        
        $("body").on("click", "#term em", function () {
            $("#term em").removeClass("cur")
            $(this).addClass("cur")
            stuGrade.getGradeList();
            stuGrade.getClassList();
            stuGrade.getIsolateClass();
        })
        
        $("body").on("click", ".opt", function () {
        	 stuGrade.getClassDto($(this).attr("ids"));
        })
        
        $("body").on("click", ".download", function () {
        	window.location.href="/new33classManage/exportTemplate.do";
        })
        
         $("body").on("click", ".sc", function () {
            var par = {};
            par.xqid =  $("#term .cur").attr("ids");
            if(!par.xqid || par.xqid=="" || par.xqid == null){
            	layer.alert("请选择一个学期");
            	return;
            }
            if ($("#file").val() != "") {
                var value = $("#file").val();
                var result = /\.[^\.]+/.exec(value);
                if (result == ".xls" || result == ".xlsx") {
                	$(".load").show();
                	$('.bg').show();
                    $.ajaxFileUpload({
                        url: '/new33classManage/addClassImpYu.do',
                        async: false,
                        param: par,
                        secureuri: false,
                        fileElementId: 'file',
                        dataType: 'json',
                        success: function (data) {
                        	$(".load").hide();
                       	 	$('.bg').hide();
                       	 	window.location.href = document.URL;
                        },
                        error: function (e) {
                        	$(".load").hide();
                       	 	$('.bg').hide();
                            layer.msg("网络错误，请重试！！");
                        }
                    })
                } else {
                    layer.msg("请使用excel格式导入");
                }
            } else {
                layer.msg("请选择文件");
            }
        })
        
        $("#file").change(function () {
            var _this = $(this);
            var nameArr = _this.val().split("\\");
            var filename = nameArr[nameArr.length - 1];
            $(".file-name").html(filename);
        })
        
        $("body").on("click", ".opts", function () {
        	  var par = {};
        	  par.id = $(this).attr("ids");
        	  par.stuCount = $(this).attr("stuCount");
              layer.confirm('确认删除？', {
                  btn: ['确定', '取消'] //按钮
              }, function () {
            	  Common.getData('/new33classManage/deleteClass.do', par, function (rep) {
              		layer.msg(rep.message);
              		stuGrade.getClassList();
                  });
              },function () {
              }); 
         });

        $("body").on("click", ".qd", function () {
        	 var dto = {};
             dto.id = $(this).attr("ids");
             dto.xqid = $(this).attr("xqid");
             dto.classId = $(this).attr("classId");
             dto.buid = $(this).attr("buid");
             dto.gid = $(this).attr("gid");
             dto.type = $(this).attr("type");
             dto.bunm = $(this).attr("bunm");
             dto.bz = $(this).attr("bz");
             dto.stus = stuList;
             stuGrade.updateClass(dto);
        })

        stuGrade.getTermList();
        stuGrade.getDefaultCi();
        stuGrade.getGradeList();
        stuGrade.getIsolateClass();
        stuGrade.getClassList();
        initGrade();
    }
    
    stuGrade.getIsolateClass = function () {
    	var par = {};
        par.xqid = $("#term .cur").attr("ids");
        if(par.xqid != "" && par.xqid != undefined && par.xqid != null){
        	Common.getData('/new33classManage/getIsolateClass.do', par, function (rep) {
                
            });
        }else{
        	layer.msg("同步班级时学期为空");
        }
    }
    
    stuGrade.updateClass = function (dto) {
    	if($(".claName").val() == null || $(".claName").val() == "" || $(".claName").val() == undefined){
    		layer.msg("班级名称必填");
    		return;
    	}
        dto.className = $(".claName").val();
        dto.xh = $(".xh").val();
        Common.getPostBodyData('/new33classManage/updateClassDto.do', dto, function (rep) {
            $('.editg-popup,.bg').hide();
        })
        stuGrade.getClassList();
    }
    
    stuGrade.getClassDto = function (id) {
        stuList = new Array();
    	$('.editg-popup,.bg').show();
    	var par = {};
        par.id = id;
        Common.getData('/new33classManage/getClassDto.do', par, function (rep) {
            $(".claName").val(rep.message.className)
            $(".xh").val(rep.message.xh)
            $(".qd").attr("ids", rep.message.id)
            $(".qd").attr("xqid", rep.message.xqid)
            $(".qd").attr("classId", rep.message.classId)
            $(".qd").attr("buid", rep.message.buid)
            $(".qd").attr("gid", rep.message.gid)
            $(".qd").attr("type", rep.message.type)
            $(".qd").attr("bunm", rep.message.bunm)
            $(".qd").attr("bz", rep.message.bz)
            stuList = rep.message.stus;
        })
	}
    
    stuGrade.getClassList = function () {
        var par = {};
        par.xqid = $("#term .cur").attr("ids");
        par.gradeId = $("#grade .cur").attr("ids");
        if(!par.xqid || par.xqid == "" || par.xqid == null || !par.gradeId || par.gradeId == "" || par.gradeId == null){
        	Common.render({tmpl: $('#classList'), data: [], context: '#claList', overwrite: 1});
        	return;
        }
        Common.getData('/new33classManage/getClassList.do', par, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#content").hide();
            }
            else{
                $("#none_png").hide();
                $("#content").show();
            }
            Common.render({tmpl: $('#classList'), data: rep.message, context: '#claList', overwrite: 1});
        })
        
    }
    
    stuGrade.getTermList = function () {
        Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if (rep.code == 200) {
                Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term', overwrite: 1});
                //$("#term em:eq(0)").addClass("cur");
            }
        });
    }
    
    stuGrade.getGradeList = function () {
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

    stuGrade.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term em").each(function () {
                if(rep.message.paikeci==$(this).attr("ids")){
                    $(this).addClass("cur");
                }
            });
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
                    })
                    $("#grade em").each(function () {
                        if(gradeId==$(this).attr("ids")){
                            $(this).click();
                        }
                    });

                } else {

                }
            });
        } catch (x) {

        }
    }
    module.exports = stuGrade;
})