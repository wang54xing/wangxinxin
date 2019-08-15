/**
 * Created by albin on 2017/5/2.
 */
define('schoolUserInfo', ['jquery', 'doT', 'common', 'pagination', "layer",], function (require, exports, module) {
        var schoolUserInfo = {};
        require('jquery');
        require('doT');
        require('pagination');
        require('layer');
        Common = require('common');
        var pg = 1;
        schoolUserInfo.getGradeList = function() {
            Common.getData('/schoolGrade/selGradeList.do', "",function(rep){
                Common.render({tmpl: $('#gra'), data: rep.message, context: '#grade'});
            });
        }
        schoolUserInfo.getClassInfoByGrade = function() {
            var classManageData={}
            classManageData.gradeId = $("#grade").val();
            Common.getData('/schoolClass/getClassInfoByGrade.do', classManageData,function(rep){
                var obj = eval('(' + rep.message + ')');
                $("#classList").empty()
                    Common.render({tmpl:$('#classLists'),data:obj.message,context:'#clss'});

            });
        }
        schoolUserInfo.impYu = function (par) {
            $.ajaxFileUpload({
                url: '/teacher/addUserImpYu.do',
                param: par,
                secureuri: false,
                fileElementId: 'file',
                dataType: 'json',
                success: function (dt) {
                    $("#cfname").empty();
                    if (dt.code == 500) {
                        layer.msg("第" + dt.message + "数据存在问题,请手手动修改");
                    } else {
                        if (dt.message.length > 0) {
                            Common.render({tmpl: $('#cfcf'), data: dt.message, context: '#cfname'});
                        } else {
                            layer.msg('可以导入该数据集合')
                        }
                    }
                },
                error: function (e) {
                    layer.msg("网络错误，请重试！！");
                }
            })
        }


        schoolUserInfo.impUserInfo = function (par) {

            $.ajaxFileUpload({
                url: '/teacher/importUserList.do',
                param: par,
                secureuri: false,
                fileElementId: 'file',
                dataType: 'json',
                success: function (data) {
                    if (data.message == "ok") {
                        window.location.href = document.URL;
                    }
                    if (data.message != "ok" && data.code == 200) {
                        layer.msg(data.message)
                    }
                },
                error: function (e) {

                }
            })

        }
        schoolUserInfo.init = function () {
            $("#grade").change(function(){
                schoolUserInfo.getClassInfoByGrade();
            })
            schoolUserInfo.getGradeList()
            schoolUserInfo.getClassInfoByGrade()
            $("#sel").change(function(){
                var v=$(this).val();
                if(v=="学生"){
                    $(".ft").show();
                }else{
                    $(".ft").hide();
                }
            })

            $("#grade").change(function(){

            })

            $(".btn-addinset").click(function () {
                $("#edit-ph1").val("")
                $("#edit-logn1").val("")
                $("#edit-name1").val("")
                $(".wind-edit-ts,.bg").show();
            })
            $(".btn-dao").click(function () {
                $(".wind-edit-dao,.bg").show();
            })
            $("#daoT").click(function () {
                var role = $(".dc:checked").val();
                window.location.href = "/teacher/daoUserImp.do?role=" +role;
                $(".wind-edit-dao,.bg").hide();
            })
            $("#okss").click(function () {
                var par = {};
                par.sex = 1;
                if ($(".tt1:eq(1)").is(":checked")) {
                    par.sex = 0;
                }
                par.nm = $("#edit-name1").val();
                par.logn = $("#edit-logn1").val();
                par.mn = $("#edit-ph1").val();
                par.role = $("#sel").val();
                par.son = $("#edit-xue1").val();
                par.gra = $("#grade").val();
                par.gra=$("#grade option[value="+par.gra+"]").html()
                par.cls=$("#clss").val();
                if (par.logn != "" && par.nm != "") {
                    Common.getData("/teacher/addUserInfo.do", par, function (res) {
                        if (res.message == "on") {
                            layer.msg("登录名或手机号已经存在");
                        }
                        if (res.message == "ok") {
                            schoolUserInfo.getUserList(1);
                            $(".wind-edit-ts,.bg").hide();
                        }
                    })
                } else {
                    layer.msg("请将信息填写完整");
                }
            })


            //导入
            $(".sp-down").click(function () {
                window.location.href = "/teacher/addUserImp.do";
            })
            $("body").on("change", "#file", function () {
                var _this = $(this);
                var nameArr = _this.val().split("\\");
                var filename = nameArr[nameArr.length - 1];
                $(".file-name").text(filename);
                $("#cfname").empty();
            })
            $(".ft").click(function () {
                if ($(this).attr("ty") == 1) {
                    $("#sta").val("")
                    $("#end").val("")
                    $("#sta").attr("disabled", "disabled")
                    $("#end").attr("disabled", "disabled")
                } else {

                    $("#sta").attr("disabled", false)
                    $("#end").attr("disabled", false)
                }
            })

            //预检查
            $("#yujian").click(function () {
                var par = {};
                par.type = $(".ft:checked").attr("ty")
                par.sta = ""
                par.end = ""
                if (par.type == 0) {
                    if ($("#sta").val() == "" && $("#end").val() == "") {
                        layer.msg("请填写前缀或者后缀")
                        return;
                    }
                    par.sta = $("#sta").val();
                    par.end = $("#end").val();
                }
                if ($("#file").val() != "") {
                    var value = $("#file").val();
                    var result = /\.[^\.]+/.exec(value);
                    if (result == ".xls" || result == ".xlsx") {
                        schoolUserInfo.impYu(par);
                    } else {
                        layer.msg("请使用excel格式导入")
                    }
                } else {
                    layer.msg("请选择文件")
                }
            })

            $("#baocun").click(function () {
                    var par = {};
                    par.type = $(".ft:checked").attr("ty")
                    par.sta = ""
                    par.end = ""
                    if (par.type == 0) {
                        if ($("#sta").val() == "" && $("#end").val() == "") {
                            layer.msg("请填写前缀或者后缀")
                            return;
                        }
                        par.sta = $("#sta").val();
                        par.end = $("#end").val();
                    }
                    if ($("#file").val() != "") {
                        var value = $("#file").val();
                        var result = /\.[^\.]+/.exec(value);
                        if (result == ".xls" || result == ".xlsx") {
                            if (par.type == 0) {
                                layer.confirm('系统检测发现用户账号重名会自动增加数字为后缀（如：张三1），您确定要导入吗？', {
                                    title: '提示信息', btn: ['确定', '取消'] //按钮
                                }, function () {
                                    layer.msg('导入中。。。', {time: 1000});
                                    schoolUserInfo.impUserInfo(par);
                                }, function () {
                                });
                            } else {
                                schoolUserInfo.impUserInfo(par);
                            }
                        } else {
                            layer.msg("请使用excel格式导入")
                        }
                    } else {
                        layer.msg("请选择文件")
                    }
                }
            )


            $("#stuSearch").click(function () {
                schoolUserInfo.getUserList(1);
            })
            schoolUserInfo.getUserList(1);
            //编辑
            $("body").on("click", ".btn-editt", function () {
                $("#edit-ph").val($(this).attr("cellphone"))
                $("#edit-logn").val($(this).attr("logn"))
                $("#edit-name").val($(this).attr("name"))
                $("#edit-xue").val($(this).attr("son"))
                $("#oks").attr("ids", $(this).attr("ids"))
                var sex = $(this).attr("sex");
                $("input [name=sex]").prop("checked", false)
                if (sex == "男") {
                    $(".tt:eq(0)").prop("checked", true)
                }
                if (sex == "女") {
                    $(".tt:eq(1)").prop("checked", true)
                }
                $('.wind-edit,.bg').fadeIn();
            })
            $("#oks").click(function () {
                var par = {};
                par.userId = $(this).attr("ids");
                par.sex = 1;
                if ($(".tt:eq(1)").is(":checked")) {
                    par.sex = 0;
                }
                par.nm = $("#edit-name").val();
                par.logn = $("#edit-logn").val();
                par.mn = $("#edit-ph").val();
                par.son = $("#edit-xue").val();
                if (par.logn != "" && par.nm != "") {
                    Common.getData("/teacher/updateUserInfo.do", par, function (res) {
                        if (res.message == "on") {
                            layer.msg("修改的登录名或手机号已经存在");
                        }
                    })
                    schoolUserInfo.getUserList(pg);
                    $('.wind-edit,.bg').hide();
                } else {
                    layer.msg("登录名或者姓名不能为空")
                }
            })
            //修改密码
            $("body").on("click", ".btn-resett", function () {
                $("#oss").attr("ids", $(this).attr("ids"))
                $("#zhanghao").html($(this).attr("logn"))
                $('.wind-reset,.bg').fadeIn();
            })
            $("#oss").click(function () {
                var par = {};
                par.userId = $(this).attr("ids");
                par.passWord = $("#psd").val();
                //if(par.logn!=""&&par.nm!="") {
                Common.getData("/teacher/updateUserPassWord.do", par, function (res) {
                })
                $('.wind-reset,.bg').hide();
                $("#psd").val("")
                $("#rpsd").val("")
                //}else{
                //    layer.msg("登录名或者姓名不能为空")
                //}
            })
        }

        schoolUserInfo.getUserList = function (page) {
            $("#tbody").empty();
            var par = {};
            par.page = page;
            par.pageSize = 15;
            if ($("#keyword").val() != "") {
                par.userName = $("#keyword").val();
                pg = 1;
            } else {
                par.userName = "*";
                pg = page;
            }
            Common.getData("/teacher/selUserList.do", par, function (res) {
                Common.render({tmpl: $('#tbd'), data: res.message.list, context: '#tbody'});
                $('.pageDiv').jqPaginator({
                    totalPages: Math.ceil(res.message.count / 15) == 0 ? 1 : Math.ceil(res.message.count / 15),
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (n != page) {
                            page = n;
                            schoolUserInfo.getUserList(page);
                        }
                    }
                });
            })

        }

        module.exports = schoolUserInfo;
    }
)