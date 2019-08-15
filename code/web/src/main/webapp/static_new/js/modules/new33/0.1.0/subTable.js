/**
 * Created by albin on 2018/3/23.
 */
define('subTable', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var subTable = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Rome');
    Common = require('common');
    var deXqid = "";
    var deCid = "";
    var ciId = null;
    var desc = "";
    
    subTable.init = function () {
    	
    	subTable.getDefaultTerm();
        subTable.getGrade();
        subTable.getListByXq();
        subTable.getListBySchoolId();
        subTable.getSubjectList();
        subTable.getChushi();
        subTable.getGDSX();
        subTable.getGuDingShiWu();
        //subTable.getGradeSubjectTeaTable(); //查看课表专项显示错误注释 oz zs1
        subTable.getGradeSubTable();   		  //查看课表专项显示错误 替换zs的方法 oz
        subTable.getZhuanXiangZuHeXianQing();
        subTable.getCurrentJXZ();
    	
    	$('#export').click(function(){
    		subTable.getGrade2List();
    		subTable.getSub2List();
            $('.bg').show();
        	$('.dc-popup').show();
        });
        
        $('.close,.qx').click(function(){
             $('.bg').hide();
             $('.dc-popup').hide();
        });
    	
        $('body').on('change','#grade2',function(){
        	subTable.getSub2List();
        });
        
        $('body').on("click",".selectAll",function () {
            var flag = false;
            $('#subjectList').each(function (i,st) {
                if(!$(this).children().children('input').is(":checked")){
                    flag = true;
                }
            });
            if(flag){
                $('#subjectList').each(function (i,st) {
                	$(this).children().children('input').prop("checked",true);
                });
            }else{
                $('#subjectList').each(function (i,st) {
                	$(this).children().children('input').prop("checked",false);
                });
            }
        });
        
        //批量导出学科课表
        $('body').on("click",".dcAll",function () {
        	var par = {};
        	par.xqid = deXqid;
            par.gradeId = $(".grade .active").attr("ids");
            var subjectId = '';
            $('#subjectList label').each(function (i,st) {
                if($(this).children('input').is(":checked")){
                	subjectId = subjectId + ',' + $(this).children('input').attr('subid');
                }
            });
            par.subjectIds = subjectId;
            par.week = $("#week").val();
        	window.location.href = '/paike/exportSubsKB.do?xqid=' + par.xqid + "&gradeId=" + par.gradeId + "&subjectIds=" + par.subjectIds + "&week=" + par.week;
        });
        
        //导出学科课表
        $("body").on("click", ".b9", function () {
            var par = {};
            par.xqid = deXqid;
            par.gradeId = $(".grade .active").attr("ids");
            par.subjectId = $(".xueke .active").attr("ids");
            par.week = $("#week").val();
            //oz注释 window.location.href = "/paike/getGradeSubjectTeaTableImp.do?xqid=" + par.xqid + "&gradeId=" + par.gradeId + "&subjectId=" + par.subjectId+"&week="+par.week;
            window.location.href = "/paike/expSubKB.do?xqid=" + par.xqid + "&gradeId=" + par.gradeId + "&subjectId=" + par.subjectId+"&week="+par.week;
        });
        
        $('body').on('click', '#printGDG', function () {
            $('#printGDG').hide();
            var headstr = "<html><head><title></title></head><body>";
            var footstr = "</body>";
            var newstr = $('#GDGContent').html();
            var oldstr = document.body.innerHTML;
            document.body.innerHTML = headstr + newstr + footstr;
            window.print();
            console.log('老页面：' + oldstr);
            console.log('新页面：' + newstr);
            document.body.innerHTML = oldstr;
            $('#printGDG').show();
            return false;
        });
        
        $("body").on("mouseover", ".rd", function () {
            $(this).append($(".fc-popup"));
            $(".ddtt").empty();
            $(this).find(".title span").each(function (i, obj) {
                $(" <span>" + $(obj).html() + "</span><br></br>").appendTo(".ddtt")
            });
        });
        
        
        //$("body").on("click", ".rd", function () {
        //    $(".rd").css("background", "rgb(192, 213, 301)")
        //    $(this).css("background", "rgb(150, 213, 255)")
        //    $("#use").empty();
        //    $("#userCount").html("");
        //    $("#userCount").html($(this).find("span").length);
        //    $(this).find("span").each(function (i, obj) {
        //        $("<li class='cctv' > <span>" + $(obj).html() + "</span> </li>").appendTo("#use")
        //    })
        //})
        
        $("#week").change(function () {
            //subTable.getSubjectList();
            subTable.getChushi();
            subTable.getGDSX();
            subTable.getGuDingShiWu();
            subTable.getGradeSubTable();//oz
            //subTable.getGradeSubjectTeaTable();
            subTable.getCurrentJXZ();
        });
        
        $("body").on("click", ".xueke label", function () {
            $(".xueke label").removeClass("active");
            $(this).addClass("active");
            subTable.getGDSX();
            subTable.getGuDingShiWu();
            //subTable.getGradeSubjectTeaTable();
            subTable.getGradeSubTable();//oz
            subTable.getZhuanXiangZuHeXianQing(); 
            subTable.getCurrentJXZ();
        });
        
        $("body").on("click", ".grade label", function () {
            $(".grade label").removeClass("active");
            $(this).addClass("active");
            subTable.getSubjectList();
            subTable.getChushi();
            subTable.getGDSX();
            subTable.getGuDingShiWu();
            subTable.getGradeSubjectTeaTable();
            subTable.getZhuanXiangZuHeXianQing();
            subTable.getCurrentJXZ();
        });
    };
    
    
    
    
    
    subTable.getDesc = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".grade .active").attr("ids");
        par.type = 3;
        par.kbType = 5;
        if(ciId != null){
            Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
                desc = rep.message;
            });
        }
    };

    subTable.initDesc = function(){
        $('.itt').each(function (i,st) {
            if(($(this).text() == null || $(this).text().trim() == "") && !$(this).hasClass("gray")){
                $(this).text(desc);
            }
        });
    };

    
    subTable.getZhuanXiangZuHeXianQing=function(){
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $(".grade .active").attr("ids");
        par.subjectId = $(".xueke .active").attr("ids");
        $("#zhuanList").empty();
        if(par.subjectId!=""&&par.subjectId!=undefined) {
            Common.getData('/paike/getZhuanXiangZuHeXianQing.do', par, function (rep) {
                Common.render({tmpl: $('#Zhuan'), data: rep.message, context: '#zhuanList', overwrite: 1});
            });
        }
    };
    
    subTable.getGradeSubjectTeaTable = function () {
        subTable.getListBySchoolId();
        subTable.getChushi();
        subTable.getGDSX();
        subTable.getGuDingShiWu();
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $(".grade .active").attr("ids");
        par.subjectId = $(".xueke .active").attr("ids");
        par.week = $("#week").val();
        if(par.subjectId!=""&&par.subjectId!=undefined) {
            Common.getData('/paike/getGradeSubjectTeaTable.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    //$(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.title)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("rd");
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("background", "#FFF");
                    ciId = obj.cid;
                    var str = "<div>";
                    //var stt = "";
                    $.each(obj.tsName, function (d, dt) {
                        str += "<span>" + dt + "</span>";
                        //stt += dt + ",";
                    })
                    str += "</div>";
                    if (obj.tsName.length > 0) {
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(str);
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").append("<div class='title' hidden>" + str + "</div>")
                    }
                })
            })
        }
        subTable.getDesc();
    }

    subTable.getGDSX = function () {
        $("#fuckYou").append($(".fc-popup"))
        $(".itt").html("")
        $(".itt").css("background", "#FFF")
        $(".itt").removeClass("rd")
        var par = {};
        par.xqid = deXqid;
        par.gid = $(".grade .active").attr("ids");
        ;
        if (par.gid != "*") {
            Common.getData('/gdsx/getGDSXByXqid.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    var x = obj.y + 1;
                    var y = obj.x + 1;
                    $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc);
                    $(".itt[x=" + x + "][y=" + y + "]").css("background", "#FFB6C1");
                })
            })
        }
    }
    
    subTable.getGuDingShiWu = function () {
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                $(".itt[x=" + obj.x + "][y=" + obj.y + "]").css("background", "#FFB6C1")
            })
        })
    }
    
    subTable.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid": deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y=" + index + "]").addClass("gray");
                $("[y=" + index2 + "]").addClass("gray");
            }
        });
        subTable.initDesc();
    };
    
    subTable.getChushi = function () {
        var y = 1;
        var x = 1;
        $(".itt").each(function (s, ot) {
            $(this).attr("x", x);
            $(this).attr("y", y);
            y += 1;
            if (y == 8) {
                x += 1;
                y = 1;
            }
        });
    }
    
    subTable.getSubjectList = function () {
        var par = {};
        par.gradeId = $(".grade .active").attr("ids");
        par.xqid = deXqid;
        Common.getData('/new33isolateMange/getIsolateSubjectListByZKBGrade.do', par, function (rep) {
            /* $(".xueke").append(" <span>学科: </span>");*/
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.xuexue', overwrite: 1});
            $(".xueke label:eq(0)").addClass("active");
        });
    };
    
    subTable.getListBySchoolId = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        par.gradeId = $(".grade .active").attr("ids");
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdForKB.do', par, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    };
    
    subTable.getGrade = function () {
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            $(".grade").append(" <span>年级: </span>")
            //$(".gg").append("<label ids='*'>全部</label>")
            Common.render({tmpl: $('#grade1_temp'), data: rep, context: '.gg'});
            $(".grade label:eq(0)").addClass("active");
        });
    };
    
    subTable.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    
    subTable.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        });
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        });
    };
    
    subTable.getGrade2List = function () {
    	Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
			 Common.render({tmpl: $('#grade2Temp'), data: rep, context: '#grade2'});
       });
    };
    
    subTable.getSub2List = function () {
    	 var par = {};
    	 par.gradeId = $("#grade2").val();
    	 par.xqid = deXqid;
    	 Common.getData('/new33isolateMange/getIsolateSubjectListByZKBGrade.do', par, function (rep) {
    		 Common.render({tmpl: $('#subjectTemp'), data: rep.message, context: '#subjectList', overwrite: 1});
    });
    };
   
    //oz
    subTable.getGradeSubTable = function () {
        subTable.getListBySchoolId();
        subTable.getChushi();
        subTable.getGDSX();
        subTable.getGuDingShiWu();
        var par = {};
        par.xqid = deXqid;
        par.gradeId = $(".grade .active").attr("ids");
        par.subjectId = $(".xueke .active").attr("ids");
        par.week = $("#week").val();
        if(par.subjectId!=""&&par.subjectId!=undefined) {
            Common.getData('/paike/getGradeSubTable.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    //$(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.title)
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").addClass("rd");
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("background", "#FFF")
                    ciId = obj.cid;
                    var str = "<div>";
                    //var stt = "";
                    $.each(obj.tsName, function (d, dt) {
                        str += "<span>" + dt + "</span>";
                        //stt += dt + ",";
                    });
                    str += "</div>";
                    if (obj.tsName.length > 0) {
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(str);
                        $(".itt[x=" + obj.y + "][y=" + obj.x + "]").append("<div class='title' hidden>" + str + "</div>");
                    }
                });
            });
        }
        subTable.getDesc();
    };
    
    module.exports = subTable;
})