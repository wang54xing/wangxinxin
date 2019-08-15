'use strict';
define(['jquery','doT','easing','common',"layer"],function(require,exports,module){
    var timeTableManage = {},
        Common = require('common');
    require('layer');
    var timeTableManageData = {};
    timeTableManage.init = function(){
        timeTableManageData.page = 1;
        timeTableManageData.pageSize = 10;

        //-----------------------课表------------------------------------------
        timeTableManage.getTimeTableList();
        //取消
        $(".btn-esc,.alert-r").click(function(){
            $(".alert-top").parent().hide();
            $(".bg").hide();
        })
        //课表管理---添加课表
        $(".step-6 .newAdd").click(function(){
            $('#tableText').text("新建课表");
            $('#timeTableId').val("");
            $('#timeName').val("");
            $('#timesjd').val("");
            $('#timeStartTime').val("");
            $('#timeEndTime').val("");
            $('#timeType').val("");
            $('#timeRemark').val("");
            $(".bg,.addtable-alert").show();
        })
        $('.timetable-sure').click(function() {
            timeTableManageData.name = $('#timeName').val();
            timeTableManageData.startTime = $('#timeStartTime').val();
            timeTableManageData.sjd=$('#timesjd').val();
            timeTableManageData.endTime = $('#timeEndTime').val();
            timeTableManageData.type = $('#timeType').val();
            timeTableManageData.remark = $('#timeRemark').val();
            if ($('#timeTableId').val()!='') {
                timeTableManage.updTimeTable($('#timeTableId').val());
            } else {
                timeTableManage.addTimeTable();
            }
        });
    }

    //----------------课表---------------------------------------------------------------------------------------------------------------------------------
    timeTableManage.addTimeTable = function() {
        Common.getData('/timetable/addTimeTble.do', timeTableManageData,function(rep){
            if (rep.code=='200') {
                $(".bg,.addtable-alert").hide();
                timeTableManage.getTimeTableList();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    timeTableManage.updTimeTable = function(id) {
        timeTableManageData.id = id;
        Common.getData('/timetable/updTimeTable.do', timeTableManageData,function(rep){
            if (rep.code=='200') {
                $(".bg,.addtable-alert").hide();
                timeTableManage.getTimeTableList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    timeTableManage.getTimeTable = function(id) {
        timeTableManageData.id = id;
        Common.getData('/timetable/getTimeTable.do', timeTableManageData,function(rep){
            if (rep.code=='200') {
                timeTableManage.clearTimeTable();
                $('#timeTableId').val(id);
                $('#timeName').val(rep.message.name);
                $('#timesjd').val(rep.message.sjd);
                $('#timeStartTime').val(rep.message.startTime);
                $('#timeEndTime').val(rep.message.endTime);
                $('#timeType').val(rep.message.type);
                $('#timeRemark').val(rep.message.remark);
                $(".bg,.addtable-alert").show();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    timeTableManage.clearTimeTable = function() {
        $('#timeTableId').val("");
        $('#timeName').val("");
        $('#timesjd').val("");
        $('#timeStartTime').val("");
        $('#timeEndTime').val("");
        $('#timeType').val("");
        $('#timeRemark').val("");
    }
    timeTableManage.delTimeTable = function(id) {
        timeTableManageData.id = id;
        Common.getData('/timetable/delTimeTable.do', timeTableManageData,function(rep){
            if (rep.code=='200') {
                timeTableManage.getTimeTableList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    timeTableManage.getTimeTableList = function() {
        Common.getData('/timetable/getTimeTableList.do', timeTableManageData,function(rep){
            if (rep.code=='200') {
                $('.timeTableList').html('');
                Common.render({tmpl:$('#timeTableList_templ'),data:rep,context:'.timeTableList'});
                $('.table-edit').click(function() {
                    $('#tableText').text("编辑课表");
                    timeTableManage.getTimeTable($(this).attr('tid'));
                });
                $('.table-del').click(function() {
                    if (confirm('确认删除！')) {
                        timeTableManage.delTimeTable($(this).attr('tid'));
                    }
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }

    timeTableManage.init();
});