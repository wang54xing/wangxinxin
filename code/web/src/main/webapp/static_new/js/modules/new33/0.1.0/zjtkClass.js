define('zjtkClass', ['jquery', 'doT', 'common', 'Rome', 'layer'], function (require, exports, module) {
    var excCourse = {};
    require('jquery');
    require('doT');
    require('layer');
    Common = require('common');
    var deXqid = "";
    var xqid = "";
    var l3 = 1;
    var l2 = 1;
    var l1 = 1;
    var nb = 0;
    var currentWeek = 0;
    var weekArg = new Array("周一","周二","周三","周四","周五","周六","周日");
    excCourse.init = function() {
    	excCourse.getDefaultTerm();
        excCourse.getGradeList();
        excCourse.getListBySchoolId();
        //excCourse.getGradeWeekRangeByXqid();
        excCourse.getRoomEntryListByXqGrade();
        excCourse.getListByXq();
        excCourse.getCurrentJXZ();
        excCourse.getKeBiaoList();

        $('#queDing').click(function(){
            excCourse.setZkj();
        });

        $('#tksel').change(function() {
            if ($('#tksel').val()==1) {
                if ($('#edit').text()=='保存') {
                    $('#exchange').show();
                    $('#daike').hide();
                }
            } else {
                if ($('#edit').text()=='保存') {
                    $('#exchange').hide();
                    $('#daike').show();
                }
            }
        });
        $("#week").change(function(){
            excCourse.getKeBiaoList();
        })
        $('.gaozhong label').click(function () {
            $('.gaozhong label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            // $('#name').text($(this).text() + "年级总课表");
            excCourse.getListByXq();
            excCourse.getRoomEntryListByXqGrade();
            excCourse.getKeBiaoList();
        });
        $('.p5').click(function(){
            $('.fixedcont1').css('width',560);
            $('.zh-li').css({
                'height':'550px',
                'border':'1px solid #f77737',
                'border-top':0
            });
            $(this).hide();
            $('.cont1').show();
            $('#movebg1').show();
            $('.conEm').show();
            excCourse.getZjkjList();
        });
        $("body").on("click",".zhou label",function () {
            if ($(this).hasClass("all")) {
                $(this).addClass('active').siblings('label').removeClass('active');
            } else {
                $('.zhou .all').removeClass('active');
                if ($(this).hasClass("active")) {
                    $(this).removeClass('active');
                } else {
                    $(this).addClass('active');
                }
            }
            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            var wd = $('.tab2 th>div').width();
            if(jln =='all'){
                $('.zhou>div').show();
                $('.gao>div').show();
                var len = ($('.zhou label').length-1)*100;
                $('th>div').css('width',len+'px');
                l2 = 1
            }else{
                if(l2 == 1){
                    $('tr td div.'+jln).show().siblings('tr td div').hide();
                    $('tr th div.'+jln).show().siblings('tr th div').hide();
                    $('th>div').css('width', '100px');
                    l2 = l2 + 1
                }else{
                    if(cln == 'active'){
                        $('tr td div.'+jln).show();
                        $('tr th div.'+jln).show();
                        $('tr th:first-child').show();
                        $('th>div').css('width',wd+100);
                    }else{
                        $('tr td div.'+jln).hide();
                        $('tr th div.'+jln).hide();
                        $('tr th:first-child').show();
                        $('th>div').css('width',wd-100);
                    }
                }
            }
        });
        $(document).ready(function(){
            var len = ($('.zhou label').length-1)*100;
            $('th>div').css('width',len+'px');
        })
        $("body").on("click", ".jiaoshi label", function () {
            if ($(this).hasClass("all")) {
                $(this).addClass('active').siblings('label').removeClass('active');
            } else {
                $('.jiaoshi .all').removeClass('active');
                if ($(this).hasClass("active")) {
                    $(this).removeClass('active');
                } else {
                    $(this).addClass('active');
                }
            }

            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            if (jln == 'all') {
                $('tr td').show();
                $('tr th').show();
                l1 = 1;
            } else {
                if (l1 == 1) {
                    if (cln == 'active') {
                        $('tr td.' + jln).show().siblings('tr td').hide();
                        $('tr th.' + jln).show().siblings('tr th').hide();
                        l1 = l1 + 1
                    }
                } else {
                    if (cln == 'active') {
                        $('tr td.' + jln).show();
                        $('tr th.' + jln).show();
                    } else {
                        $('tr td.' + jln).hide();
                        $('tr th.' + jln).hide();
                    }
                }
            }
        });

        //编辑课表状态
        $('#edit').click(function(){
            var txt = $(this).text()
            if(txt == '编辑'){
                $(this).text('保存');
                if ($('#tksel').val()==1) {
                    $('#exchange').show();
                } else {
                    $('#daike').show();
                }
               
                $('.tabbg').hide();
            }else{
                $(this).text('编辑')
                $('#exchange').hide();
                $('#daike').hide();
                $('.tabbg').show();
                $('div').removeClass('active');
                $('div').removeClass('act');
                nb = 0
            }
        });
        $('.wind .btn-no,.wind .d1 em,.close').click(function(){
            $('.wind,.bg').fadeOut()
        })
        $('.btn2').click(function() {
            excCourse.setJkj();
        });
        $('.btn1').click(function() {
            var jxbId = "";
            $('.active2').each(function(i){
                jxbId = $(this).attr('jxbid');
            });
            if (jxbId!="null") {
                layer.alert("新增课节不能有课！");
                return;
            }
            excCourse.getJXBList();
        });

        //仅选中两个
        $('body').on('click','.tab2 td>div>div',function(){
            if($(this).hasClass('red')){
                $(".btn2").hide()
                $(".btn1").hide()
            }else {
                $('div').removeClass('act');
                var kid = $(this).find('div');
                $('.one').each(function(i) {
                    $(this).removeClass("one");
                });
                $('.active2').each(function(i) {
                    $(this).removeClass("active2");
                });
                kid.addClass('one');
                $(this).addClass('active2');
                if ($(this).attr('subid')!="null") {
                    $(".btn2").show()
                    $(".btn1").hide()
                } else {
                    $(".btn2").hide()
                    $(".btn1").show();
                }
                // if ($(this).attr('subid')!="null") {
                //     $('div').removeClass('act')
                //     var kid = $(this).find('div')
                //     if(nb<=2){
                //         if(kid.hasClass('active')){
                //             kid.removeClass('active');
                //             kid.removeClass('one');
                //             kid.removeClass('two');
                //             nb--
                //         }else{
                //             if(nb==0){
                //                 kid.addClass('one')
                //                 kid.addClass('active')
                //                 nb+=1
                //             }else if(nb==1){
                //                 if($('.one')){
                //                     kid.addClass('two')
                //                 }else{
                //                     kid.addClass('one')
                //                 }
                //                 kid.addClass('active')
                //                 nb+=1
                //             }
                //         }
                //     }
                // }
                
            }
        });
        //代课老师设置
        $('body').on('click','#daike',function(){
            if(nb==0){
                layer.msg('请选择一门课!');
                $('div').removeClass('act');
                return;
            } else if (nb==1) {
                if ($('.tk .active').attr('tk')==1) {
                    $('.d1').text("代课");
                    $('.tktime').hide();
                    $('.thwerk').hide();
                    $('.xkcls').hide();
                } else {
                    $('.d1').text("长期代课");
                    $('.tktime').show();
                    $('.thwerk').show();
                    $('.xkcls').show();
                }
                excCourse.getTeacherList();
                $('.dkcls').show();
                $('.wind-new-tea,.bg').fadeIn();
            } else {
                layer.msg('请选择一门课!')
                return;
            }
        });
        //交换
        $('body').on('click','#exchange',function(){
            if(nb==0){
                layer.msg('请先选择两个课!')
                $('div').removeClass('act');
                return;
            }else if(nb==1){
                layer.msg('请再选择一个课!');
                return;
            } else if(nb==2){
                // if ($('.one').parent().attr('jxbtp')==1 || $('.one').parent().attr('jxbtp')==2 || $('.two').parent().attr('jxbtp')==1 ||$('.two').parent().attr('jxbtp')==2) {
                //     layer.msg("走班课不允许调课！");
                //     return;
                // }
                if ($('.tk .active').attr('tk')==1) {
                    if ($('#week').val()<currentWeek) {
                        layer.alert("选择周不能小于当前周！");
                        return;
                    }
                    excCourse.exchangeJXBByZkb($('#week').val(),0);
                } else {
                    $('.d1').text("长期调课");
                    $('.tktime').show();
                    $('.dkcls').hide();
                    $('.thwerk').show();
                    $('.xkcls').show();
                    $('.wind-new-tea,.bg').fadeIn();
                }

            } else {
                layer.msg('最多选择两个个课!');
                return;
            }
        });
        $('body').on('click','.btn-ok',function(){
            if ($('.tk .active').attr('tk')==2) {
                if ($('#week2').val() < currentWeek || $('#week3').val() < currentWeek) {
                    layer.alert("选择周不能小于当前周！");
                    return;
                } else if ($('#week2').val() > $('#week3').val()) {
                    layer.alert("后一周不能小于前一周！");
                    return;
                }
            }
            if ($('#tksel').val()==1) {
                excCourse.exchangeJXBByZkb($('#week2').val(),$('#week3').val());
            } else {
                if ($('.tk .active').attr('tk')==1) {
                    excCourse.dkJXB($('#week').val(),0);
                } else {
                    excCourse.dkJXB($('#week2').val(),$('#week3').val());
                }
                
            }
        })

        //切换长短期调课
        $('.tag div span').click(function(){
            var txt = $(this).text();
            layer.msg('当前状态为'+txt);
            $(this).addClass('active').siblings('span').removeClass('active')
        })
        //选择学科
        $('.fixedcont .cont .lt span').click(function () {
            $(this).addClass('active').siblings('span').removeClass('active');
            $('.fixedcont .ul1').hide();
            $('.fixedcont .ul2').show();
        });
        $("body").on("click",".ke label",function(){
            if ($(this).hasClass("all")) {
                $(this).addClass('active').siblings('label').removeClass('active');
            } else {
                $('.ke .all').removeClass('active');
                if ($(this).hasClass("active")) {
                    $(this).removeClass('active');
                } else {
                    $(this).addClass('active');
                }
            }


            var ind = $(this).index();
            var jln = $(this).attr('js');
            var cln = $(this).attr('class');
            if(jln =='all'){
                $('.ke>div').show();
                l3 =1;
            }else{
                if(l3 ==1){
                    $('.ke>div.'+jln).show().siblings('.ke>div').hide();
                    l3 = l3 +1
                }else{
                    if(cln == 'active'){
                        $('.ke>div.'+jln).show();
                    }else{
                        $('.ke>div.'+jln).hide();
                    }
                }
            }
        });
    }

    excCourse.getTeacherList = function(){
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        var subjectId = $('.one').parent().attr('subid');
        Common.getData('/n33_fenbaninfoset/getTeacherList.do', {"gradeId":gradeId,"subjectId":subjectId}, function (rep) {
            Common.render({tmpl: $('#teacher_temp'), data: rep, context: '#dkTeacherId',overwrite:1});
        })
    }

    /*excCourse.getGradeWeekRangeByXqid = function() {
        if (deXqid && deXqid != null) {
            var par = {};
            var gradeId = "";
            $('.gaozhong label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gradeId = $(this).attr('ids');
                }
            })
            par.gid = gradeId;
            par.xqid = deXqid;
            Common.getData('/n33_gradeweekrange/getGradeWeekRangeByXqid.do', par, function (rep) {
                $(".zhou").empty();
                $(".zhou").append("<span>教学日 : </span><label class='active all' js='all' id='0'>全部</label>");
                if (rep!=null) {
                    for (var i=rep.start;i<=rep.end;i++) {
                        var name = weekArg[i-1];
                        $(".zhou").append("<label id='"+i+"' js='zh"+i+"'>"+name+"</label>");
                    }
                }
            });
        }
    }*/
    excCourse.getDefaultTerm = function(){
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
        	$("#defaultTerm").text(rep.message.xqnm);
        	xqid = rep.message.xqid;
        	deXqid = rep.message.paikeci;
        	$("#defaultTerm").attr("ids",rep.message.paikeci);
        });
    }

    excCourse.dkJXB = function(week,week2) {
        var data = {};
        data.ykbId = $('.one').parent().attr('ykbid');
        data.teacherId = $('#dkTeacherId').val();
        data.sweek = week;
        data.eweek = week2;
        data.week = $('#week').val();
        Common.getData('/paike/dkJXB.do', data, function (rep) {
            if (rep.code == '200') {
                if (rep.message=="代课成功！") {
                    $('.wind-new-tea').hide();
                    $('.bg').hide();
                    layer.msg(rep.message);
                    excCourse.getKeBiaoList();
                    $('#edit').text('编辑');
                    $('#exchange').hide();
                    $('#daike').hide();
                    $('.tabbg').show();
                    $('div').removeClass('active');
                    $('div').removeClass('act');
                    nb = 0
                } else {
                    layer.alert(rep.message);
                }
            } else {
                layer.alert(rep.message);
            }

        });
    }
    
    excCourse.exchangeJXBByZkb = function(week,week2) {
        var data = {};
        data.ykbId = $('.one').parent().attr('ykbid');
        data.orgYkbId = $('.two').parent().attr('ykbid');
        if ($('.one').parent().attr('crmid')!=$('.two').parent().attr('crmid')) {
            layer.alert("教学班设置固定教室不同，不允许调课！");
            return;
        }
        data.sweek = week;
        data.eweek = week2;
        data.week = $('#week').val();
        Common.getData('/paike/exchangeJXBByZkb.do', data, function (rep) {
            if (rep.code == '200') {
                if (rep.message=="交换成功！") {
                    $('.wind-new-tea').hide();
                    $('.bg').hide();
                    excCourse.getKeBiaoList();
                    layer.msg(rep.message);
                    $('#edit').text('编辑');
                    $('#exchange').hide();
                    $('#daike').hide();
                    $('.tabbg').show();
                    $('div').removeClass('active');
                    $('div').removeClass('act');
                    nb = 0
                } else {
                    layer.alert(rep.message);
                }


            } else {
                layer.alert(rep.message);
            }
        });
    }
    excCourse.getListByXq=function(){
        Common.getData('/n33_jxz/getTermByXq.do', {}, function (rep) {
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week',overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week2',overwrite: 1});
            Common.render({tmpl: $('#week_temp'), data: rep, context: '#week3',overwrite: 1});
        });
    }
    excCourse.getCurrentJXZ=function(){
        Common.getData('/n33_jxz/getCurrentJXZ.do', {}, function (rep) {
            if (rep!=null && rep.serial!=null) {
                currentWeek = rep.serial;
                $('#week').val(rep.serial);
                $('#week2').val(rep.serial);
                $('#week3').val(rep.serial);
            }
            
        });
    }
    excCourse.getGradeList = function () {
        if(xqid != null && xqid != undefined  && xqid != ""){
            Common.getData('/new33isolateMange/getGradeListByXqidList.do', {"xqid": xqid}, function (rep) {
                $(".gaozhong").append(" <span>年级 : </span>")
                Common.render({tmpl: $('#grade_temp'), data: rep, context: '.gaozhong'});
                // $(".gaozhong label:eq(0)").addClass("active");
                excCourse.initGrade();
            })
        }
    }
    excCourse.getListBySchoolId = function () {
        if (xqid != undefined && xqid != null && xqid != "") {
            Common.getData('/courseset/getListBySchoolIdZKB.do', {"xqid": xqid}, function (rep) {
                $(".ul-main .ke").append(" <span>课节 : </span>")
                $(".ul-main .ke").append(" <label class='active all' js='all'>全部</label>")
                Common.render({tmpl: $('#KeShiList'), data: rep, context: '.ul-main .ke'});
            });
        }
    }

    excCourse.getRoomEntryListByXqGrade = function () {
        if (xqid != null && xqid != undefined && xqid != "") {
            var par = {};
            par.xqid = xqid;
            var gradeId = "";
            $('.gaozhong label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gradeId = $(this).attr('ids');
                }
            })
            par.gradeId = gradeId;
            if(par.xqid != "" && par.xqid != undefined && par.xqid != null && par.gradeId != "" && par.gradeId != undefined && par.gradeId != null){
                Common.getData('/paike/getRoomEntryListByXqGradeAndTerm.do', par, function (rep) {
                    $(".jiaoshi").empty();
                    $(".jiaoshi").append(" <span>教室 : </span>")
                    $(".jiaoshi").append(" <label class='active all' js='all'>全部</label>")
                    Common.render({tmpl: $('#jiaos'), data: rep.message, context: '.jiaoshi'});
                });
            }
        }
    }
    excCourse.getZjkjList = function() {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.xqId = xqid;
        data.ciId = deXqid;
        data.gradeId = gradeId;
        Common.getData('/n33zjkj/getZjkjList.do', data, function (rep) {
            if (rep.code=='200') {
                $('.zjkj2').empty();
                Common.render({tmpl: $('#zjkj_temp'), data: rep.message, context: '.zjkj2'});
            } else {
                layer.alert("查询失败！");
            }
        });
    }

    excCourse.getJXBList = function() {
        var data = {};
        var zkbId = "";
        $('.active2').each(function(i){
            zkbId = $(this).attr('ykbid');
        });
        data.ciId = deXqid;
        data.zkbId = zkbId;
        Common.getData('/n33zjkj/getJXBList.do', data, function (rep) {
            if (rep.code == '200') {
                $('.sel20').empty();
                Common.render({tmpl: $('#sel20_temp'), data: rep.message, context: '.sel20'});
                $(".bg").show();
                $(".dbtk-popup").show();
            } else {
                layer.alert("查询失败！");
            }
        });
    }
    excCourse.setZkj = function() {
        var zkbId = "";
        var jxbid = "";
        $('.active2').each(function(i){
            zkbId = $(this).attr('ykbid');
            jxbid = $(this).attr('jxbid');
        });
        var data = {};
        data.zkbId = zkbId;
        data.jxbId = $('.sel20').val();
        Common.getData('/n33zjkj/setZkj.do', data, function (rep) {
            if (rep.code == '200') {
                layer.alert("增加课节成功！");
                $(".bg").hide();
                $(".dbtk-popup").hide();
                excCourse.getKeBiaoList();
            } else {
                layer.alert("增加课节失败！");
            }
        });
    }
    excCourse.setJkj = function() {
        var zkbId = "";
        var jxbid = "";
        $('.active2').each(function(i){
            zkbId = $(this).attr('ykbid');
            jxbid = $(this).attr('jxbid');
        });
        if (jxbid==null) {
            layer.alert("删除课节不能为空！");
            return;
        }
        var data = {};
        data.zkbId = zkbId;
        Common.getData('/n33zjkj/setJkj.do', data, function (rep) {
            if (rep.code == '200') {
                layer.alert("移除课节成功！");
                excCourse.getKeBiaoList();
            } else {
                layer.alert("移除课节失败！");
            }
        });
    }
    excCourse.getKeBiaoList = function () {
        var data = {};
        var gradeId = "";
        $('.gaozhong label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        data.gradeId = gradeId;
        data.classRoomIds = '';
        data.weeks = '';
        data.indexs = '';
        data.week=$("#week").val();
        Common.getData('/paike/getKeBiaoListZhou.do', data, function (rep) {
            if (rep.code == '200') {
                if (rep.message!=null && rep.message.ykbdto!=null && rep.message.ykbdto.length!=0) {
                    $('.tkkb').show();
                    $('.tag').show();
                    $('.tile').show();
                    $('.wtkkb').hide();
                    $('.kejie').empty();
                    $('.clroms').empty();
                    $('.weeks').empty();
                    $(".jxbkj").empty()
                    Common.render({tmpl: $('#ks_temp'), data: rep.message.courseRangeDTOs, context: '.kejie'});
                    Common.render({tmpl: $('#clroms_temp'), data: rep.message.classrooms, context: '.clroms'});
                    Common.render({tmpl: $('#weeks_temp'), data: rep.message.classrooms, context: '.weeks'});
                    $(".clroms th").each(function(i,obj) {
                        var array = new Array();
                        $.each(rep.message.ykbdto, function (index, item) {
                            if ($(obj).attr("class") == item[0].classroomId) {
                                array.push(item);
                            }
                        });
                        if (array!=null &&array.length!=0) {
                            Common.render({tmpl: $('#jxbkj_temp'), data: array, context: '.jxbkj'});
                        }
                    });
                    if (!rep.message.saflg) {
                        $('.week5').each(function() {
                            $(this).addClass("gray");
                        });
                    }
                    if (!rep.message.suflg) {
                        $('.week6').each(function() {
                            $(this).addClass("gray");
                        });
                    }
                    var len = ($('.zhou label').length-1)*100;
                    $('th>div').css('width',len+'px');
                    nb = 0;
                } else {
                    $('.wtkkb').show();
                    // $('.tile').hide();
                    $('.tag').hide();
                    $('.tkkb').hide();
                }

            } else {

            }
        })
    }
    excCourse.initGrade = function () {
        try {
            Common.getData('/udc/data.do', {"pageTag":"n33"}, function (res) {
                if (res.message.length > 0) {
                    $.each(res.message, function (i, obj) {
                        if (obj.key == "gradeId") {
                            gradeId = obj.value;
                        }
                    })

                    $(".gaozhong label").each(function () {
                        if (gradeId == $(this).attr("ids")) {
                            $(this).addClass("active");
                        }
                    });
                } else {

                }
            });
        } catch (x) {
            console.log(x);
        }
    }
    module.exports = excCourse;
});