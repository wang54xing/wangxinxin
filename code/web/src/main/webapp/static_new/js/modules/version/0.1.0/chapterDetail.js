/**
 * Created by albin on 2017/5/11.
 */
define('chapterDetail', ['jquery', 'doT', 'common', 'pagination', "layer", "Pictab"], function (require, exports, module) {
    var chapterDetail = {};
    require('jquery');
    require('doT');
    require('pagination');
    require('layer');
    require('Pictab');
    common = require('common')
    chapterDetail.init = function () {
        if ($("#is").val() == 0) {
            $(".chapter-operate-wrap").hide()
        } else {
            $(".chapter-operate-wrap").show()
        }
        $("body").on("click", ".chapter-left dd", function () {
            $(".textbooks-detail dd").removeClass("active");
            $(this).addClass("active");
            chapterDetail.getTP();
            $("#fileToUpload").val("");
        })
        $(".chapter-delete").click(function () {
            if ($(".active").attr("ids") != undefined) {
                layer.confirm('确实要删除吗？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    layer.msg("正在删除！", {time: 1000})
                    var p = {};
                    p.did = $(".ective").attr("ids");
                    p.id = $(".active").attr("ids");
                    if (p.did != undefined) {
                        common.getData("/teacher/dictionary/removeDoc.do", p, function (res) {
                            chapterDetail.getTP();
                        })
                    } else {
                        layer.msg("没有图片可以删除！");
                    }
                }, function () {
                });
            } else {
                layer.msg("请添加好章节目录在添加图片！")
            }
        })

        chapterDetail.get();
        chapterDetail.getTP();

        $('#bimg li img').map(function () {
            $wid = $(this).width();
            $hig = $(this).height();
            $bi = $wid / $hig;
            ml = (500 - $wid / $hig * 500) / 2;
            mt = (500 - $hig / $wid * 500) / 2;
            if ($bi <= 1) {
                $(this).css({'height': '500px', 'marginLeft': ml + 'px'})
            } else {
                $(this).css({'width': '500px', 'marginTop': mt + 'px'})
            }
        })


        $("body").on("change", "#fileToUpload", function () {
            if ($(".active").attr("ids") != undefined) {
                layer.confirm('确实要上传吗？', {
                    btn: ['确定', '取消'] //按钮
                }, function () {
                    layer.msg("正在上传！", {time: 1000})
                    chapterDetail.addTp();
                }, function () {
                });
            } else {
                layer.msg("请添加好章节目录在添加图片！")
            }
        })
    }

    chapterDetail.get = function () {
        var p = {};
        p.dictionaryId = $(".path-cur").attr("ids");
        common.getData("/teacher/dictionary/materialList.do", p, function (res) {
            if (res.message.length > 0) {
                common.render({tmpl: $('#textxj'), data: res.message, context: ".textbooks-detail"});
                $.each(res.message, function (i, obj) {
                    common.render({tmpl: $('#textxj1'), data: obj.list, context: "#tt_" + obj.id});
                })
                $(".textbooks-detail dd:eq(0)").addClass("active")
            }else{
                $("<div class='no-textbooks'>暂无教材</div>").appendTo(".textbooks-detail");
            }
        })
    }

    chapterDetail.getTP = function () {
        $("#miimg").empty();
        $("#bimg").empty();
        var p = {};
        p.id = $(".active").attr("ids");
        if (p.id != undefined) {
            common.getData("/teacher/dictionary/getTP.do", p, function (res) {
                common.render({tmpl: $('#minimg'), data: res.message, context: "#miimg"});
                common.render({tmpl: $('#bigimg'), data: res.message, context: "#bimg"});
                if (res.message.length > 0) {
                    can = $('#demo1').banqh({
                        box: "#demo1",//总框架
                        pic: "#ban_pic1",//大图框架
                        pnum: "#ban_num1",//小图框架
                        prev_btn: "#prev_btn1",//小图左箭头
                        next_btn: "#next_btn1",//小图右箭头
                        pop_prev: "#prev2",//弹出框左箭头
                        pop_next: "#next2",//弹出框右箭头
                        prev: "#prev1",//大图左箭头
                        next: "#next1",//大图右箭头
                        pop_div: "#demo2",//弹出框框架
                        pop_pic: "#ban_pic2",//弹出框图片框架
                        pop_xx: ".pop_up_xx",//关闭弹出框按钮
                        mhc: ".mhc",//朦灰层
                        autoplay: true,//是否自动播放
                        interTime: 5000,//图片自动切换间隔
                        delayTime: 400,//切换一张图片时间
                        pop_delayTime: 400,//弹出框切换一张图片时间
                        order: 0,//当前显示的图片（从0开始）
                        picdire: true,//大图滚动方向（true为水平方向滚动）
                        mindire: true,//小图滚动方向（true为水平方向滚动）
                        min_picnum: 5,//小图显示数量
                        pop_up: true//大图是否有弹出框
                    })
                }
            })
        }
    }

    chapterDetail.addTp = function () {
        var value = $("#fileToUpload").val();
        if (value != "") {
            var result = /\.[^\.]+/.exec(value);
            if (result == ".jpg" || result == ".png" || result == ".jpeg" || result == ".gif" || result == ".bmp") {
                $.ajaxFileUpload({
                    url: '/teacher/dictionary/uploadDoc.do',
                    param: {id: $(".active").attr("ids")},
                    secureuri: false,
                    fileElementId: 'fileToUpload',
                    dataType: 'json',
                    success: function (data) {
                        chapterDetail.getTP();
                    },
                    error: function (e) {
                        layer.msg("网络错误，请重试！！");
                    }
                })
            } else {
                layer.msg("请传正确的图片格式！")
            }
        } else {
            layer.msg("请选择图片!");
        }
    }
    module.exports = chapterDetail;
})