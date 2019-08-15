/**
 * Created by albin on 2017/7/25.
 */

define('selectResult', ['jquery', 'doT', 'common','Rome', 'pagination', 'layer'], function (require, exports, module) {
    var selectResult = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    common = require('common');



    selectResult.init=function(){
    	selectResult.getTermList();
        selectResult.getDefaultCi();
    	selectResult.getgrade($("#term .cur").attr("val"));
    	selectResult.getStuSelectResultByClass();
        var gradeId = "";
        var type = "";
        initGrade();

        $('body').on("click",".itt",function () {
            if($("#type em[class='cur']").attr("val") == 3){
                $('.stu-xk-popup').show();
                $('.bg').show();
            }else if($("#type em[class='cur']").attr("val") == 1){
                $('#sel1').val(1);
                $('.stu-xkk-popup').show();
                $('.bg').show();
            }
            var string = "选科-";
            if($(this).hasClass("classItt")){
                var string1 = $(this).attr("xkName");
                var index = string1.indexOf("(");
                var string2 = string1.substring(0,index);
                var num = $(this).text();
                string2 += "(";
                string2 += num;
                string2 +=")";
                string += string2;
            }else{
                string += $(this).attr("xkName")
            }

            $('#xkName').text(string);
            $('#xkNamedan').text(string);
            $('#xkNamedan').attr("classId",null);
            $('#xkNamedan').attr("classId",$(this).attr("classId"));
            selectResult.getStuSelectResultByClass1($(this).attr("zuHecount"),$(this).attr("classId"));
        });

        $('.close,.qxx').click(function () {
            $(".bg").hide();
            $(".imselect-popup1").hide();
            $(".drsb-popup1").hide();
            $(".stu-xk-popup").hide();
            $(".stu-xkk-popup").hide();
        })
    	$("body").on("click","#term em",function(){
    		$(this).addClass('cur').siblings().removeClass('cur');
    		selectResult.getgrade($(this).attr("val"));
    		selectResult.getStuSelectResultByClass();
    	});
    	$("body").on("click","#grade em",function(){
    		$(this).addClass('cur').siblings().removeClass('cur');
    		selectResult.getStuSelectResultByClass();
            //年级改变
            selectResult.getAfreshClass();
    	});
    	$("body").on("click","#type em",function(){
    		$(this).addClass('cur').siblings().removeClass('cur');
    		selectResult.getStuSelectResultByClass();
    	});
    	$("#download").click(function(){
    		window.location.href="/new33school/set/exportTemplate.do";
    	});

    	$("body").on("click","#sc",function(){
            $(".bg").show();
            $(".imselect-popup1").show();
		});

    	$('body').on("change","#sel1",function () {
            selectResult.getHGStuIds();
        });

    	$("body").on("click","#qddr",function(){
            var par = {};
            par.xqid =  $("#term em[class='cur']").attr("val");
            if(!par.xqid|| par.xqid==""){
                layer.alert("请选择一个学期");
                return;
            }
            if ($("#file").val() != "") {
                var value = $("#file").val();
                var result = /\.[^\.]+/.exec(value);
                if (result == ".xls" || result == ".xlsx") {
                    $(".jhh").show();
                    $('.bg').show();
                    $(".jhhSp").text("正在导入学生选科，请稍候...");
                    $(".imselect-popup1").hide();
                    $.ajaxFileUpload({
                        url: '/new33school/set/importUser.do',
                        param: par,
                        secureuri: false,
                        fileElementId: 'file',
                        dataType: 'json',
                        success: function (data) {
                            $(".jhh").hide();
                            $('.bg').hide();
                            selectResult.getStuSelectResultByClass();
                            if(data.message.length>0){
                                common.render({tmpl: $('#errorList'), data: data.message, context: '#stuErrorList', overwrite: 1});
                                $(".bg").show();
                                $(".drsb-popup1").show();
                            }
                            var file = $("#file")
                            file.after(file.clone().val(""));
                            file.remove();
                            $(".file-name").html("");
                        },
                        error: function (e) {
                            $(".jhh").hide();
                            $('.bg').hide();
                            layer.msg("网络错误，请重试！！");
                        }
                    })
                    var file = $("#file")
                    file.after(file.clone().val(""));
                    file.remove();
                    $(".file-name").html("");
                } else {
                    layer.msg("请使用excel格式导入")
                }
            } else {
                layer.msg("请选择文件")
            }
		});
        $("body").on("change","#file",function () {
            var _this = $(this);
            var nameArr = _this.val().split("\\");
            var filename = nameArr[nameArr.length - 1];
            $(".file-name").html(filename);
        });
        $("#exportStuSelectResult").click(function () {
            selectResult.exportStuSelectResult();
        });
	}
    selectResult.getAfreshClass = function(){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        common.getPostData('/n33_set/getTurnOff.do', {"ciId":termId,"gradeId":gradeId,"type":1}, function (rep) {
            if(rep.code==200){
                if(rep.message.acClass==1){
                    $("#afreshId").show();
                }else{
                    $("#afreshId").hide();
                }
            }
        });
    };

    selectResult.getTermList = function(){
    	common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
    		  if(rep.code==200){
    			common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});

    		  }
    	  });
    }
    selectResult.getgrade = function(xqid){
    	common.getData('/new33isolateMange/getGradList.do', {"xqid":xqid}, function (rep) {
    		common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
    		// $("#grade em:eq(0)").addClass("cur");
    	})
    }
    selectResult.getStuSelectResultByClass = function(){
    	var termId = $("#term em[class='cur']").attr("val");
    	var gradeId = $("#grade em[class='cur']").attr("val");
    	var type = $("#type em[class='cur']").attr("val");
    	if(type == 1){
            $('#header').html("学科（等级/合格)");
        }else{
            $('#header').html("组合（总人数)");
        }
    	if(!termId||!gradeId||termId==""||gradeId==""||!type||type==""){
    		common.render({tmpl: $('#head_temp'), data: [], context: '#head',overwrite:1});
    		common.render({tmpl: $('#content2_temp'), data: [], context: '#content2',overwrite:1});
    		common.render({tmpl: $('#content_temp'), data: [], context: '#content',overwrite:1});
    		return;
    	}
    	common.getData('/new33school/set/getStuSelectResultByClass.do', {"xqid":termId,"gradeId":gradeId,"type":type}, function (rep) {
    	    if(rep.content.length==0){
                $("#none_png").show();
                $("#all_content").hide();
            }
            else{
                $("#none_png").hide();
                $("#all_content").show();
            }
    		common.render({tmpl: $('#head_temp'), data: rep.head, context: '#head',overwrite:1});
    		common.render({tmpl: $('#content2_temp'), data: rep.content, context: '#content2',overwrite:1});
    		common.render({tmpl: $('#content_temp'), data: rep.content, context: '#content',overwrite:1});
    	})

        $(".classItt").each(function (i,st) {
            $('.allItt').each(function (o,rt) {
                if($(st).attr("zuHecount") == $(rt).attr("zuHecount")){
                    $(st).attr("xkName",$(rt).attr("xkName"));
                }
            })
        })
    }

    selectResult.getStuSelectResultByClass1 = function(count,classId){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var type = $("#type em[class='cur']").attr("val");
        if(!termId||!gradeId||termId==""||gradeId==""||!type||type==""){
            common.render({tmpl: $('#head_temp'), data: [], context: '#head',overwrite:1});
            common.render({tmpl: $('#content2_temp'), data: [], context: '#content2',overwrite:1});
            common.render({tmpl: $('#content_temp'), data: [], context: '#content',overwrite:1});
            return;
        }
        common.getData('/new33school/set/getStuSelectResultByClass.do', {"xqid":termId,"gradeId":gradeId,"type":type}, function (rep) {
            var array = new Array();
            $.each(rep.content,function (i,st) {
               if(count == i){
                   if(classId == undefined || classId == "undefined"){
                       array = st.stuList;
                   }else{
                       $.each(st.stuList,function (r,ot) {
                           if(classId == ot.clsId){
                               array.push(ot)
                           }
                       })
                   }
               }
            })
            common.render({tmpl: $('#stuList_temp'), data: array, context: '#stuList',overwrite:1});
            common.render({tmpl: $('#stuList_tempdan'), data: array, context: '#stuListdan',overwrite:1});
        })
    }

    selectResult.getDefaultCi = function () {
        common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#term em").each(function () {
                if(rep.message.paikeci==$(this).attr("val")){
                    $(this).addClass("cur");
                }
            });
        });
    }

    selectResult.getHGStuIds = function () {
        var map = {};
        var termId = $("#term em[class='cur']").attr("val");
        map.xqid = termId;
        map.classId = $('#xkNamedan').attr("classId");
        var array = new Array();
        $('.stu').each(function (i,st) {
            array.push($(st).attr("stuId"))
        })
        map.stuIds = array;
        var gradeId = $("#grade em[class='cur']").attr("val");
        map.gradeId = gradeId;
        common.getPostBodyData('/new33school/set/getHGStuIds.do',map, function (rep) {
            common.render({tmpl: $('#stuList_tempdan'), data: rep, context: '#stuListdan',overwrite:1});
        });
    }

    selectResult.exportStuSelectResult = function(){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");

        if(!termId||!gradeId||termId==""||gradeId==""){
            layert.alert("年级和次不能为空");
            return;
        }
        window.location.href="/new33school/set/exportStuSelectResult.do?xqid="+termId+"&gradeId="+gradeId;
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
                    $("#type em").each(function () {
                        if(type==$(this).attr("val")){
                            $(this).click();
                        }
                    });
                } else {

                }
                selectResult.getAfreshClass();
            });
        } catch (x) {

        }
    }
    module.exports = selectResult;
})