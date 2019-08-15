/**
 * Created by albin on 2018/3/23.
 */
define('pStuClassTime', ['jquery', 'doT', 'common', 'Rome', 'pagination', 'layer'], function (require, exports, module) {
    var pStuClassTime = {};
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
    pStuClassTime.getDesc = function () {
        var par = {};
        par.ciId = ciId;
        par.gradeId = $(".xueke .active").attr("ids");
        par.type = 3;
        par.kbType = 3;
        Common.getData('/n33_set/getDESCForNoCourse.do', par, function (rep) {
            desc = rep.message;
        });
    }

    pStuClassTime.initDesc = function(){
        $('.itt').each(function (i,st) {
            if(($(this).text() == null || $(this).text().trim() == "") && !$(this).hasClass("grayhui")){
                $(this).text(desc);
            }
        });
    }
    pStuClassTime.getCurrentJXZ = function () {
        Common.getData('/n33_jxz/getGradeWeek.do', {"xqid":deXqid}, function (rep) {
            if (rep != null && rep.serial != null) {
                var index = rep.serial + 1;
                var index2 = rep.serial + 2;
                $("[y="+index+"]").addClass("gray");
                $("[y="+index2+"]").addClass("gray");
            }
        });
        pStuClassTime.initDesc();
    }
    pStuClassTime.getGDSX = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $(".xueke .active").attr("ids");
        Common.getData('/gdsx/getGDSXByXqid.do', par, function (rep) {
            $.each(rep.message, function (i, obj) {
                var x=obj.y + 1;
                var y=obj.x + 1;
                $(".itt[x=" + x + "][y=" + y + "]").html(obj.desc)
                $(".itt[x=" + x + "][y=" + y + "]").css("background", "#FFB6C1")
            })
        })
    }
    pStuClassTime.GetTeachersSettledPositions = function () {
        pStuClassTime.getListBySchoolId();
        pStuClassTime.getChushi();
        pStuClassTime.getGuDingShiWu()
        pStuClassTime.getGDSX();
        $(".itt").removeClass("swRed");
        $(".itt").removeClass("shi");
        var par = {};
        par.studentId = $(".t-li").attr("ids");
        par.week = $("#week").val();
        par.gid = $(".xueke .active").attr("ids");
        if(par.studentId!=""&&par.studentId!=undefined) {
            Common.getData('/paike/GetStudentSettledPositionsByWeek.do', par, function (rep) {
                $.each(rep.message, function (i, obj) {
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").css("background", "#FFF")
                    $(".itt[x=" + obj.y + "][y=" + obj.x + "]").html(obj.JxbName + "</br>" + obj.teaName + "</br>" + obj.subName + "</br>" + obj.roomName)
                    ciId = obj.cid;
                })
            })
        }
        pStuClassTime.getDesc();
    }
    pStuClassTime.init = function () {
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
            $('#printGDG').show()
            return false;
        })

        //批量导出学生课表
        $('body').on("click",".b10",function () {
        	pStuClassTime.getGrade2();
            //pStuClassTime.getClassListForExport();
            pStuClassTime.getStudentListForExport();
            $('.dc-popup').show();
            $('.bg').show();
        });

        $("body").on("change", "#grade2", function () {
        	pStuClassTime.getClassListForExport();
            pStuClassTime.getStudentListForExport();
        });
        
        $("body").on("change", "#classList", function () {
            pStuClassTime.getStudentListForExport();
        });

        $('body').on("click",".selectAll",function () {
            var flag = false;
            $('.dcStu').each(function (i,st) {
                if(!$(this).is(":checked")){
                    flag = true;
                }
            });
            if(flag){
                $('.dcStu').each(function (i,st) {
                    $(this).prop("checked",true);
                });
            }else{
                $('.dcStu').each(function (i,st) {
                    $(this).prop("checked",false);
                });
            }
        });

        $("body").on("click", ".xueke label", function () {
            $(".xueke label").removeClass("active");
            $(this).addClass("active");
            $("#usPk").html("")
            $("#userName").html("")
            $(".itt").empty();
            pStuClassTime.getClassList();
            pStuClassTime.getStudentList();
            pStuClassTime.GetTeachersSettledPositions();
            pStuClassTime.getGuDingShiWu()
            pStuClassTime.getGDSX();
            pStuClassTime.getCurrentJXZ();
        })

        $("body").on("click", ".b9", function () {
            var par = {};
            par.studentId = $(".t-li").attr("ids");
            par.week = $("#week").val();
            par.gid = $(".xueke .active").attr("ids");
            par.userName = $(".t-li").find("span").text();
            par.className = $('.cls label[class="active"]').attr('gName') + '(' +$('.cls label[class="active"]').attr('cName') + ')' + '班';
            
            var flag = isCanExport();
            if(!flag){
                layer.alert("未发布该周课表，不允许导出课表");
                return;
            }
            window.location.href = "/paike/exportStuKB.do?studentId=" + par.studentId + "&week=" + par.week + "&gid=" + par.gid 
            						+ "&userName=" + par.userName + '&className=' +par.className;
        });

        $("body").on("click", ".cls label", function () {
            $(".cls label").removeClass("active");
            $(this).addClass("active");
            $("#usPk").html("")
            $("#userName").html("")
            $(".itt").empty();
            pStuClassTime.getStudentList();
            pStuClassTime.GetTeachersSettledPositions();
            pStuClassTime.getGuDingShiWu()
            pStuClassTime.getGDSX();
            pStuClassTime.getCurrentJXZ();
        })
        $("#week").change(function () {
            pStuClassTime.getStudentList();
            pStuClassTime.GetTeachersSettledPositions();
            pStuClassTime.getGuDingShiWu()
            pStuClassTime.getGDSX();
            pStuClassTime.getCurrentJXZ();
        })
        $("body").on("click", "#use li", function () {
            $("#use li").removeClass("t-li");
            $(this).addClass("t-li");
            pStuClassTime.getPks();
            $(".itt").html("")
            pStuClassTime.GetTeachersSettledPositions();
            pStuClassTime.getGuDingShiWu()
            pStuClassTime.getGDSX();
            pStuClassTime.getCurrentJXZ();
        })
        
         //批量导出学生课表
         $('body').on("click",".dcAll",function () {
        	 var param = {};
        	 var stuIds = '';
        	 $('#studentList label').each(function(i){
        		 if($(this).children('input').prop('checked') == true)
        		 {
        			 stuIds = stuIds + ',' + $(this).children('input').attr('ids');
        		 }
        	 });
        	 if(stuIds == ''){
        		 layer.msg('请先选择学生');
        	 }else{
        		 param.stuIds = stuIds;
        		 param.week = $('#week').val();
        		 param.gid = $(".xueke .active").attr("ids");
        		 Common.getPostBodyData('/paike/expStusKB.do',param, function (rep) {
        			 window.location.href = '/paike/exportStusKB.do';
        	     });
        	 }
        });

        pStuClassTime.getDefaultTerm();
        pStuClassTime.getListByXq();
        pStuClassTime.getGrade();
        pStuClassTime.getListBySchoolId();
        pStuClassTime.getChushi();
        pStuClassTime.getStudentList();
        pStuClassTime.GetTeachersSettledPositions();
        pStuClassTime.getGuDingShiWu()
        pStuClassTime.getGDSX();
        pStuClassTime.getCurrentJXZ();
    }

    function isCanExport() {
        var flag = null;
        var par = {};
        par.week = $("#week").val();
        par.gradeId = $(".xueke .active").attr("ids");
        par.xqid = deXqid;
        Common.getData('/paike/isCanExport.do', par, function (rep) {
            flag = rep.message;
        });
        return flag;
    }

    pStuClassTime.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.xqnm);
            deXqid = rep.message.xqid;
            deCid = rep.message.paikeci;
            $("#defaultTerm").attr("ids", rep.message.xqid);
        });
    }
    pStuClassTime.getGrade = function () {
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            $(".xueke").append(" <span>年级: </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep, context: '.xueke'});
            //Common.render({tmpl: $('#gradeList'), data: rep, context: '#grade2'});
            $(".xueke label:eq(0)").addClass("active");
            pStuClassTime.getClassList();
        })
    }

    pStuClassTime.getClassList = function () {
        var par = {};
        par.xqid = deXqid;
        par.gid = $(".xueke .active").attr("ids");
        $(".cls").empty();
        $(".cls").append(" <span>班级: </span>")
        Common.getData('/new33classManage/getClassListZKB.do', par, function (rep) {
            Common.render({tmpl: $('#class_temp'), data: rep.message, context: '.cls'});
            $(".cls label:eq(0)").addClass("active");
        })
    }

    //获取年级
    pStuClassTime.getGrade2 = function () {
    	$('#grade2').html('');
        Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#gradeList'), data: rep, context: '#grade2'});
            pStuClassTime.getClassListForExport();
        });
    };
    
    pStuClassTime.getClassListForExport = function(){
    	$('#classList').html('');
        var par = {};
        par.xqid = deXqid;
        par.gid = $("#grade2").val();
        Common.getData('/new33classManage/getClassListZKB.do', par, function (rep) {
            var classList = new Array();
            var map = {};
            map.classId = "*";
            map.gname = "全部";
            map.xh = "";
            classList.push(map);
            $.each(rep.message, function (i, obj) {
                classList.push(obj);
            });
            Common.render({tmpl: $('#class'), data: classList, context: '#classList',overwrite : 1});
        });
    };

    pStuClassTime.getListByXq = function () {
        var wk = 1;
        Common.getData('/new33isolateMange/getDefWeek.do', "", function (rep) {
            wk = rep.message;
        })
        Common.getData('/n33_jxz/getListByXq.do', {"xqid": deXqid}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week', overwrite: 1});
            $("#week option[value=" + wk + "]").prop("selected", true)
        })
    }
    pStuClassTime.getChushi = function () {
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
        })
    }

    pStuClassTime.getGuDingShiWu = function () {
        $(".itt").css("background", "#FFF")
        //$(".itt").html("");
        Common.getData('/new33isolateMange/getGuDingShiWuByXqid.do', {"xqid": deXqid}, function (rep) {
            $.each(rep.message, function (i, obj) {
                var string = "";
                if ($(".itt[x=" + obj.x + "][y=" + obj.y + "]").text() == "" && !$(".itt[x=" + obj.x + "][y=" + obj.y + "]").hasClass("swRed")) {
                    if(obj.sk==0) {
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html(obj.desc)
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass("swRed");
                    }else{
                        $(".itt[x=" + obj.x + "][y=" + obj.y + "]").html("自习课")
                    }
                } else {
                    if ($(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title") != undefined) {
                        if(obj.sk==0){
                            string = $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title");
                            string += "\r\n";
                            string += obj.desc;
                        }else{
                            string = $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title");
                            string += "\r\n";
                            string += "自习课";
                        }
                    } else {
                        if(obj.sk==0){
                            $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title", obj.desc);
                            $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass('shi');
                        }else{
                            $(".itt[x=" + obj.x + "][y=" + obj.y + "]").attr("title","自习课");
                            $(".itt[x=" + obj.x + "][y=" + obj.y + "]").addClass('shi');
                        }

                    }
                }
            })
        })
    }
    pStuClassTime.getListBySchoolId = function () {
        var par = {};
        par.xqid = deXqid;
        par.week = $("#week").val();
        par.gradeId = $(".xueke .active").attr("ids");
        if (deXqid && deXqid != null) {
            Common.getData('/courseset/getListBySchoolIdForKB.do', par, function (rep) {
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '#tbd', overwrite: 1});
            });
        }
    }

    pStuClassTime.getStudentList = function () {
        var par = {};
        par.xqid = deXqid;
        par.classId = $(".cls .active").attr("ids");
        Common.getData('/IsolateStudent/getStudentDtoByClassIdZKB.do', par, function (rep) {
            $("#userCount").html(rep.message.length)
            Common.render({tmpl: $('#userList'), data: rep.message, context: '#use', overwrite: 1});
            Common.render({tmpl: $('#student'), data: rep.message, context: '#studentList', overwrite: 1});
            $("#use li:eq(0)").addClass("t-li");
            pStuClassTime.getPks();
        })
    }

    pStuClassTime.getStudentListForExport = function () {
    	$('#studentList').html('');
        var par = {};
        par.gid = $('#grade2').val();
        par.xqid = deXqid;
        par.classId = $("#classList").val();
        Common.getData('/IsolateStudent/getStudentDtoByClassIdZKBForExport.do', par, function (rep) {
            Common.render({tmpl: $('#student'), data: rep.message, context: '#studentList', overwrite: 1});
        });
    };

    pStuClassTime.getPks = function () {
        $("#userName").html($(".t-li").attr("nm"))
    }
    module.exports = pStuClassTime;
})