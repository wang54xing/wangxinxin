'use strict';
define(['jquery','doT','easing','common','select2',"layer"],function(require,exports,module){
    (function ($) {
        var ms = {
            init: function (obj, args) {
                return (function () {
                    ms.fillHtml(obj, args);
                    ms.bindEvent(obj, args);
                })();
            },
            //填充html
            fillHtml: function (obj, args) {
                return (function () {
                    obj.empty();
                    //上一页
                    if (args.current > 1) {
                        obj.append('<a href="javascript:;" class="prevPage"><上一页</a>');
                    } else {
                        obj.remove('.prevPage');
                        obj.append('<span class="disabled"><上一页</span>');
                    }
                    //中间页码
                    if (args.current != 1 && args.current >= 4 && args.pageCount != 4) {
                        obj.append('<a href="javascript:;" class="tcdNumber">' + 1 + '</a>');
                    }
                    if (args.current - 2 > 2 && args.current <= args.pageCount && args.pageCount > 5) {
                        obj.append('<span>...</span>');
                    }
                    var start = args.current - 2, end = args.current + 2;
                    if ((start > 1 && args.current < 4) || args.current == 1) {
                        end++;
                    }
                    if (args.current > args.pageCount - 4 && args.current >= args.pageCount) {
                        start--;
                    }
                    for (; start <= end; start++) {
                        if (start <= args.pageCount && start >= 1) {
                            if (start != args.current) {
                                obj.append('<a href="javascript:;" class="tcdNumber">' + start + '</a>');
                            } else {
                                obj.append('<span class="current">' + start + '</span>');
                            }
                        }
                    }
                    if (args.current + 2 < args.pageCount - 1 && args.current >= 1 && args.pageCount > 5) {
                        obj.append('<span>...</span>');
                    }
                    if (args.current != args.pageCount && args.current < args.pageCount - 2 && args.pageCount != 4) {
                        obj.append('<a href="javascript:;" class="tcdNumber">' + args.pageCount + '</a>');
                    }
                    //下一页
                    if (args.current < args.pageCount) {
                        obj.append('<a href="javascript:;" class="nextPage">下一页></a>');
                    } else {
                        obj.remove('.nextPage');
                        obj.append('<span class="disabled">下一页></span>');
                    }
                    //跳转页码
                    if (args.turndown == 'true') {
                        obj.append('<span class="countYe">到第<input type="text" maxlength=' + args.pageCount.toString().length + '>页<a href="javascript:;" class="turndown">确定</a><span>');
                    }
                })();
            },
            //绑定事件
            bindEvent: function (obj, args) {
                return (function () {
                    obj.off("click", "a.tcdNumber");
                    obj.one("click", "a.tcdNumber", function () {
                        var current = parseInt($(this).text());
                        ms.fillHtml(obj, {"current": current, "pageCount": args.pageCount, "turndown": args.turndown});
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current);
                        }
                    });
                    //上一页
                    obj.off("click", "a.prevPage");
                    obj.one("click", "a.prevPage", function () {
                        var current = parseInt(obj.children("span.current").text());
                        ms.fillHtml(obj, {
                            "current": current - 1,
                            "pageCount": args.pageCount,
                            "turndown": args.turndown
                        });
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current - 1);
                        }
                    });
                    //下一页
                    obj.off("click", "a.nextPage");
                    obj.one("click", "a.nextPage", function () {
                        var current = parseInt(obj.children("span.current").text());
                        ms.fillHtml(obj, {
                            "current": current + 1,
                            "pageCount": args.pageCount,
                            "turndown": args.turndown
                        });
                        if (typeof(args.backFn) == "function") {
                            args.backFn(current + 1);
                        }
                    });
                    //跳转
                    obj.one("click", "a.turndown", function () {
                        var page = $("span.countYe input").val();
                        if (page > args.pageCount) {
                            layer.alert("您的输入有误，请重新输入！");
                        }
                        ms.fillHtml(obj, {"current": page, "pageCount": args.pageCount, "turndown": args.turndown});
                        /*if(typeof(args.backFn)=="function"){
                         args.backFn(current+1);
                         }*/
                    });
                })();
            }
        }
        $.fn.createPage = function (options) {
            var args = $.extend({
                pageCount: 10,
                current: 1,
                turndown: true,
                backFn: function () {
                }
            }, options);
            ms.init(this, args);
        }
    })(jQuery);
    var subjectMange = {},
        Common = require('common');
        require('select2');
    require('layer');
    var subjectMangeData = {};
    subjectMange.init = function(){
        subjectMangeData.page = 1;
        subjectMangeData.pageSize = 10;
        //-------------------------學科----------------------------------------------------------------------
        subjectMange.getSubjectList();

        $('#subSeach').click(function() {
            subjectMangeData.page = 1;
            subjectMange.getSubject($('#subjectTId').val(),$('#subjectT').text());
        });
        //取消
        $(".btn-esc,.alert-r").click(function(){
            $(".alert-top").parent().hide();
            $(".bg").hide();
        })

        $('#subjectType').change(function() {
            if ($('#subjectType').val()==1) {
                $('.type').show();
            } else {
                $('.type').hide();
            }
        });
        //添加学科
        $('.subject-manage .subject-add').click(function(){
            $('#subjectName').val('');
            $('#subjectUserName').val('');
            $('#subjectUserId').val('');
            $('#subjectId').val('');
            subjectMange.getSelectTeacherList($('.edit-subject-teacher'),function() {
                $('.edit-subject-teacher').select2("destroy");
                initSelect($('.edit-subject-teacher'));
            });
            $('.addsubject-alert,.bg').show();
        });

        //返回学科管理首页
        $('.subject-manage .return-div img').click(function(){
            $(".hide-sub").hide();
            $(".show-sub").show();
            subjectMange.getSubjectList();
        });
        $('.subject-sure').click(function() {
            subjectMangeData.id=$('#subjectId').val();
            subjectMangeData.subjectName = $('#subjectName').val();
            //subjectMangeData.leadUserName = $('#subjectUserName').val();
            subjectMangeData.leadUser = $('.edit-subject-teacher').select2("val")=="0"?"":$('.edit-subject-teacher').select2("val");
            subjectMangeData.subjectType = $('#subjectType').val();
            subjectMangeData.attachedSubjectId = $('#subject').val();
            subjectMangeData.index = $('#index').val();
            if ($('#subjectName').val()=='') {
                layer.alert("请输入学科名称！");
                return;
            }
            if ($('#index').val()=='') {
                layer.alert("请输入序号！");
                return;
            }
            subjectMange.addOrUdpSubject();
        });
        //$('#subjectUserName').blur(function() {
        //    if ($(this).val()!='') {
        //        subjectMangeData.userName = $('#subjectUserName').val();
        //        subjectMange.checkUserName();
        //    }
        //});


    }
    //-----------------------通用--------------------------------------------------------------------------------------------------------------------
    subjectMange.checkUserName = function() {
        Common.getData('/shoolbase/checkUserName.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                if (rep.message!=null) {
                    $('#subjectUserId').val(rep.message);
                } else {
                    layer.alert("用户名不存在！");
                    $('#subjectUserName').val('');
                }
            } else {
                layer.alert(rep.message);
            }
        });
    }

    //-------------学科管理--------------------------------------------------------------------------------------------------------------------------
    subjectMange.getSubjectInfo = function() {
        Common.getData('/subject/getSubjectInfo.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                $('#subjectName').val(rep.message.subjectName);
                $('#subjectId').val(rep.message.id);
                $('#subjectType').val(rep.message.subjectType);
                $('#index').val(rep.message.index);
                if (rep.message.subjectType==1) {
                    $('.type').show();
                    $('#subject').val(rep.message.attachedSubjectId);
                } else {
                    $('.type').hide();
                }
                subjectMange.getSelectTeacherList($('.edit-subject-teacher'),function() {
                    //年级组长控制
                    var childs=$('.edit-subject-teacher').children();
                    for(var i=0;i<childs.length;i++){
                        if(childs[i].value==rep.message.leadUser){
                            childs[i].selected='selected';
                        }
                    }
                    $('.edit-subject-teacher').select2("destroy");
                    initSelect($('.edit-subject-teacher'));
                });
                $('.addsubject-alert,.bg').show();
            }
        });
    }
    subjectMange.getSubjectList = function() {
        Common.getData('/subject/getSubjectList.do', subjectMangeData,function(rep){
            $('.subjectList').html('');
            var sublist = rep.message;
            for(var i in sublist){
            	if(sublist[i].subjectType==1){
            		var attchId = sublist[i].attachedSubjectId;
            		for(var j in sublist){
            			if(sublist[j].id==attchId){
            				sublist[i].mainName=sublist[j].subjectName;
            			}
            		}
            		
            	}
            	else{
            		
            			sublist[i].mainName="";
            		
            	}
            }
            rep.message = sublist;
            Common.render({tmpl:$('#subjectList_templ'),data:rep,context:'.subjectList'});
            $('#subject').html('');
            Common.render({tmpl:$('#subject_templ'),data:rep,context:'#subject'});
            $('.editSubject').click(function() {
                subjectMangeData.id = $(this).attr('subId');
                subjectMange.getSubjectInfo();
            });
            //教师管理-选择学科
            $(".sub-list li").click(function(){
                $(this).toggleClass("li-select");
            });
            $('.subject-edit').click(function() {
                subjectMangeData.page = 1;
                subjectMange.getSubject($(this).attr('subId'),$(this).attr('sunm'));
            });
            $('.subject-ck').click(function() {
                if (confirm('确认删除！')) {
                    subjectMange.delSubject($(this).attr('subId'));
                }
            });
            $('.addIndex').click(function () {
                var index = $(this).attr('idx');
                if (index==0) {
                    layer.alert("0不可以调节！");
                    return;
                }
                subjectMange.updateIndex($(this).attr('subId'),1);
            });
            $('.minIndex').click(function () {
                var index = $(this).attr('idx');
                if (index==0) {
                    layer.alert("0不可以调节！");
                    return;
                } else if (index==1) {
                    layer.alert("已经在第一位了！");
                    return;
                }
                subjectMange.updateIndex($(this).attr('subId'),2);
            })
            var subLength = $('.subject-name').text().length;
            var sum = $('.subject-name').length;
            for(var i=0 ; i<sum;i++){
                if($('.subject-name').eq(i).text().length>=20){
                    $('.subject-name').css('overflow','hidden');
                    $('.subject-name').css('width','20em');
                    $('.subject-name').eq(i).next('.subject-xq').css('display','inline-block');
                    $('.subject-name').eq(i).next().find('.subject-an').css('display','inline-block')

                }
            }
        });
    }
    subjectMange.getSubject = function(id,nm) {
        subjectMangeData.id = id;
        subjectMangeData.keyword = $('#subjectKeyword').val();
        Common.getData('/subject/getSubject.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                if (nm.indexOf("组老师")==-1) {
                    $('#subjectT').text(nm+"组老师");
                }
                $('#subjectTId').val(id);
                $('.subjectUsers').html('');
                Common.render({tmpl:$('#subjectUsers_templ'),data:rep,context:'.subjectUsers'});
                var totalPage = 0;
                if (rep.message.count % rep.message.pageSize == 0) {
                    totalPage = rep.message.count / rep.message.pageSize;
                } else {
                    totalPage = parseInt(rep.message.count / rep.message.pageSize) + 1;
                }
                $(".pageDiv").createPage({
                    pageCount:totalPage,//总页数
                    current:rep.message.page,//当前页
                    turndown:'false',//是否显示跳转框，显示为true，不现实为false,一定记得加上引号...
                    backFn:function(p){
                        if (subjectMangeData.page!=p) {
                            subjectMangeData.page = p;
                            subjectMange.getSubject(id,nm);
                        }

                    }
                });
                $(".show-sub").hide();
                $(".hide-sub").show();
                $('.subject-head').click(function() {
                    subjectMangeData.id = $('#subjectTId').val();
                    subjectMange.setSubjectLeader($(this).attr('uid'));
                })
                $('.table-del').click(function() {
                    if (confirm('确认删除学科老师！')) {
                        subjectMangeData.subjectId = $('#subjectTId').val();
                        subjectMange.delSubjectUser(this);
                    }
                });
            } else {
                layer.alert(rep.message);
            }
        });
    }
    subjectMange.setSubjectLeader = function(uid) {
        subjectMangeData.leadUser = uid;
        Common.getData('/subject/setSubjectLeader.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                subjectMange.getSubject($('#subjectTId').val(),$('#subjectT').text());
            } else {
                layer.alert(rep.message);
            }
        });
    }
    subjectMange.delSubjectUser = function(dom) {
        subjectMangeData.userId = $(dom).attr('uid');
        Common.getData('/subject/delSubjectUser.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                subjectMange.getSubject($('#subjectTId').val(),$('#subjectT').text());
            } else {
                layer.alert(rep.message);
            }
        });
    }

    subjectMange.delSubject = function(id) {
        subjectMangeData.id = id;
        Common.getData('/subject/delSubject.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                subjectMange.getSubjectList();
            } else {
                layer.alert(rep.message);
            }
        });
    }
    subjectMange.addOrUdpSubject  = function() {
        Common.getData('/subject/addOrUdpSubject.do', subjectMangeData,function(rep){
            if (rep.code=='200') {
                $('.addsubject-alert,.bg').hide();
                subjectMange.getSubjectList();
            } else {
                layer.alert(rep.message);
            }
        });
    }

    subjectMange.getSelectTeacherList = function(target,callback) {
        target.empty();
        target.append('<option value="0">请选择...</option>');
        Common.getData('/teacher/selTeachersBySchoolId.do', subjectMangeData,function(rep){
            var obj = eval('(' + rep.message + ')');
            for (var i = 0; i < obj.message.length; i++) {
                var content = '';
                content += '<option value=' + obj.message[i].userId + '>' + obj.message[i].userName + '</option>';
                target.append(content);
            }
            callback();
        });

    }
    // 初始化select2
    function initSelect(target) {
        target.select2({
            width: '250px',
            containerCss: {
                'margin-left': '0px',
                'font-family': 'sans-serif'
            },
            dropdownCss: {
                'font-size': '14px',
                'font-family': 'sans-serif'
            }
        });
    }
    subjectMange.updateIndex = function(id,type) {
        var data = {};
        data.id = id;
        data.type = type;
        Common.getData('/subject/updateIndex.do', data,function(rep){
            if (rep.code=='200') {
                layer.msg("调节成功！");
                subjectMange.getSubjectList();
            } else {
                layer.alert("序号调节失败！");
            }
        });
    }

    subjectMange.init();
});