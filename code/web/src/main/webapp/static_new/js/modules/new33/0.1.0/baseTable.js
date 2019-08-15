/**
 * Created by albin on 2017/7/25.
 */

define('baseTable', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var baseTable = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    var ciId = "";
    var gdsxFlag = true;
    Common = require('common');
    /**全局变量**/

    var gradeId = new Array();
    var deXqid = "";

    baseTable.init=function(){
    	baseTable.getTermList();
        baseTable.getDefaultCi();
    	baseTable.getListBySchoolId();
    	baseTable.initRome();
        baseTable.getGradeWeek();
        baseTable.getGradeList();
		baseTable.getListBySchoolIdTime();
        baseTable.getChushi();
        baseTable.getChushiForGDSX();
        baseTable.getZouBanTimeList();
        baseTable.getGDSXBySidAndXqid();

        $("body").on("click",".btn11",function () {
            if($(this).text().indexOf('编辑')!=-1){
                $(this).text('保存')
                $('.t-tbo td input').removeAttr('readonly');
                //$('.t-tbo .tEdit input').css('border','1px solid #000');
                $('.tp').each(function () {
                    if($(this).attr("x") == 0 && $(this).attr("y") == 0){
                        $(this).focus();
                    }
                });
            }else{
                $('.tp').each(function () {
                    if($(this).val().length > 8){
                        gdsxFlag = false;
                    }
                });
                $(this).text('编辑')
                $('.t-tbo td input').attr('readonly',true);
                $('.t-tbo .tEdit input').css('border','none');
                if(gdsxFlag){
                    baseTable.saveGDSX();
                }else{
                    layer.alert("固定事项字数不允许超过8个字");
                    gdsxFlag = true;
                }
                $('.tp').each(function () {
                    $(this).val("");
                });
                baseTable.getGDSXBySidAndXqid();
            }
        });

        var flag = getCiIdIsFaBu();
    	 // 区间设置
    	 $('.btn-qjsz').click(function(){
    	     if(flag){
                layer.alert("课表已发布,区间不能修改");
                return;
             }
    		 baseTable.getDayRange();
 			$('.wind-qjsz,.bg').fadeIn();
 		})
 		// 新增课节
 		$('.xzkj').click(function(){
            if(flag){
                layer.alert("课表已发布,课节不能修改");
                return;
            }
            var obj = getLastCourse();
            if(obj){
                $("#course_serial").val(obj.serial);
                $("#course_name").val(obj.name);
            }
            else{
                $("#course_serial").val("");
                $("#course_name").val("");
            }
 	    	$("#course_start").val("");
 	    	$("#course_end").val("");
 	    	$("#course_range").attr("eid",null);
 			$('.wind-xzkj,.bg').fadeIn();
 		})
 		$('.wind .d1 em,.wind .dbtn .btn-no').click(function(){
 			$('.wind,.bg').fadeOut();
 		})
 		$('.cou-x em').click(function(){
 			$('.reset').show();
 		});
 		$('.d-sty em').click(function(){
 			$(this).addClass('cur').siblings().removeClass('cur');
 		});
 		$('.vonav span').click(function(){
 			$(this).addClass('active').siblings('span').removeClass('active')
 		});
 		$("#term").change(function(){
 			baseTable.getListBySchoolId();
            baseTable.getGradeWeek();
 		});
        $("#term2").change(function(){
            baseTable.getListBySchoolIdTime();
            baseTable.getChushi();
            baseTable.getGradeList();
            baseTable.getZouBanTimeList();
            baseTable.getGradeWeek();
        });

        $("#grade").change(function(){
            baseTable.getListBySchoolIdTime();
            baseTable.getChushi();
            baseTable.getZouBanTimeList();
            baseTable.getGradeWeek();
        });
 		// 删除
 		$("body").on("click","#course_list .del",function(){
            if(flag){
                layer.alert("课表已发布，不允许删除");
                return;
            }
 			baseTable.removeCourse($(this).attr("eid"));
 		});
 		// 编辑
 		$("body").on("click","#course_list .edit",function(){
            if(flag){
                layer.alert("课表已发布，不允许修改");
                return;
            }
 			$("#course_serial").val($(this).attr("serial"));
 	    	$("#course_name").val($(this).attr("ename"));
 	    	$("#course_start").val($(this).attr("start"));
 	    	$("#course_end").val($(this).attr("end"));
 	    	$("#course_range").attr("eid",$(this).attr("eid"));
 			$('.wind-xzkj,.bg').fadeIn();
 		});
 		// 保存课节
 		$("#course_save").click(function(){
 			baseTable.saveCourse();
 		});
 		$("#dayrange_save").click(function(){
 			baseTable.saveDayRange();
 		});
 		$("#sync_last").click(function(){
 			 baseTable.syncLastTermRange();
 		});

 		$("body").on("change",".six",function () {
 			if($(this).is(":checked")){
                $(".six").prop("checked",true);
			}else{
                $(".six").prop("checked",false);
                $(".seven").prop("checked",false);
            }
            baseTable.saveList($(this));
        });

        $("body").on("change",".seven",function () {
            if($(this).is(":checked")){
                $(".six").prop("checked",true);
            }
            baseTable.saveList($(this));
        });

        $("body").on("change","#gdgrade",function () {
            $('.tp').each(function () {
                $(this).val("");
            });
            baseTable.getGDSXBySidAndXqid();
        });


        $("body").on("click", ".itt", function () {
            baseTable.setZouBanTime($(this));
        });
        if(flag){
            $(".six").attr("disabled",true);
            $(".seven").attr("disabled",true);
        }
	}
    baseTable.getGDSXBySidAndXqid = function (){
        var par = {};
        par.gid = $("#gdgrade").val();
        par.xqid = deXqid;
        Common.getData('/gdsx/getGDSXByXqid.do', par, function (rep) {
            if(rep.code==200){
                $('.tp').each(function () {
                    $(this).parent().removeClass("tEdit");
                });
                $.each(rep.message,function (i,st) {
                    $('.tp').each(function () {
                        if($(this).attr("x") == st.x && $(this).attr("y") == st.y){
                            $(this).val(st.desc)
                            $(this).parent().addClass("tEdit");
                        }
                    });
                })
            }
        });
    }
	baseTable.saveGDSX = function (){
        var gdsxdtos = new Array();
        $(".tp").each(function () {
            if($(this).val().trim() != ""){
                var par = {};
                par.x = $(this).attr("x");
                par.y = $(this).attr("y");
                par.desc = $(this).val().trim();
                par.termId = deXqid;
                par.gradeId = $("#gdgrade").val();
                gdsxdtos.push(par);
            }
        });
        if(gdsxdtos.length > 0){
            Common.getPostBodyData('/gdsx/updateGDSX.do',gdsxdtos,function (rep) {
                layer.msg(rep.message);
            });
        }else{
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $("#gdgrade").val();
            Common.getData('/gdsx/deleteGDSX.do', par , function (rep) {
                if(rep.code==200){
                    layer.msg("保存成功")
                }
            });
        }
    }

    baseTable.getTermList = function(){
    	Common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
    		  if(rep.code==200){
    			Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
    			Common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term2',overwrite:1});
    		  }
    	});
    }
    baseTable.getListBySchoolId = function(){
    	var xqid = $("#term").val();
    	if(xqid&&xqid!=null){
    		Common.getData('/courseset/getListBySchoolId.do', {"xqid":xqid}, function (rep) {
                if(rep.length==0){
                    $("#none_png").show();
                    $("#content").hide();
                }
                else{
                    $("#none_png").hide();
                    $("#content").show();
                }
        		Common.render({tmpl: $('#course_list_temp'), data: rep, context: '#course_list',overwrite:1});
      	  	});
    	}
    }

    baseTable.getZouBanTimeList = function(){
    	var par = {}
        par.xqid = $("#term").val();
        par.gid = $("#grade").val();
        if(par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gid != "" && par.gid != undefined && par.gid != null){
            Common.getData('/courseset/getZouBanTime.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    if(obj.type == 0){
                        $(".itt").each(function (o,st) {
                            if($(this).attr("x") == obj.x && $(this).attr("y") == obj.y){
                                $(this).prop("checked","checked");
                            }
                        });
                    }
                })
            });
		}
        var flag = getCiIdIsFaBu();
		if(flag){
            $(".itt").prop("disabled","disabled");
        }
	}

    baseTable.setZouBanTime = function (tr) {
        var par = {};
        par.xqid = $("#term").val();
        par.x = tr.attr("x");
        par.y = tr.attr("y");
        par.gid = $("#grade").val();
        if(tr.is(":checked")){
            par.type = 0;
        }else{
            par.type = 1;
        }
        if(par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gid != "" && par.gid != undefined && par.gid != null){
            Common.getData('/courseset/setZouBanTime.do', par, function (rep) {
                baseTable.getZouBanTimeList();
            });
		}

    }

    baseTable.getChushi = function () {
        var y = 1;
        var x = 1;
        $(".itt").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 8) {
                y += 1;
                x = 1;
            }
        })
    }

    baseTable.getChushiForGDSX = function () {
        var y = 0;
        var x = 0;
        $(".tp").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            x += 1;
            if (x == 7) {
                y += 1;
                x = 0;
            }
        })
    }

    // 保存课节
    baseTable.saveCourse = function(){
    	var dto = {};
    	if($("#course_range").attr("eid")!=null){
    		dto.id= $("#course_range").attr("eid");
    	}
    	dto.xqid = $("#term").val();
    	dto.serial = $("#course_serial").val();
    	dto.name = $("#course_name").val();
    	dto.start = $("#course_start").val();
    	dto.end = $("#course_end").val();
    	if(!dto.serial||dto.serial==null){
    		layer.alert("序号不能为空");
    		return;
    	}
    	if(!dto.name||dto.name==null){
    		layer.alert("课节名不能为空");
    		return;
    	}
    	if(!dto.start||dto.start==null){
    		layer.alert("开始时间不能为空");
    		return;
    	}
    	if(!dto.end||dto.end==null){
    		layer.alert("结束时间不能为空");
    		return;
    	}
    	Common.getPostBodyData('/courseset/saveCourseRange.do', dto, function (rep) {
    		baseTable.getListBySchoolId();
    		$('.wind,.bg').fadeOut();
  	  	});
    }
    baseTable.removeCourse = function(id){
    	Common.getData('/courseset/removeCourseRnage.do', {"id":id}, function (rep) {
    		baseTable.getListBySchoolId();
    		$('.wind,.bg').fadeOut();
  	  	});
    }
    baseTable.saveDayRange = function(){
    	var dto = {};
    	if($("#day_range").attr("eid")!=null){
    		dto.id = $("#day_range").attr("eid");
    	}
    	dto.start1 = $("#dt1").val();
    	dto.end1 = $("#dt2").val();
    	dto.start2 = $("#dt3").val();
    	dto.end2 = $("#dt4").val();
    	dto.start3 = $("#dt5").val();
    	dto.end3 = $("#dt6").val();
        var start1 = parseInt(dto.start1);
        var end1 = parseInt(dto.end1);
        var start2 = parseInt(dto.start2);
        var end2 = parseInt(dto.end2);
        var start3 = parseInt(dto.start3);
        var end3 = parseInt(dto.end3);
    	if(dto.start1 == "" || dto.end1 == "" || dto.start2 == "" || dto.end2 == "" || dto.start3 == "" || dto.end3 == ""){
			layer.msg("数据填写不完整");
			return;
		}
        if(start1 >= end1  || start2 >= end2 || start3 >= end3 ){
            layer.msg("时间填写不符合规范");
            return;
        }
    	Common.getPostBodyData('/courseset/saveDayRange.do', dto, function (rep) {
    		baseTable.getListBySchoolId();
    		$('.wind,.bg').fadeOut();
  	  	});
    }
    baseTable.getDayRange = function(){
    	Common.getData('/courseset/getDayRange.do', {}, function (dto) {
    		if(dto&&dto!=null){
    			$("#day_range").attr("eid",dto.id);
    			$("#dt1").val(dto.start1);
    	    	$("#dt2").val(dto.end1);
    	    	$("#dt3").val(dto.start2);
    	    	$("#dt4").val(dto.end2);
    	    	$("#dt5").val(dto.start3);
    	    	$("#dt6").val(dto.end3);
    		}
  	  	});
    }
    // 手动同步
    baseTable.syncLastTermRange = function(){
    	var xqid = $("#term").val();
    	if(xqid&&xqid!=null){
    		Common.getData('/courseset/syncLastTermRange.do', {"xqid":xqid}, function (rep) {
    			baseTable.getListBySchoolId();
      	  	});
    	}
    }

    baseTable.getGradeWeek = function(){
        var xqid = $("#term").val();
        if(xqid&&xqid!=null){
            Common.getData('/n33_gradeweekrange/getGradeWeekRangeByXqidForView.do', {"xqid":xqid}, function (rep) {
				if(rep[0].end == 6){
					$(".six").prop("checked",true);
                    $(".seven").prop("checked",false);
				}else if(rep[0].end == 7) {
                    $(".six").prop("checked",true);
                    $(".seven").prop("checked",true);
				}else if(rep[0].end == 5) {
                    $(".six").prop("checked",false);
                    $(".seven").prop("checked",false);
				}
            });
        }
    }

    baseTable.saveList = function (cla) {
        var xqid = $("#term").val();
        var dto = {};
        dto.xqid = xqid;
        dto.start = 1;
        if (cla.attr("js") == "six" && cla.is(":checked")) {
            dto.end = 6;
        } else if (cla.attr("js") == "six" && !cla.is(":checked")) {
            dto.end = 5;
        }
        if (cla.attr("js") == "seven" && cla.is(":checked")) {
            dto.end = 7;
        } else if (cla.attr("js") == "seven" && !cla.is(":checked")) {
            dto.end = 6;
        }
        Common.getPostBodyData('/n33_gradeweekrange/saveListByXqid.do', dto, function (rep) {
            baseTable.getGradeWeek();
        });
    }
	  //请假时间选择初始化
    baseTable.initRome = function(){
	        rome(dt1,{date:false});
	        rome(dt2,{date:false});
	        rome(dt3,{date:false});
	        rome(dt4,{date:false});
	        rome(dt5,{date:false});
	        rome(dt6,{date:false});
	        rome(course_start,{date:false});
	        rome(course_end,{date:false});
	    }

    baseTable.getGradeList = function () {
        var xqid = $("#term").val();
        if(!xqid || xqid == "" || xqid == null){
            Common.render({tmpl: $('#grade_temp'), data: [], context: '#grade', overwrite: 1});
            return;
        }
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
        	gradeId = new Array();
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade', overwrite: 1});
            Common.render({tmpl: $('#gdgrade_temp'), data: rep.message, context: '#gdgrade', overwrite: 1});
            $("#grade em:eq(0)").addClass("cur");
            $.each(rep.message,function (i,obj) {
                gradeId.push(obj.gid);
            })
        })
    }

    baseTable.getListBySchoolIdTime = function () {
        var xqid = $("#term").val();
        if (xqid && xqid != null) {
            Common.getData('/courseset/getListBySchoolId.do', {"xqid": xqid}, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
                Common.render({tmpl: $('#gdThing_temp'), data: rep, context: '#gdThing', overwrite: 1});

            });
        }
    }

    baseTable.getDefaultCi = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            ciId = rep.message.paikeci;
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term").val(rep.message.paikeci);
            deXqid = rep.message.paikexq;
        });
    }

    function getLastCourse(){
        var obj = {};
        var arr = $("#course_list tr");
        if(arr.length>0){
            var temp = arr[arr.length-1];
            var arr2 = $(temp).find("td");
            obj.serial = Number($(arr2[0]).html())+1;
            obj.name = "第"+SectionToChinese(Number($(arr2[0]).html())+1)+"节课";
            return obj;
        }
    }
    var chnNumChar = ["零","一","二","三","四","五","六","七","八","九"];
    var chnUnitSection = ["","万","亿","万亿","亿亿"];
    var chnUnitChar = ["","十","百","千"];

    function SectionToChinese(section){
        var strIns = '', chnStr = '';
        var unitPos = 0;
        var zero = true;
        while(section > 0){
            var v = section % 10;
            if(v === 0){
                if(!zero){
                    zero = true;
                    chnStr = chnNumChar[v] + chnStr;
                }
            }else{
                zero = false;
                strIns = chnNumChar[v];
                strIns += chnUnitChar[unitPos];
                chnStr = strIns + chnStr;
            }
            unitPos++;
            section = Math.floor(section / 10);
        }
        return chnStr;
    }
    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":ciId}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    module.exports = baseTable;
})