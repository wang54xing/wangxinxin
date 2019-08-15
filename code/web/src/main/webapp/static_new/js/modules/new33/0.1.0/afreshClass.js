/**
 * Created by James on 2019-05-11.
 */
define('afreshClass', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'fselect','layer'], function (require, exports, module) {
    var afreshClass = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    require('fselect');
    var common = require('common');
    //初始化方法
    afreshClass.init = function () {
        afreshClass.getTermList();
        afreshClass.getDefaultCi();
        afreshClass.getgrade($("#term .cur").attr("val"));
        var gradeId = "";
        initGrade();
        //afreshClass.getStuSelectResultByClass();
        //afreshClass.getStuSelectResultByClass();


        $("body").on("click",".afresh",function(){

            afreshClass.afreshNewClass();
        })

        $("body").on("click",".daochu",function(){
        	var termId = $("#term em[class='cur']").attr("val");
            var gradeId = $("#grade em[class='cur']").attr("val");

            window.location.href="/afreshClass/exportClasStu.do?xqid=" + termId + "&gradeId=" + gradeId + "&type=" + 3;
            //afreshClass.exportStuSelectResult(); conn zhushi
        });

        $("body").on("click","#term em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            afreshClass.getgrade($(this).attr("val"));
            afreshClass.getStuSelectResultByClass();
        });
        $("body").on("click","#grade em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            afreshClass.getStuSelectResultByClass();
            //年级重置
            afreshClass.getExams();
            afreshClass.getSubjectList();
            afreshClass.getStuSelectResultByClass();
        });
        $("body").on("click","#type em",function(){
            $(this).addClass('cur').siblings().removeClass('cur');
            afreshClass.getSubjectList();
            afreshClass.getStuSelectResultByClass();
        });
        //-------选择考试成绩---------//
        //打开弹窗
        $("body").on("click", ".choose_ks", function () {
            var rid = $("#reportId").val();
            //console.log(rid);
            //$('input:radio[name="ks"]').each(function () {
            $('.selectReport').each(function () {
                //console.log($(this).val());
                if($(this).val()==rid){
                    $(this).prop('checked','true');
                }else{
                    $(this).removeAttr("checked");
                }
            });
            $('.choose-ks-popup').show()
            $('.bg').show()
        });

        //确定选择成绩单
        $("body").on("click",".choose_ks_qd",function (){
            var i = 0;
            $(".selectReport").each(function () {
                console.log($(this).attr("checked"));
                if($(this).prop("checked")){
                    $("#reportId").attr("value",$(this).val());
                    i++;
                }
            });
            if(i!=0){
                $('.choose-ks-popup').hide()
                $('.bg').hide()
            }else{
                layer.alert("请选择一次考试成绩");
            }

        })

        //关闭弹窗
        $("body").on("click",".choose_ks_close",function (){
            $('.choose-ks-popup').hide()
            $('.bg').hide()
        })

        //年级-组合 切换
        $(document).on('click','.d-sty em',function () {
            $(this).addClass('cur').siblings().removeClass('cur')
        })


        //---------------设置分班规则------------//
        //打开弹窗
        $("body").on("click",".set_rule",function () {
//            $(".classNumber1").each(function(){
//                $(this).val( $(this).attr("num"));
//            });
//            
//            $(".classNumber2").each(function(){
//                $(this).val( $(this).attr("num"));
//            })
//            
//            $(".classNumber3").each(function(){
//                $(this).val( $(this).attr("num"));
//            })
            afreshClass.getAfreshTypeList();
            $('.set-rule-popup').show();
            $('.bg').show();
        })

        //关闭弹窗
        $("body").on("click",".set_rule_close",function () {
            $('.set-rule-popup').hide();
            $('.bg').hide();
        });
        
        //保存
        $("body").on("click",".bc",function () {
        	var paramP = {};
        	var chooseSetList = new Array();
        	$(".sibb").each(function(){
        		var param = {};
        		param.id = $(this).attr('id');
        		param.classNum = $(this).children('td').eq(1).children().val();
    			param.classContain = $(this).children('td').eq(2).children().val();
    			param.floatNum = $(this).children('td').eq(3).children().val();
    			chooseSetList.push(param);
            });
        	paramP.chooseSetList = chooseSetList;
        	common.getPostBodyData('/afreshClass/updSubList.do', paramP , function (rep) {
        		layer.msg('操作成功');
        		 afreshClass.getSubjectList();
            });
        	$('.set-rule-popup').hide();
            $('.bg').hide();
        	
//            $(".classNumber1").each(function(){
//                $(this).attr("num",$(this).val());
//            })
//            $(".classNumber2").each(function(){
//                $(this).attr("num",$(this).val());
//            })
//            $(".classNumber3").each(function(){
//                $(this).attr("num",$(this).val());
//            })
            
        });

        //打开 编辑全部规则弹窗
        $("body").on("click",".set_all_rule",function (){
            $('.set-all-rule-popup').show()
            $('.set-rule-popup').hide()
        });

        //关闭 编辑全部规则弹窗
        $("body").on("click",".set_all_rule_close",function(){
            $('.set-all-rule-popup').hide()
            $('.set-rule-popup').show()
        })
        //确定
        $("body").on("click",".set_all_rule_qd",function(){
            $(".classNumber1").val($("#ct1").val());
            $(".classNumber2").val($("#ct2").val());
            $(".classNumber3").val($("#ct3").val());
            $('.set-all-rule-popup').hide()
            $('.set-rule-popup').show()
        })



        // -------------调班--------------//
        //打开调整班级弹窗
        $(document).on('click','.stu',function () {
            var title = "选科-" + $(this).attr("xkName");
            var num = $(this).text();
            title += "("+num+")";
            $('#xkName').text(title);
            $('#xkName').attr("classId", $(this).attr('classId')||"");
            //$('#xkNamedan').text(title);
            //$('#xkNamedan').attr("classId",$(this).attr("classId")||"");
            $("#keyword").val("");
        	$("#keyword").attr("name", $(this).attr("zuHecount"));
        	$("#keyword").attr("sore", $(this).attr('classId')||"");
        	afreshClass.getStuSelectResultByClass2($("#keyword").attr("name"),$(this).attr('classId'),$("#keyword").val());
            $('.change-class-popup').show();
            $('.bg').show();
        });
        
        //关闭弹窗
        $("body").on("click",".change_class_close",function(){
            $('.change-class-popup').hide();
            $('.bg').hide();
        });
        
        //学生班级调整弹框
        $("body").on('click','.change_class',function () {
            var uIds = '';
            var clsIds = [];
            $('.stuChange').each(function(index, implent){
        		if($(this).is(':checked')){
        			uIds = uIds + ',' + $(this).attr('stuId');
                    var clsId = $(this).attr('clsId')||"";
                    if(clsId!=""){
                        if($.inArray(clsId, clsIds)==-1) {
                            clsIds.push(clsId);
                        }
                    }
                }
        	});
            if(uIds == '' || uIds == null || uIds == undefined)
            {
            	layer.msg('请选择调班学生');
            	return ;
            }
            //$('#nowClass').attr("name",$(this).attr("stuId"));
            afreshClass.tongBuClass(clsIds);
            $('.change-class-popup').hide();
            $('.set-change-class-popup').show();
        });
        
        //学生调整班级确定按钮
        $("body").on("click",".set_change_class_add",function(){
            afreshClass.updateClass();
            $('.change-class-popup').hide();
            $('.set-change-class-popup').hide();
            $('.bg').hide();
        });
        
        $("body").on("click",".set_change_class_close",function(){
            $('.change-class-popup').show()
            $('.set-change-class-popup').hide()
        });
        
        $('body').on("keyup","#keyword", function(event) {
            if (event.keyCode == "13") {
                afreshClass.getStuSelectResultByClass1($("#keyword").attr("name"),$("#keyword").attr("sore"),$("#keyword").val());
            }
        });

        $('body').on("click","#content2 .itt",function () {
            $("#keyword").val("");
            if($("#type em[class='cur']").attr("val") == 3){
                $('.change-class-popup').show()
                $('.bg').show()
            }else if($("#type em[class='cur']").attr("val") == 1){
                $('#sel1').val(1);
                $('.change-class-popup').show()
                $('.bg').show()
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
                string += $(this).attr("xkName");
            }
            $('#xkName').text(string);
            $('#xkName').attr("classId", "");
            //$('#xkNamedan').text(string);
            //$('#xkNamedan').attr("classId", $(this).attr("classId")||"");
            $("#keyword").attr("name", $(this).attr("zuHecount"));
            $("#keyword").attr("sore", $(this).attr("classId")||"");
            afreshClass.getStuSelectResultByClass1($(this).attr("zuHecount"),$(this).attr("classId"),"");
        });

        $("body").on("click","#keyword2",function(){
            afreshClass.getStuSelectResultByClass1($("#keyword").attr("name"),$("#keyword").attr("sore"),$("#keyword").val());
        });

        //修改班级名称
        $("body").on('click','.edit_name',function () {
            var classId = $(this).parent().attr("classId")||"";
            var xh = $(this).parent().attr("xh")||"";
            $("#classId").val(classId);
            $("#classXuHao").val(xh);
            $('.class-name-popup').show();
            $('.bg').show();
        });

        $('.class-name-popup .class_name_close, .class-name-popup .qxx').click(function () {
            $("#classId").val("");
            $("#classXuHao").val('');
            $('.class-name-popup').hide();
            $('.bg').hide();
        });

        $('.class-name-popup .qd').click(function () {
            afreshClass.updateClassXh();
        });

        //-----------------------------删除班级---------------------------//
       
        $('#bjList').fSelect({
            showSearch: false
        });
        
        
        $('#delClass').click(function () {
        	var termId = $("#term em[class='cur']").attr("val");
            var gradeId = $("#grade em[class='cur']").attr("val");
            var type = $("#type em[class='cur']").attr("val");
            var reportId = $("#reportId").val();
            if(reportId == null || reportId == undefined || reportId == '')
            	reportId = '';
            $('#bjList').val('');
        	common.getData('/afreshClass/getStuSelectResultByClass.do',{"xqid":termId,"gradeId":gradeId,"type":type, "reportId":reportId} , function (rep) {
                
                common.render({tmpl: $('#bjTempl'), data: rep.head, context: '#bjList',overwrite:1});
               
                $('#bjList').fSelect('reload');
                
            });
            $('.class-del-popup').show();
            $('.bg').show();
        });
        
        //删除班级确定按钮
        $("body").on("click","#delLca",function () {
        	var classIds = '';
        	$('#bjList  option:selected').each(function(i, element){
        		classIds = classIds + ',' + $(this).val();
        	});
        	if(classIds == null || classIds == '' || classIds == undefined ){
        		layer.msg('请选择班级');
        		return;
        	}
        	common.getData('/afreshClass/delClassById.do?classIds=' + classIds,{} , function (rep) {
                if(rep.code == '200'){
                	afreshClass.getStuSelectResultByClass();
                	$('.class-del-popup').hide()
                    $('.bg').hide()
                }
            });
        });
        
        //新增班级按钮
        $("body").on("click","#addClassP",function () {
        	var param  = {};
        	param.termId = $("#term em[class='cur']").attr("val");
        	param.gradeId = $("#grade em[class='cur']").attr("val");
            common.getPostBodyData('/afreshClass/addAfrClass.do', param, function (rep) {
                if(rep.code==200){
                    afreshClass.getStuSelectResultByClass();
                }else{
                	
                }

            });
        });
        
        $("body").on("click",".class_del_close,.qxx",function () {
            $('.class-del-popup').hide();
            $('.bg').hide();
        });


        $('input[name="afreshType"]').change(function () {
            if($(this).val()=='2'){
                $('.dk_edit').show();
            }else{
                $('.dk_edit').hide();
            }
            afreshClass.editAfreshType();
        });

        $('.dk_edit').click(function () {
            afreshClass.getDkyxSubList();
            $('.dkyx-popup').show();
        });

        $('.dkyx_close').click(function () {
            $('.dkyx-popup').hide();
        });

        $('.dkyx-popup .dkyx_submit').click(function () {
            afreshClass.editAfreshSubRuleList();
        });
    };

    afreshClass.getAfreshTypeList = function(){
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val");
        param.ciId = $("#term em[class='cur']").attr("val");
        common.getPostData('/afreshClass/getAfreshTypeList.do', param, function (rep) {
            if(rep.code=='200'){
                if(rep.message.length==0){
                    afreshClass.editAfreshType();
                }else{
                    $.each(rep.message, function (i, obj) {
                        if(obj.use==1){
                            $("input[name='afreshType'][value="+obj.afreshType+"]").prop("checked", true);
                            if(obj.afreshType=='2'){
                                $('.dk_edit').show();
                            }else{
                                $('.dk_edit').hide();
                            }
                        }
                    });
                }
            }
        });
    };

    afreshClass.editAfreshType = function(){
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val");
        param.ciId = $("#term em[class='cur']").attr("val");
        param.afreshType = $("input[name='afreshType']:checked").val();
        param.use = 1;
        common.getPostBodyData('/afreshClass/editAfreshType.do', param, function (rep) {
            if(rep.code=='200'){

            }
        });
    };

    afreshClass.editAfreshSubRuleList = function(){
        var list = new Array();
        var gradeId = $("#grade em[class='cur']").attr("val");
        var ciId = $("#term em[class='cur']").attr("val");
        var afreshType = $("input[name='afreshType']:checked").val();
        $("#yxSubList tr").each(function(i, tr){
            var item = {};
            item.gradeId = gradeId;
            item.ciId = ciId;
            item.afreshType = afreshType;
            item.subId = $(tr).attr("subId")||"";
            item.number = $(tr).find(".number").val();
            item.volume = $(tr).find(".volume").val();
            list.push(item);
        });
        common.getPostBodyData('/afreshClass/editAfreshSubRuleList.do', list, function (rep) {
            if(rep.code=='200'){
                $('.dkyx-popup').hide();
            }
        });
    };

    afreshClass.getDkyxSubList = function(){
        var param = {};
        param.gradeId = $("#grade em[class='cur']").attr("val");
        param.ciId = $("#term em[class='cur']").attr("val");
        param.afreshType = $("input[name='afreshType']:checked").val();
        common.getPostData('/afreshClass/getDkyxSubList.do', param, function (rep) {
            $("#yxSubList").html("");
            if(rep.code=='200'){
                common.render({tmpl: $('#yxSubList_tmpl'), data: rep.message, context: '#yxSubList',overwrite:1});
            }
        });
    };

    afreshClass.updateClassXh = function(){
        var updParam = {};
        updParam.classId = $("#classId").val();
        updParam.xh = $("#classXuHao").val();
        common.getData('/afreshClass/updateClassXh.do', updParam, function (rep) {
            if(rep.code=='200'){
                var msg = rep.message;
                if(msg.stateCode==0){
                    $('#head em[classId='+updParam.classId+']').attr("xh", updParam.xh);
                    $('#head em[classId='+updParam.classId+']').attr("nm", msg.className);
                    $('#head em[classId='+updParam.classId+']').find(".showClassName").text(msg.className);
                    $("#classId").val("");
                    $("#classXuHao").val("");
                    $('.class-name-popup').hide();
                    $('.bg').hide();
                    //afreshClass.getStuSelectResultByClass1($("#keyword").attr("name"),$("#keyword").attr("sore"),$("#keyword").val());
                }else{
                    layer.alert('“'+updParam.xh+'”'+"该班级序号已存在！");
                }
            }
        });
    }

    //调班
    afreshClass.updateClass = function(){
    	var stuInfos = [];
        var newClassId = $("#optionId").val();
    	$('.stuChange').each(function(i, stu){
            if($(stu).is(':checked')) {
                var stuId = $(stu).attr('stuId') || "";
                var oldClassId = $(stu).attr('clsId') || "";
                if (stuId != "") {
                    var stuInfo = {};
                    stuInfo.stuId = stuId;
                    stuInfo.oldClassId = oldClassId;
                    stuInfo.newClassId = newClassId;
                    stuInfos.push(stuInfo);
                }
            }
    	});
        common.getPostBodyData('/afreshClass/updateClass.do', stuInfos, function (rep) {
            layer.msg('操作成功');
        	afreshClass.getStuSelectResultByClass1($("#keyword").attr("name"),$("#keyword").attr("sore"),$("#keyword").val());
        });
    };

    //调班查询
    afreshClass.tongBuClass = function(clsIds){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var classId = $('#xkName').attr("classId")||"";
        common.getPostData('/afreshClass/tongBuClass.do', {"xqid":termId,"gradeId":gradeId,"classId":classId}, function (rep) {
            var classList = [];
            if(classId!="") {
                classList = rep.message.list;
                $("#nowClass").text(rep.message.now.className);
                $("#nowClass").val(rep.message.now.classId);
                $("#currClassSel").parent().show();
            }else{
                $("#currClassSel").parent().hide();
                $.each(rep.message.list, function (i, obj) {
                    if($.inArray(obj.classId, clsIds)==-1){
                        classList.push(obj);
                    }
                });
            }
            common.render({tmpl: $('#optionId_tempdan'), data: classList, context: '#optionId',overwrite:1});
        });
    };

    //导出数据
    afreshClass.exportStuSelectResult = function(){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        window.location.href="/afreshClass/exportStuSelectResult.do?xqid="+termId+"&gradeId="+gradeId;
    };

    //重新分班
    afreshClass.afreshNewClass = function(){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var reportId = $("#reportId").val();
        //List<Map<String,Object>> ruleMapList
        var ruleMapList = [];
        //number volume swim type compose
        $(".sibb").each(function(){
            var compose = "";
            $(this).find(".className").each(function(){
                compose = $(this).attr("name");
            })
            if(compose=="化生地"){
                compose = "物生地";
            }
            var number = "";
            $(this).find(".classNumber1").each(function(){
                number += $(this).val();
                number += ",";
            })
            var volume = "";
            $(this).find(".classNumber2").each(function(){
                if($(this).val()<=0 || $(this).val()==""){
                    layer.alert("班级容量应大于0");
                    return;
                }
                volume += $(this).val();
                volume += ",";
            })
            var swim = "";
            $(this).find(".classNumber3").each(function(){
                swim += $(this).val();
                swim += ",";
            })
            var type = "";
            $(this).find(".classNumber3").each(function(){
                if(reportId!=""){
                    type += "1";
                }else{
                    type += "0";
                }
                type += ",";
            })
            var dto = {
                "compose":compose,
                "number":number,
                "volume":volume,
                "swim":swim,
                "type":type
            };
            ruleMapList.push(dto);
        });
        console.log(ruleMapList);

        console.log($(".guize").find("input"));
        common.getPostBodyData('/afreshClass/goAfreshClassEntry.do', {
            "xqid":termId,
            "gradeId":gradeId,
            "reportId":$("#reportId").val(),
            "ruleMapList": ruleMapList
        }, function (rep) {
            if(rep.code==200){
                layer.alert(rep.message);
                afreshClass.getStuSelectResultByClass();
            }else{
                layer.alert(rep.message);
            }

        });
    };

    //学期列表
    afreshClass.getTermList = function(){
        common.getData('/new33isolateMange/getTermPaikeTimes.do', {}, function (rep) {
            if(rep.code==200){
                common.render({tmpl: $('#term_temp'), data: rep.message, context: '#term',overwrite:1});
            }
        });
    };

    //当前默认学期
    afreshClass.getDefaultCi = function () {
        common.getData('/afreshClass/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            $("#timeLoad").attr("startTime",rep.message.startTime);
            $("#timeLoad").attr("endTime",rep.message.endTime);
            $("#term em").each(function () {
                if(rep.message.paikeci==$(this).attr("val")){
                    $(this).addClass("cur");
                }
            });
        });
    };

    //年级列表
    afreshClass.getgrade = function(xqid){
        common.getData('/new33isolateMange/getNewGradList.do', {"xqid":xqid}, function (rep) {
            common.render({tmpl: $('#grade_temp'), data: rep.message, context: '#grade',overwrite:1});
            //$("#grade em:eq(0)").addClass("cur");
        })
    };

    //获得总属性
    afreshClass.getStuSelectResultByClass = function(){
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var type = $("#type em[class='cur']").attr("val");
        var reportId = $("#reportId").val();
        if(reportId == null || reportId == undefined || reportId == '')
        	reportId = '';
        if(type == 1){
            $('#header').html("学科（等级/合格)");
        }else{
            $('#header').html("组合（总人数)");
        }
        if(!termId||!gradeId||termId==""||gradeId==""||!type||type==""){
            common.render({tmpl: $('#head_temp'), data: [], context: '#head',overwrite:1});
            common.render({tmpl: $('#content2_temp'), data: [], context: '#content2',overwrite:1});
            common.render({tmpl: $('#content3_temp'), data: [], context: '#content3',overwrite:1});
            return;
        }
        common.getData('/afreshClass/getStuSelectResultByClass.do', {"xqid":termId,"gradeId":gradeId,"type":type, "reportId": reportId}, function (rep) {
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
            common.render({tmpl: $('#content3_temp'), data: rep.content, context: '#content3',overwrite:1});
        })

        /*$(".classItt").each(function (i,st) {
            $('.allItt').each(function (o,rt) {
                if($(st).attr("zuHecount") == $(rt).attr("zuHecount")){
                    $(st).attr("xkName",$(rt).attr("xkName"));
                }
            })
        })*/
    };

    afreshClass.getStuSelectResultByClass1 = function(count,classId,keyword){
        console.log("keyword"+keyword)
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var type = $("#type em[class='cur']").attr("val");
        var reportId = $("#reportId").val();
        if(reportId == null || reportId == undefined || reportId == '')
        	reportId = '';
        if(!termId||!gradeId||termId==""||gradeId==""||!type||type==""){
            common.render({tmpl: $('#head_temp'), data: [], context: '#head',overwrite:1});
            common.render({tmpl: $('#content2_temp'), data: [], context: '#content2',overwrite:1});
            common.render({tmpl: $('#content3_temp'), data: [], context: '#content3',overwrite:1});
            return;
        }
        common.getData('/afreshClass/getStuSelectResultByClass.do', {"xqid":termId,"gradeId":gradeId,"type":type, "reportId":reportId}, function (rep) {
            var array = new Array();
            $.each(rep.content,function (i,st) {
                if(count == i){
                    if(classId == undefined || classId == "undefined"|| classId == ""){
                        $.each(st.stuList,function (r,ot) {
                            if(keyword==""){
                                array.push(ot)
                            }else{
                                if(ot.stuName.indexOf(keyword)!=-1){
                                    array.push(ot)
                                }
                            }
                        });
                    }else{
                        $.each(st.stuList,function (r,ot) {
                            if(classId == ot.clsId){
                                if(keyword==""){
                                    array.push(ot)
                                }else{
                                    if(ot.stuName.indexOf(keyword)!=-1){
                                        array.push(ot)
                                    }
                                }

                            }
                        })
                    }
                }
            })
            console.log(array)
            //common.render({tmpl: $('#stuList_temp'), data: array, context: '#stuList',overwrite:1});
            common.render({tmpl: $('#stuList_tempdan'), data: array, context: '#stuListdan',overwrite:1});

            common.render({tmpl: $('#head_temp'), data: rep.head, context: '#head',overwrite:1});
            common.render({tmpl: $('#content2_temp'), data: rep.content, context: '#content2',overwrite:1});
            common.render({tmpl: $('#content3_temp'), data: rep.content, context: '#content3',overwrite:1});
        })
    };
    //获得成绩单
    afreshClass.getExams = function () {
        var gradeIds = [];
        var timespan = [];
        gradeIds.push($("#grade em[class='cur']").attr("val"));
        timespan.push({
            "startDate": $("#timeLoad").attr("startTime"),
            "endDate": $("#timeLoad").attr("endTime")
            //无数据先写死
            //"startDate": "2019-02-20",
            //"endDate": "2019-07-01"
        })
        common.getPostBodyData('/afreshClass/getExamListByGradeTime.do', {
            "gradeIds": gradeIds,
            "timespan": timespan
        }, function (rep) {
            common.render({tmpl: $('#report_temp'), data: rep, context: '#report', overwrite: 1});
        });
    };

    //获得学科组合 james
//    afreshClass.getSubjectList = function (){
//        var termId = $("#term em[class='cur']").attr("val");
//        var gradeId = $("#grade em[class='cur']").attr("val");
//        common.getData('/afreshClass/getAfreshSubjectList.do', {"xqid":termId,"gradeId":gradeId}, function (rep) {
//            console.log(rep.message);
//            common.render({tmpl: $('#content_temp'), data: rep.message, context: '#content',overwrite:1});
//            //$("#grade em:eq(0)").addClass("cur");
//        })
//    };
    
  //获得学科组合 conn
    afreshClass.getSubjectList = function (){
        var termId = $("#term em[class='cur']").attr("val")||"";
        var gradeId = $("#grade em[class='cur']").attr("val")||"";
        var type = $("#type em[class='cur']").attr("val")||"";
        if(termId==""||gradeId==""||type==""){
            return false;
        }
        common.getData('/afreshClass/getNewAfreshSubList.do', {"xqid":termId,"gradeId":gradeId,"type":type}, function (rep) {
            console.log(rep.message);
            common.render({tmpl: $('#content_temp'), data: rep.message, context: '#content',overwrite:1});
            //$("#grade em:eq(0)").addClass("cur");
        })
    };

    //初始化年级班级
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
                afreshClass.getExams();
                afreshClass.getSubjectList();
                afreshClass.getStuSelectResultByClass();
            });
        } catch (x) {

        }

    }

    afreshClass.getStuSelectResultByClass2 = function(count,classId,keyword){
    	$('#stuListdan').html('');
        console.log("keyword"+keyword)
        var termId = $("#term em[class='cur']").attr("val");
        var gradeId = $("#grade em[class='cur']").attr("val");
        var type = $("#type em[class='cur']").attr("val");
        var reportId = $("#reportId").val();
        if(reportId == null || reportId == undefined || reportId == '')
        	reportId = '';
        if(!termId||!gradeId||termId==""||gradeId==""||!type||type==""){
            common.render({tmpl: $('#head_temp'), data: [], context: '#head',overwrite:1});
            common.render({tmpl: $('#content2_temp'), data: [], context: '#content2',overwrite:1});
            common.render({tmpl: $('#content3_temp'), data: [], context: '#content3',overwrite:1});
            return;
        }
        common.getData('/afreshClass/getStuSelectResultByClass.do', {"xqid":termId,"gradeId":gradeId,"type":type,"reportId":reportId}, function (rep) {
            var array = new Array();
            $.each(rep.content,function (i,st) {
                if(count == i){
                    if(classId == undefined || classId == "undefined"){
                        array = st.stuList;
                    }else{
                        $.each(st.stuList,function (r,ot) {
                            if(classId == ot.clsId){
                                if(keyword==""){
                                    array.push(ot)
                                }else{
                                    if(ot.stuName.indexOf(keyword)!=-1){
                                        array.push(ot)
                                    }
                                }

                            }
                        })
                    }
                }
            })
            console.log(array)
            common.render({tmpl: $('#stuList_tempdan'), data: array, context: '#stuListdan',overwrite:1});
        })
    };
    
    
    module.exports = afreshClass;
})

