/**
 * @author 李伟
 * @module  核心组件
 * @exports Common
 * @description
 * 公共方法，ajax,dotjs
 * @example
 * var Common = require("Common");
 */
/* global Config */
define('common',['jquery','doT','cal','easing','ajaxfileupload','widget','transport','fileupload','listnav','charfist', 'ieSolution'],function(require,exports,module){
    /**
     *初始化参数
     */
    var Common = {};
    require('jquery');
    require('easing');
    require('fileupload');
    require('ajaxfileupload');
    
    var is = require('ieSolution');
    
    /**
     * @func map
     * @desc ie浏览器下返回自定义map对象，其它浏览器返回标准Map对象
     * @example
     * var map = Common.map();
     */
    Common.map = function() {
    	if (is.isIE()) {
    		return new is.Map();
    	} else {
    		return new Map();
    	}
    }
    
    /**
     * @func getData
     * @desc 同步获取数据
     * @param {string} url 请求的地址
     * @param {object} data 请求的参数
     * @param {function} callback 回调函数
     * @returns {boolean}
     * @example
     * Common.getData("/test",“{}”,function(data){})
     */
    Common.getData = function(url,data,callback){
        //var encodeUrl = encodeURIComponent(url);
        $.ajax({
            type: "GET",
            data:data,
            url: url,
            async: false,
            cache : false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function(rep){
                if(!rep){
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };
    
    /**
     * @func fileUpload
     * @desc 异步上传附件
     * @param {string} id 出发上传的id
     * @param {string} url 异步请求的地址
     * @param {object} formats 文件格式限制
     * @param {object} tag 操作对象
     * @param {int} size 文件大小限制
     * @param {function} callback 回调函数
     * @returns {boolean}
     * @example
     * Common.ajaxFileUpload(id, "/test",formats,tag,size,function(data){})
     */
    Common.ajaxFileUpload = function(id,url,formats,tag,size,loadingId,callback){
        var filePath = $("#"+id).val();
        //判断上传文件的后缀名
        var format = filePath.substr(filePath.lastIndexOf('.') + 1);
        if(formats.length>0&&$.inArray(format, formats)==-1){
            alert("文件格式不支持, 请重新选择文件！");
            return;
        }
        var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
        var fileSize = 0;
        if (isIE && !tag.target.files) {
            var filePath = tag.target.value;
            var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
            var file = fileSystem.GetFile (filePath);
            fileSize = file.Size;
        } else {
            fileSize = tag.target.files[0].size;
        }
        var fsize = fileSize/1024/1024;
        if(size>0&&fsize>size){
            alert("文件不能大于"+size+"M");
            return ;
        }
        $('.bg').show();
        $('.d-load').show();
        $(loadingId).show();
        $.ajaxFileUpload({
            url: url, //用于文件上传的服务器端请求地址
            secureuri: true, //是否需要安全协议，一般设置为false
            fileElementId: id, //文件上传域的ID
            dataType: 'json', //返回值类型 一般设置为json
            success: function (rep, status)  //服务器成功响应处理函数
            {
                if(!rep){
                    $(loadingId).hide();
                    $('.bg').hide();
                    $('.d-load').hide();
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    $(loadingId).hide();
                    $('.bg').hide();
                    $('.d-load').hide();
                    console.log("数据解析错误!");
                    return;
                }
                $(loadingId).hide();
                $('.bg').hide();
                $('.d-load').hide();
                callback.apply(this,arguments);
            },
            error: function (data, status, e)//服务器响应失败处理函数
            {
                $(loadingId).hide();
                $('.bg').hide();
                $('.d-load').hide();
                alert(e);
            }
        });
    };

    /**
     * 异步获取
     * @param url
     * @param data
     * @param callback
     */
    Common.getDataAsync = function(url,data,callback){
        //var encodeUrl = encodeURIComponent(url);
        $.ajax({
            type: "GET",
            data:data,
            url: url,
            async: true,
            cache : false,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            success: function(rep){
                if(!rep){
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };

    /**
     * @func getPostData
     * @desc 同步获取数据
     * @param {string} url 请求的地址
     * @param {object} data 请求的参数
     * @param {function} callback 回调函数
     * @returns {boolean}
     * @example
     * Common.getPostData("/test",“{}”,function(data){})
     */
    Common.getPostData = function(url,data,callback){
        //var encodeUrl = encodeURIComponent(url);
        $.ajax({
            type: "post",
            data:data,
            url: url,
            async: false,
            cache : false,
            dataType: "json",
            traditional :true,
            success: function(rep){
                if(!rep){
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };

    Common.getPostBodyData = function(url,data,callback){
        $.ajax({
            type: "post",
            data:JSON.stringify(data),
            url: url,
            async: false,
            cache : false,
            contentType: 'application/json',
            success: function(rep){
                if(!rep){
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };

    /**
     * 异步获取
     * @param url
     * @param data
     * @param callback
     */
    Common.postDataAsync = function(url,data,callback){
        //var encodeUrl = encodeURIComponent(url);
        $.ajax({
            type: "post",
            data:data,
            url: url,
            async: true,
            cache : false,
            dataType: "json",
            traditional :true,
            success: function(rep){
                if(!rep){
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };

    Common.deleteDataAsync = function(url,data,callback){
        //var encodeUrl = encodeURIComponent(url);
        //data._method = 'delete';
        $.ajax({
            type: "post",
            data:data,
            url: url,
            async: true,
            cache : false,
            dataType: "json",
            traditional :true,
            success: function(rep){
                if(!rep){
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };

    Common.putDataAsync = function(url,data,callback){
        //var encodeUrl = encodeURIComponent(url);
        //data._method = 'put';
        $.ajax({
            type: "post",
            data:data,
            url: url,
            async: true,
            cache : false,
            dataType: "json",
            traditional :true,
            success: function(rep){
                if(!rep){
                    //$("#pageLoading").hide();
                    return rep;
                }
                try{
                    $.isPlainObject(rep);
                }catch(e){
                    console.log("数据解析错误!");
                    return;
                }
                callback.apply(this,arguments);
            }
        });

    };

    /**
     * @func fileUpload
     * @desc 异步上传附件
     * @param {string} id 出发上传的id
     * @param {string} url 异步请求的地址
     * @param {object} data 请求的参数
     * @param {function} callback 回调函数
     * @returns {boolean}
     * @example
     * Common.fileUpload(id, "/test",function(data){})
     */
    Common.fileUpload = function(id,url,loadingId,callback){
        $(id).fileupload({
            url: url,
            paramName: 'file',
            start: function(e) {
                $(loadingId).show();
            },
            done: function (e,response) {
                //低版本IE获取text/plain的数据解析
                callback.apply(this,arguments);
            },
            always: function (e, data) {
                $(loadingId).hide();
            }
        });
    }

    /**
     * @func eva
     * @desc 通用的将字符串转为json
     * @param {string} str
     * @returns {object}
     */
    Common.eva = function(str){
        try{
            return eval(str);
        }catch(e){
            return {};
        }
    };

    /**
     * @func goTo
     * @desc 跳转页面
     * @param {string} url
     * @returns {object}
     */
    Common.goTo = function(url){
        location.href = url;
    };

    /**
     * @func render
     * @desc 页面渲染
     * @param {object} cfg
     * @param {object} cfg.tmpl dotjs的模板对象
     * @param {object} cfg.data 渲染模板所需要数据
     * @param {string} cfg.context 渲染的模板将被插入的容器选择器
     * @param {function} cfg.callback 渲染完成的回调方法
     * @param {boolean} [cfg.overwrite] 是否清空容器原有内容 默认不清空
     * @example
     * Common.render(cfg);
     */
    Common.render = function(cfg){
        var self = this,
            _data = cfg.data,
            dom = '',
            context = cfg.context,
            callback = cfg.callback,
            _tmpl = doT.template($(cfg.tmpl).html());
        if(cfg.overwrite){
            $(context).empty();
        }
        if(_tmpl){
            dom = self.renderHtml($.trim(_tmpl(_data)),context);
        }else{
            console.log("对应的模块不存在!");
        }
        callback && callback.call(this,{data: _data,dom:dom});

    };

    /**
     * @func renderHtml
     * @desc 将tmpl插入到context里
     * @param {string} tmpl 模板生成的html字符串
     * @param {string} context 选择器
     */
    Common.renderHtml = function(tmpl,context){
        var contentEl = $(context);
        return $(tmpl).appendTo(contentEl);
    };

    /**
     * @func send
     * @desc 创建一个图片用来发送
     * @param url {string} 发送url
     */
    Common.send= function(url) {
        var img = new Image(),
            id = "__img__" + Math.random();
        window[id] = img;
        img.onload = img.onerror = function () {
            window[id] = null;
        };
        img.src = url;
        img = null;
    };

    /**
     * @func cal
     * @desc 调用日期插件
     * @param {string} id 模板生成的html字符串
     * @example
     * Common.cal('calId')
     */
    Common.cal = function(id){
        WdatePicker({
            eCont:id,
            onpicked:function(dp){
                Common.goTo('/calendar/day?date='+dp.cal.getDateStr()+'#eightclock');
            }
        })
    };

    /**
     * @func leftNavSel
     * @desc 左边菜单导航切换效果
     * @example
     * Common.leftNavSel()
     */

    Common.leftNavSel = function(){
        Common.getData('/school/navs/load.do',null,function(rep){

            if(rep!=null && rep.length!=0) {
                $('.left-nav').empty();
                //$('.left-nav').append("<li class='homepage-left-one copy-two' id='nav_left_1'><img src='/static_new/images/navv_left_16.png'> <span><a href='/user/homepage.do?index=1'>首页</a></span></li>");
                for (var i=0;i<rep.length;i++) {
                    var con = "";
                    
                    if(rep[i].dto.link)
                    {
                    	 con = "<li class='pre' id='"+rep[i].dto.id+"'><img src='"+rep[i].dto.image+"'> <span><a href='"+rep[i].dto.link+"'>"+rep[i].dto.name+"</a></span></li><dl>";
                    }
                    else
                    {
                    	 con = "<li class='er' id='"+rep[i].dto.id+"'><img src='"+rep[i].dto.image+"'> <span><a href='#'>"+rep[i].dto.name+"</a></span><s class='iconfont'>&#xe61c;</s></li><dl>";
                    }
                   
                    if (rep[i].list!=null && rep[i].list.length!=0) {
                        for (var j=0;j<rep[i].list.length;j++) {
                            if (rep[i].list[j].name=="党建管理") {
                                con += "<a href='"+rep[i].list[j].link+"' id='"+rep[i].list[j].id+"' target='_blank'><dt>"+rep[i].list[j].name+"</dt></a>";
                            } else {
                                con += "<a href='"+rep[i].list[j].link+"' id='"+rep[i].list[j].id+"'><dt>"+rep[i].list[j].name+"</dt></a>";
                            }
                        }
                    }
                    con+="</dl>";
                    $('.left-nav').append(con);
                }

            }
        });
        //
        var version = $("#versionValue").val();
        var versionIdx =jQuery("#index_Value").val();

        $("#nav_left_" + versionIdx).removeClass('er');
        $("#nav_left_" + versionIdx).addClass('current');
        
        if (versionIdx && version) 
        {
              jQuery("#version_" +versionIdx+ "_"+version).css("color", "red");
        }

        $("#nav_left_" + versionIdx).next("dl").slideDown(400);

/*        $(".left-nav li.er").click(function(event) {
            $(this).toggleClass('re').siblings().removeClass('re').next("dl").slideUp(400);
            $(".left-nav li.er").children('s').html("&#xe61c;");
            $(".left-nav li.re").children('s').html("&#xe607;");
            $(this).next("dl").slideToggle(400);

            //$(".left-nav dl a").click(function(event) {
            //    $(this)css({"color": '#ff8a00'}).siblings().css({"color": '#666'
            //    }).parent("dl").siblings().children(".left-nav dl a").css({"color": '#666'});
            //    $(this).parent("dl").prev().addClass('current').siblings().removeClass('current');
            //});

        });*/

        /*new*/

        $(".left-nav li.er").click(function(event) {
            $(this).toggleClass('re').siblings().removeClass('re').next("dl").slideUp(400);
            $(".left-nav li.er").children('s').html("&#xe61c;");
            $(".left-nav li.re").children('s').html("&#xe607;");
            $('.left-nav li.current').addClass('ll');
            $(".left-nav li.ll").children('s').html("&#xe61c;");
            $(this).next("dl").slideToggle(400);
        });
        $(".left-nav li.current").click(function(event) {
            $(".left-nav li.re").children('s').html("&#xe61c;");
            $(this).toggleClass('ll').siblings().removeClass('re').next("dl").slideUp(400);
            $(this).children('s').html("&#xe607;");
            $(".ll").children('s').html("&#xe61c;");
            $(this).next("dl").slideToggle(400);
        });
        /*new*/

        $(".left-nav li#pre").click(function(event) {
            $(this).addClass('current').siblings().removeClass('current');
            $(".left-nav dl a").css({"color": '#666'});
        });

        /*右侧头部*/
        $(".user-cener").hover(function() {
            $(this).children('ul').css({
                "display": 'block'
            });
        }, function() {
            $(this).children('ul').css({
                "display": 'none'
            });
        });

        //私信数目
        Common.getData("/letter/count.do",null,function (resp) {
            if (resp) {
                if (resp > 0) {
                    $('a.user-msg').html('<i>'+resp+'</i>私信');
                } else {
                    $('a.user-msg').html('私信');
                }
            }
        })


        //同学排行
        Common.getDataAsync('/user/studenttopfive', null, function (resp) {
        	
        	if(resp.length==0)
        	{
        		 jQuery("#top5Container").hide();
        	}
        	else
        	{
		            Common.render({
		                tmpl: '#top5studenttemp',
		                data: resp,
		                context: '#top5studentcontainner',
		                overwrite: 1
		            });
        	}
        });
    }


    /**
     * @func tab
     * @desc tab切换效果
     * @param type {string} 事件类型
     * @param obj jquery对象
     * @example
     * Common.tab('click',$('.tab-head-new'));
     */
    Common.tab = function(type,obj){
        obj.find('li').on(type,function(){
            var curLi = $(this).addClass('cur')
            curLi.siblings('li').removeClass('cur');
            $(curLi.find('a').attr('href')).show().siblings().hide();
            return false;
        });
    };

    /**
     * @func dialog
     * @desc 模拟弹窗
     * @param {jQuery obj} jQuery
     * @example
     * Common.dialog(jQuery)
     */

    Common.dialog = function(obj,callback){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        if(callback){
            callback.apply(this,arguments[0]);
            obj.find('.pop-btn span:not(".active")').click(function(){
                Common.dialog.close($(this));
            });
        }else{
            obj.find('.pop-btn span').click(function(){
                Common.dialog.close($(this));
            });
        }
        $('body').scrollTop(0);
    };

    Common.dialog.close = function(obj){
        $(obj).closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    };

    
    Common.userData=function(pageTag,callback)
    {
        Common.getDataAsync('/udc/data.do', {"pageTag":pageTag}, function (resp) {
        	 callback.apply(this,arguments);
        });
    }
    
    
    module.exports = Common;

})