/**
 * Created by albin on 2018/3/19.
 */
define('zixiclass', ['jquery', 'doT', 'common', 'Rome', 'pagination','fselect', 'layer'], function (require, exports, module) {
    var zixiclass = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('fselect');
    Common = require('common');
    var xqid = ""
    var content = new Array();
    var htitle = new Array();
    var vtitle = new Array();
    var gradeId = "";
    var table ={};
    zixiclass.getDefaultTerm = function () {
        Common.getData('/n33_set/getDefaultTerm.do', {}, function (rep) {
            $("#defaultTerm").text(rep.message.paikeciname);
            xqid = rep.message.paikeci;
            zhenXqid=rep.message.xqid;
            $("#defaultTerm").attr("ids", rep.message.paikeci);
        });
    }

    zixiclass.init = function () {

        $('#ms1').change(fSelectAction);
        $('#ms2').change(fSelectAction);
        $('#ms3').change(fSelectAction);

        $(".a3").addClass("active");
        zixiclass.getDefaultTerm();
        zixiclass.getGradeList();
        initGrade();
        $(".tt-left li>span").click(function(){
            $(this).addClass('t-li').parent().siblings().find('span').removeClass('t-li');
        })
        $('.tt-left li i').click(function(){
            if($(this).hasClass('rr-t')){
                $(this).removeClass('rr-t').addClass('rr-d');
                $(this).parent().siblings('ul').show();
            }else{
                $(this).removeClass('rr-d').addClass('rr-t')
                $(this).parent().siblings('ul').hide();
            }
        })
        $('.xueke label').click(function(){
            $(this).addClass('active').siblings('label').removeClass('active')
        })
        $('.xueke label').click(function(){
            $(this).toggleClass('avtive');
        })
        // 排本节自习课
        $('.pk').click(function(){
            var flag = getCiIdIsFaBu();
            if(flag){
                layer.alert("课表已发布,不允许排自习");
                return;
            }
            $('#all_select').prop("checked",false);
            var x = $("#content em").filter(".zuh-cur").attr("x");
            var y = $("#content em").filter(".zuh-cur").attr("y");
            if(!x||!y){
                layer.alert("请选择一个课节");
                return;
            }
            $('.main1').hide();
            $('.main2').show();
        })

        $('body').on('click','#addStudentsToZiXi',function(){
            $('.main3').hide();
        })
        $('.prev').click(function(){
            $('.main1').show();
            $('.main2').hide();
            $('.main3').hide();
        })
        //
        // $('.prevv').click(function(){
        //     $('.main2').show();
        //     $('.main3').hide();
        // })

        $('.tjz').click(function(){
            $('.bg').show();
            $('.xz-popup').show();
        })
        $('.cj').click(function(){
            // zixiclass.getCreateDialog();
            $("#zixiname").val("");
            var x = $("#content em").filter(".zuh-cur").attr("x");
            var y = $("#content em").filter(".zuh-cur").attr("y");
            // $("#content em").each(function () {
            //     if($(this).attr("x")==x&&$(this).attr("y")==y){
            //         $(this).click();
            //     }
            // });
            zixiclass.getRoomByXY();
            zixiclass.getTeachersByXY();
            zixiclass.getStudents();
            zixiclass.getZiXiBanByXY();
            $('.bg').show();
            $('.cj-popup').show();
        })
        $('.close,.qx').click(function(){
            $('.bg').hide();
            $('.xz-popup').hide();
            $('.cj-popup').hide();
            $('.zd-popup').hide();
        })
        $("i[class='fr']").click(function(){
            $('.bg').hide();
            $('.xz-popup').hide();
            
        })
        // 年级切换
        $('.xueke label').click(function () {
            $('.xueke label').each(function (i) {
                $(this).removeClass('active');
            })
            $(this).addClass('active');
            zixiclass.getTable();
        });
        // $('.xueke label').eq(0).click();


        $('body').on('click', '.tab2 div>p:nth-child(2)', function () {
            var jsn = $(this).attr('js');
            var cln = $(this).attr('class');
            if ($(this).hasClass('active')) {
                $(this).removeClass('active')
                $('.tab2 div.' + jsn).removeClass('active')
            } else {
                $(this).addClass('active')
                $('.tab2 div.' + jsn).addClass('active')
            }
            return false;
        })
        $("body").on("click","#content .zbz",function () {
            $(this).toggleClass('zuh-cur').parent().siblings('td').find('em').removeClass('zuh-cur');
            $(this).parent().parent().siblings('tr').find('em').removeClass('zuh-cur');
            // $(this).toggleClass('zuh-cur').parent().parent().siblings('tr').find('em').removeClass('zuh-cur');
            if($(this).hasClass('zuh-cur')){
                var x = $("#content em").filter(".zuh-cur").attr("x");
                var y = $("#content em").filter(".zuh-cur").attr("y");
                $("#weekday").val(x);
                $("#course").val(y);
                zixiclass.getRoomByXY();
                zixiclass.getTeachersByXY();
                zixiclass.getStudents();
                zixiclass.getZiXiBanByXY();
            }
        })
        $('.zd-btn').click(function(){
            $('.main1').hide();
            $('.main4').show();
        });
        $('.prev4').click(function(){
            $('.main1').show();
            $('.main4').hide();
        });
        $('#ckzxk').click(function(){
            zixiclass.getZiXiBan();
            $('.main1').hide();
            $('.main3').show();
        })
        $("#weekday").change(function () {
            zixiclass.getRoomByXY();
            zixiclass.getTeachersByXY();
        });
        $("#course").change(function () {
            zixiclass.getRoomByXY();
            zixiclass.getTeachersByXY();
        });
        $("#createZiXiBan").click(function () {
            zixiclass.createZiXiBan();
        });
        $("#addStudentsToZiXi").click(function () {
            zixiclass.addStudentsToZiXi();
        });
        $("body").on("click","#zixiban .del",function () {
            var flag = getCiIdIsFaBu();
            if(flag){
                layer.alert("课表已发布,不允许删除");
                return;
            }
            zixiclass.removeZiXiBan($(this).attr("cid"));
        });
        $('body').on('click','#zixiban .tab-edit',function(){
            var flag = getCiIdIsFaBu();
            if(flag){
                layer.alert("课表已发布,不允许修改");
                return;
            }
            if($(this).parent().siblings('td').find('.zhb2').is(':hidden')){
               $(this).html("保存")
                zixiclass.getRoomByXY2({"roomid":$(this).attr("roomid"),"roomname":$(this).attr("roomname")},$(this).attr("x"),$(this).attr("y"));
                zixiclass.getTeachersByXY2({"id":$(this).attr("teaid"),"name":$(this).attr("teaname")},$(this).attr("x"),$(this).attr("y"));
                $(this).parent().parent().find('.classroom').val($(this).attr("roomid"));
                $(this).parent().siblings().find('.teacher').val($(this).attr("teaid"));
                $(this).parent().siblings('td').find('.zhb1').toggle();
                $(this).parent().siblings('td').find('.zhb2').toggle();

            }
            else{
                $(this).html("编辑");
                var flag = zixiclass.updateRoomTeacher($(this).attr("cid"),$(this).parent().parent().find('.classroom').val(),$(this).parent().parent().find('.teacher').val());
                if(flag!=false){
                    $(this).parent().siblings('td').find('.zhb1').toggle();
                    $(this).parent().siblings('td').find('.zhb2').toggle();
                    zixiclass.getZiXiBanByXY();
                }
            }


        })


        $("body").on("click", "#zixiban2 .del", function () {
            var id = $(this).attr("cid");
            layer.confirm('确认删除？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                Common.getData('/n33_zixi/removeZiXiBan.do', {"ciId":xqid,"id":id}, function (rep) {
                    if(rep.code==200){
                        layer.msg("删除成功");
                        zixiclass.getZiXiBan();
                    }
                })
            },function () {
            });
        });

       /* $("body").on("click","#zixiban2 .del",function () {
            zixiclass.removeZiXiBan2($(this).attr("cid"));
        });*/
        $('body').on('click','#zixiban2 .tab-edit',function(){

            if($(this).parent().siblings('td').find('.zhb2').is(':hidden')){
                $(this).html("保存")
                zixiclass.getRoomByXY2({"roomid":$(this).attr("roomid"),"roomname":$(this).attr("roomname")},$(this).attr("x"),$(this).attr("y"));
                zixiclass.getTeachersByXY2({"id":$(this).attr("teaid"),"name":$(this).attr("teaname")},$(this).attr("x"),$(this).attr("y"));
                $(this).parent().parent().find('.classroom').val($(this).attr("roomid"));
                $(this).parent().siblings().find('.teacher').val($(this).attr("teaid"));
                $(this).parent().siblings('td').find('.zhb1').toggle();
                $(this).parent().siblings('td').find('.zhb2').toggle();

            }
            else{
                $(this).html("编辑");
                var flag = zixiclass.updateRoomTeacher($(this).attr("cid"),$(this).parent().parent().find('.classroom').val(),$(this).parent().parent().find('.teacher').val());
                if(flag!=false){
                    $(this).parent().siblings('td').find('.zhb1').toggle();
                    $(this).parent().siblings('td').find('.zhb2').toggle();
                    zixiclass.getZiXiBan();
                }
            }
        })
        $("#exportZiXiBan").click(function () {
            var gid = "";
            $('.xueke label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gid = $(this).attr('ids');
                }
            });
            if(!gid||gid==""){
                layer.alert("请选择年级");
                return;
            }
            zixiclass.exportZiXiBan();
        });
        $("#all_select").click(function () {
            if($(this).is(":checked")){
                ckShow(true);
            }
            else{
                ckShow(false);
            }
        });
        
        function ckShow(e){//判断是否在筛选状态 如果被隐藏则不选中或不取消选中
            $.each($("#students2 input"),function () {
                var display=$(this).parent().parent().parent().css('display');
                if(display!='none'){
                    $(this).prop("checked",e);
                }
            })
        }
        
        $("#autoArranage").click(function () {
            var flag = getCiIdIsFaBu();
            if(flag){
                layer.alert("课表已发布,不允许排自习");
                return;
            }
            $('.bg').show();
            $('.zd-popup').show();
            // autoArranage();
        });
        $("#autoArrangeByJXB").click(function () {
            var cap = $("#capacity").val();
            if(cap==null||cap==""){
                layer.alert("请填写正确的容量！");
                return;
            }
            $('.zd-popup').hide();
            setTimeout(autoArranage,100,cap,1);

        });
        $("#autoArrangeByXZB").click(function () {
            var cap = $("#capacity").val();
            if(cap==null||cap==""){
                layer.alert("请填写正确的容量！");
                return;
            }
            $('.zd-popup').hide();
            setTimeout(autoArranage,100,cap,2);
        });
    }


    zixiclass.getGradeList = function () {
        Common.getData('/new33isolateMange/getGradList.do', {"xqid": xqid}, function (rep) {
            $(".xueke").append(" <span>年级 : </span>")
            Common.render({tmpl: $('#grade_temp'), data: rep.message, context: '.xueke'});
            // $(".xueke label:eq(0)").addClass("active");
        })
    }

    zixiclass.getTable = function () {
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        Common.getData('/n33_zixi/getTable.do', {"ciId": xqid,"gradeId":gradeId}, function (rep) {
            Common.render({tmpl: $('#vtitle_temp'), data: rep.message.vtitle, context: '#vtitle',overwrite:1});
            Common.render({tmpl: $('#htitle_temp'), data: rep.message.htitle, context: '#htitle',overwrite:1});
            Common.render({tmpl: $('#content_temp'), data: rep.message.content, context: '#content',overwrite:1});
            Common.render({tmpl: $('#weekday_temp'), data: rep.message.htitle, context: '#weekday',overwrite:1});
            Common.render({tmpl: $('#course_temp'), data: rep.message.vtitle, context: '#course',overwrite:1});
            $("#gradenum").html("年级人数:"+rep.message.gradenum+"人");
            content = rep.message.content;
            htitle = rep.message.htitle;
            vtitle = rep.message.vtitle;
            table = rep.message;
        })
    }
    zixiclass.getCreateDialog = function () {
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        Common.getData('/n33_zixi/getTable.do', {"ciId": xqid,"gradeId":gradeId}, function (rep) {
            Common.render({tmpl: $('#weekday_temp'), data: rep.message.htitle, context: '#weekday',overwrite:1});
            Common.render({tmpl: $('#course_temp'), data: rep.message.vtitle, context: '#course',overwrite:1});
        })
    }
    zixiclass.getStudents = function () {
        var ids = [];
        $("#content em").each(function () {
            if($(this).hasClass("zuh-cur")&&$(this).attr("y")&&$(this).attr("x")){
                ids = ids.concat(content[$(this).attr("y")][$(this).attr("x")].notinlist);
            }
        })
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        });
        var arr = ["周一","周二","周三","周四","周五","周六","周日"];

        if($("#content em").filter(".zuh-cur").attr("x")){
            var x = $("#content em").filter(".zuh-cur").attr("x");
            var y = $("#content em").filter(".zuh-cur").attr("y");
            $("#kejie").html(arr[x]+" 第"+(Number(y)+1)+"节："+ids.length+"人");
            $("#kejie2").html(arr[x]+" 第"+(Number(y)+1)+"节："+ids.length+"人");
            Common.getPostBodyData('/n33_zixi/getStudents.do?ciId='+xqid+"&gradeId="+gradeId+"&x="+x+"&y="+y, ids, function (rep) {
                Common.render({tmpl: $('#students_temp'), data: rep.message, context: '#students',overwrite:1});
                Common.render({tmpl: $('#students_temp2'), data: rep.message, context: '#students2',overwrite:1});
                selectSet(rep.message);
            })
        }
        else{
            $("#kejie").html("");
            $("#kejie2").html("");
            Common.render({tmpl: $('#students_temp'), data: [], context: '#students',overwrite:1});
            Common.render({tmpl: $('#students_temp2'), data: [], context: '#students2',overwrite:1});
        }


    }

    zixiclass.getZiXiBanByXY = function () {
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        var x = $("#content em").filter(".zuh-cur").attr("x");
        var y = $("#content em").filter(".zuh-cur").attr("y");
        Common.getData('/n33_zixi/getZiXiBanByXY.do', {"ciId": xqid,"gid":gradeId,"x":x,"y":y}, function (rep) {
            if(rep.message.length==0){
                $("#none_png").show();
                $("#all_content").hide();
            }
            else{
                $("#none_png").hide();
                $("#all_content").show();
            }
            Common.render({tmpl: $('#zixiban_temp'), data: rep.message, context: '#zixiban',overwrite:1});
            Common.render({tmpl: $('#zixiban_temp3'), data: rep.message, context: '#zixiban3',overwrite:1});
            Common.render({tmpl: $('#select_zixi_templ'), data: rep.message, context: '#select_zixi',overwrite:1});
        })
    }
    zixiclass.getRoomByXY = function(){
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        var x = $("#weekday").val();
        var y = $("#course").val();
        Common.getData('/n33_zixi/getRoomByXY.do', {"ciId": xqid,"gid":gradeId,"x":x,"y":y}, function (rep) {
            if(rep.message.length==0){
                rep.message.push({"ykbid":"null","roomname":"暂无教室"});
            }
            Common.render({tmpl: $('#classroom_temp'), data: rep.message, context: '#classroom',overwrite:1});
        })
    }
    zixiclass.getTeachersByXY = function(){
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        var x = $("#weekday").val();
        var y = $("#course").val();
        Common.getData('/n33_zixi/getTeachersByXY.do', {"ciId": xqid,"gid":gradeId,"x":x,"y":y}, function (rep) {
            if(rep.message.length==0){
                rep.message.push({"id":"null","name":"暂无教师"});
            }
            Common.render({tmpl: $('#teacher_temp'), data: rep.message, context: '#teacher',overwrite:1});
        })
    }
    zixiclass.createZiXiBan = function () {
        var param = {};
        param.ciId = xqid;
        param.gid = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                param.gid = $(this).attr('ids');
            }
        })
        param.name = $("#zixiname").val();
        if(!param.name||param.name==null){
            layer.alert("名称不能为空");
            return;
        }
        param.ykbId = $("#classroom option:selected").attr("ykb");
        param.roomId = $("#classroom").val();
        if(!param.ykbId||param.ykbId=="null"){
            layer.alert("教室不能为空");
            return;
        }
        param.tid = $("#teacher").val();
        if(!param.tid||param.tid=="null"){
            layer.alert("教师不能为空");
            return;
        }
        Common.getData('/n33_zixi/createZiXiBan.do', param, function (rep) {
            if(rep.code==200){
                layer.msg("创建成功");
                zixiclass.getZiXiBanByXY();
                $('.bg').hide();
                $('.cj-popup').hide();
            }
        })
    }
    // $('body').on('change','#klass',function(){
    //     var checkText=$("#klass").val();
    //     $('#students2 tr').each(function(){
    //         $(this).find('td').eq(2).each(function() {
    //             if ($(this).find('em').text() != checkText) {
    //                 $(this).parent().hide();
    //             }else {
    //                 $(this).parent().show();
    //             }
    //         })
    //     })
    //
    // })
    zixiclass.addStudentsToZiXi = function () {
        var id=$("#select_zixi").val();
        var gid = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        })
        var students = [];
        $("#students2 input").each(function () {
            if($(this).is(":checked")){
                students.push($(this).attr("uid"));
            }
        });
        var x = $("#content em").filter(".zuh-cur").attr("x");
        var y = $("#content em").filter(".zuh-cur").attr("y");
        Common.getPostBodyData('/n33_zixi/addStudentsToZiXi.do?id='+id+"&ciId="+xqid+"&x="+x+"&y="+y+"&gid="+gid, students, function (rep) {
            if(rep.code==200){
                layer.msg("添加成功");
                zixiclass.getTable();
                zixiclass.getStudents();
                $("#content em").each(function () {
                    if($(this).attr("x")==x&&$(this).attr("y")==y){
                        $(this).click();
                    }
                });
                $('.bg').hide();
                $('.xz-popup').hide();
            }
        })
    }
    zixiclass.removeZiXiBan = function (id) {
        Common.getData('/n33_zixi/removeZiXiBan.do', {"ciId":xqid,"id":id}, function (rep) {
            if(rep.code==200){
                layer.msg("删除成功");
                zixiclass.getZiXiBanByXY();
            }
        })
    }
    /*zixiclass.removeZiXiBan2 = function (id) {
        Common.getData('/n33_zixi/removeZiXiBan.do', {"ciId":xqid,"id":id}, function (rep) {
            if(rep.code==200){
                layer.msg("删除成功");
                zixiclass.getZiXiBan();
            }
        })
    }*/
    zixiclass.updateRoomTeacher = function (id,roomId,teaId) {
        if(!teaId||teaId==""||teaId==null){
            layer.alert("必须选择一个老师");
            return false;
        }
        if(!roomId||roomId==""||roomId==null){
            layer.alert("必须选择一个教室");
            return false;
        }
        Common.getData('/n33_zixi/updateRoomTeacher.do',{"ciId":xqid,"id":id,"teaId":teaId,"roomId":roomId} , function (rep) {
            if(rep.code==200){
                layer.msg("修改成功");
            }
        })
    }


    zixiclass.getRoomByXY2 = function(obj,x,y){
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        // var x = $("#content em").filter(".zuh-cur").attr("x");
        // var y = $("#content em").filter(".zuh-cur").attr("y");
        Common.getData('/n33_zixi/getRoomByXY.do', {"ciId": xqid,"gid":gradeId,"x":x,"y":y}, function (rep) {
            rep.message.push(obj);
            if(rep.message.length==0){
                rep.message.push({"ykbid":"null","roomname":"暂无教室"});
            }
            Common.render({tmpl: $('#classroom_temp'), data: rep.message, context: '.classroom',overwrite:1});
        })
    }
    zixiclass.getTeachersByXY2 = function(obj,x,y){
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        })
        // var x = $("#content em").filter(".zuh-cur").attr("x");
        // var y = $("#content em").filter(".zuh-cur").attr("y");
        Common.getData('/n33_zixi/getTeachersByXY.do', {"ciId": xqid,"gid":gradeId,"x":x,"y":y}, function (rep) {
            rep.message.push(obj);
            if(rep.message.length==0){
                rep.message.push({"id":"null","name":"暂无教师"});
            }
            Common.render({tmpl: $('#teacher_temp'), data: rep.message, context: '.teacher',overwrite:1});
        })
    }

    zixiclass.getZiXiBan = function () {
        var gradeId = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gradeId = $(this).attr('ids');
            }
        });
        Common.getData('/n33_zixi/getZiXiBan.do', {"ciId": xqid,"gid":gradeId}, function (rep) {
            Common.render({tmpl: $('#zixiban_temp'), data: rep.message, context: '#zixiban2',overwrite:1});

        })
    }
    zixiclass.exportZiXiBan = function () {
        var gid = "";
        $('.xueke label').each(function (i) {
            if ($(this).hasClass("active")) {
                gid = $(this).attr('ids');
            }
        });
        window.location.href = "/n33_zixi/exportZiXiBan.do?gid="+gid+"&ciId="+xqid;
    }
    function autoArranage(cap,type) {
        $(".jhh").show();
        setTimeout(anonyumous,100);
        function anonyumous(){
            var gradeId = "";
            $('.xueke label').each(function (i) {
                if ($(this).hasClass("active")) {
                    gradeId = $(this).attr('ids');
                }
            })
            Common.getPostBodyData('/n33_zixi/autoArranage.do?ciId='+xqid+"&gradeId="+gradeId+"&cap="+cap+"&type="+type, table, function (rep) {
                $(".jhh").hide();
                $('.bg').hide();
                if(rep.code==200){
                    if(rep.message.length==0){
                        layer.msg("排自习已完成!");
                    }
                    else{
                        var msg = "";
                        for(var i in rep.message){
                            msg+= rep.message[i].time+rep.message[i].reason+",";
                        }
                        console.log(msg);
                        layer.msg("排自习已完成，存在教师或教室不足的课节");
                    }
                }
                zixiclass.getTable();
            })
        }

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
                    $(".xueke label").each(function () {
                        if(gradeId==$(this).attr("ids")){
                            $(this).click();
                        }
                    });
                } else {

                }
            });
        } catch (x) {
            console.log(x);
        }
    }
    function getCiIdIsFaBu() {
        var flag = null;
        Common.getData('/new33isolateMange/getCiIdIsFaBu.do', {"ciId":xqid}, function (rep) {
            flag = rep.message;
        });
        return flag;
    }
    var flg = true;
    function selectSet(students){
        var map1 = new Map();
        var map2 = new Map();
        var map3 = new Map();
        var arr1 = [];
        var arr2 = [];
        var arr3 = [];
        for(var i in students){
            if(students[i].clsName!=""&&!map1.get(students[i].clsName)){
                arr1.push({"name":students[i].clsName});
                map1.set(students[i].clsName,1);
            }
            if(students[i].conn!=""&&!map2.get(students[i].conn)){
                arr2.push({"name":students[i].conn});
                map2.set(students[i].conn,1);
            }
            if(students[i].lev!=""&&!map3.get(students[i].lev)){
                arr3.push({"name":students[i].lev});
                map3.set(students[i].lev,1);
            }

        }
        $('#ms1').empty();
        $('#ms2').empty();
        $('#ms3').empty();
        if (arr1!=null && arr1.length!=0) {
            var str1 = "";
            for (var i=0;i<arr1.length;i++) {
                str1 += '<option value="'+ arr1[i].name +'">'+arr1[i].name+'</option>';
            }
            $('#ms2').append(str1);
        }
        if (arr2!=null && arr2.length!=0) {
            var str2 = "";
            for (var i=0;i<arr2.length;i++) {
                str2 += '<option value="'+ arr2[i].name +'">'+arr2[i].name+'</option>';
            }
            $('#ms3').append(str2);
        }
        if (arr3!=null && arr3.length!=0) {
            var str3 = "";
            for (var i=0;i<arr3.length;i++) {
                str3 += '<option value="'+ arr3[i].name +'">'+arr3[i].name+'</option>';
            }
            $('#ms1').append(str3);
        }
        if (flg) {
            $('.ms').fSelect();
            flg = false;
        } else {
            $('.ms').fSelect("reload");
        }
        // Common.render({tmpl: $('#select_set'), data: arr1, context: '#ms1',overwrite:1});
        // Common.render({tmpl: $('#select_set'), data: arr2, context: '#ms3',overwrite:1});
        // Common.render({tmpl: $('#select_set'), data: arr3, context: '#ms2',overwrite:1});
    }


    function fSelectAction() {
        var array1=$('#ms1').val();
        var array2=$('#ms2').val();
        var array3=$('#ms3').val();
        for(var i=0;i<$('#students2 tr').length;i++){
            var searchText1 = $('#students2 tr').eq(i).find('td').eq(1).find('em').text();
            var searchText2 = $('#students2 tr').eq(i).find('td').eq(2).find('em').text();
            var searchText3 = $('#students2 tr').eq(i).find('td').eq(3).find('em').text();
            console.log(searchText1,searchText2,searchText3);
            if(array1==null && array2==null && array3==null){
                $('#students2 tr').eq(i).show()
            }else{
                var a=1;
                var b=1;
                var c=1;
                if(array1!=null){a=array1.indexOf(searchText1)}
                if(array2!=null){b=array2.indexOf(searchText2)}
                if(array3!=null){c=array3.indexOf(searchText3)}

                console.log(a,b,c);

                if(a>=0 && b>=0 && c>=0){
                    $('#students2 tr').eq(i).show()
                }else{
                    $('#students2 tr').eq(i).hide()
                }

            }
        }
    }
    module.exports = zixiclass;
})