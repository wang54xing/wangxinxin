$(function() {
    if(typeof currentPageID!= 'undefined')
    {
        $('#menu' + currentPageID).addClass('current');
    }
    $("textarea[maxlength]").bind('input propertychange', function() {
        var maxLength = $(this).attr('maxlength');
        if ($(this).val().length > maxLength) {
            $(this).val($(this).val().substring(0, maxLength));
        }
    });
});

function request(paras){
    var url = location.href;
    var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");
    var paraObj = {};
    for (var i=0; j=paraString[i]; i++){
        paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if(typeof(returnValue)=="undefined"){
        return "";
    }else{
        return returnValue;
    }
}

function FormatNumberLength(num, length) {
    var r = "" + num;
    while (r.length < length) {
        r = "0" + r;
    }
    return r;
}

function FormatTimeString(len){
    var seconds = Math.floor(len/1000);
    var h = Math.floor(seconds / 3600);
    var m = Math.floor(seconds % 3600 / 60);
    var s = seconds % 60;
    return FormatNumberLength(h, 2) + ':' + FormatNumberLength(m, 2) + ':' + FormatNumberLength(s, 2);
}

function CheckFlashPlugin(){
    if (!(typeof swfobject !== 'undefined' && swfobject.hasFlashPlayerVersion("8"))) {
        var inform = '<div style="height:500px;line-height:500px;width:900px;background:black;color:white;font-size:20px;text-align:center">';
        var userAgent = window.navigator.userAgent;
        if (userAgent.indexOf('iPod') !== -1 || userAgent.indexOf('iPad') !== -1 || userAgent.indexOf('iPhone') !== -1) {
            inform += '您的系统不支持Flash，请使用PC观看。</div>';
        }
        else {
            inform += '请点击<a style="color:#06a7e1" target="_blank" href="http://www.adobe.com/go/getflash">这里</a>安装最新版Flash插件</div>';
        }
        $("#videoplayer-div").html(inform);
    }
}

function closeDialog(selector) {
    $("#fullbg").fadeOut();
    $('#bg').fadeOut();
    $(selector).fadeOut();
}

function showDialog(selector, tohide) {
    $(selector).css('z-index', 50);
    $(selector).fadeIn();
    $("#fullbg").show();
    if(tohide){
        $(tohide).css('z-index', 20);
    }
}

function closeParentDialog(selector, type, retype){
	if(type == 'push' || type == 'gotolook'){
		$(selector).parents('#prompt-div').fadeOut();
	}else if(type == 'save'){
		if (retype==2) {
			location.href = '/class/course';
		} else if (retype==3) {
			location.href = '/excellentLesson';
		} else {
			location.href = '/lesson/teacher.do';
		}
	} else if(type == 'savehomework') {
        location.href = '/homework/teacher.do';
    }
}

function fileUpload(form, callback) {
    // Create the iframe...
    var iframe = document.createElement("iframe");
    iframe.setAttribute("id", "upload_iframe");
    iframe.setAttribute("name", "upload_iframe");
    iframe.setAttribute("width", "0");
    iframe.setAttribute("height", "0");
    iframe.setAttribute("border", "0");
    iframe.setAttribute("style", "width: 0; height: 0; border: none;");

    // Add to document...
    form.parentNode.appendChild(iframe);
    window.frames['upload_iframe'].name = "upload_iframe";

    var iframeId = document.getElementById("upload_iframe");

    // Add event...
    var eventHandler = function () {

        if (iframeId.detachEvent) iframeId.detachEvent("onload", eventHandler);
        else iframeId.removeEventListener("load", eventHandler, false);
        var content;

        // Message from server...
        if (iframeId.contentDocument) {
            content = iframeId.contentDocument.body.innerHTML;
        } else if (iframeId.contentWindow) {
            content = iframeId.contentWindow.document.body.innerHTML;
        } else if (iframeId.document) {
            content = iframeId.document.body.innerHTML;
        }

        //document.getElementById(div_id).innerHTML = content;
        callback(content);

        // Del the iframe...
        setTimeout(function(){iframeId.parentNode.removeChild(iframeId);}, 250);
    }

    if (iframeId.addEventListener) iframeId.addEventListener("load", eventHandler, true);
    if (iframeId.attachEvent) iframeId.attachEvent("onload", eventHandler);

    // Set properties of form...
    form.setAttribute("target", "upload_iframe");
    //form.setAttribute("action", action_url);
    form.setAttribute("method", "post");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("encoding", "multipart/form-data");

    // Submit the form...
    form.submit();

    //document.getElementById(div_id).innerHTML = "Uploading...";
}

function ShowPromptDialog(prompt, notClose){
    $("#prompt-div").remove();
    var pdiv = '<div id="prompt-div">';

    pdiv += '<div class="message-box">';
    pdiv += '<div class="message-box-title">提示';
    if(!notClose) {
        pdiv += '<i class="fa fa-times fa-lg" style="margin-top:3px;color:white;vertical-align: middle;float: right;cursor: pointer" onclick="clearTimeout(autoclose_id);$(\'#prompt-div\').remove();"></i>';
        autoclose_id = setTimeout(ClosePromptDialog, 3000);
    }
    pdiv += '</div><div>';
    pdiv += prompt;
    pdiv += '</div></div>';
    $("body").append(pdiv);
    $("#prompt-div").fadeIn();
}
var retype;
function ClosePromptDialog() {
    $("#prompt-div").fadeOut('fast',function(){
        $("#prompt-div").remove();
        if ($('#retype').val()==3 && retype==1) {//$('#retype').val()  url上的type,retype是精品课程消失框保存按钮
        	location.href = '/excellentLesson?type=1';
        }
    });
}

function MessageBox(message, type, msgtype, text,excltype) {
    var icon;
    var notClose = false;
    var txt = text;
    retype = excltype;
    switch (type) {
        case 0://loading
            icon = '<i class="fa fa-spinner fa-spin fa-4x" style="color:deepskyblue;vertical-align: middle; margin-right: 30px"></i>';
            notClose = true;
            break;
        case 1://成功
            icon = '<i class="fa fa-check-square fa-4x" style="color:deepskyblue;vertical-align: middle; margin-right: 30px"></i>';
            break;
        case -1://失败
            icon = '<i class="fa fa-warning fa-4x" style="color:darkorange;vertical-align: middle; margin-right: 30px"></i>';
            break;
        case 2://下次不再提醒
        	icon ='<i class="fa fa-check-square fa-4x" style="color:deepskyblue;vertical-align: middle; margin-right: 30px"></i>';
        	notClose = true;
        	break;
        case 3://去看看&我知道了
        	icon ='';
        	notClose = true;
        	break;
        default :
            icon = '';
    }
    if(!txt){
    	txt = '';
    }else{
    	if(msgtype == 'save' ||msgtype == 'savehomework'){//保存
    		txt = '<div style="text-align:left;text-indent:2em;padding:14px 10px;line-height:22px;"><p>'+ txt +'</p></div>';
        	txt = txt + '<hr style="margin:10px -20px;"/><div style="text-align: left;margin-left: 22px;"><input type="checkbox" value="" id="notShow" onchange="notShowAgain(\''+ msgtype +'\')" style="margin-left: 180px;vertical-align: middle;"/><label for="notShow" style="vertical-align: middle;">下次不再提醒</label><button class="getit" onclick="closeParentDialog(this,\''+ msgtype +'\')">我知道了</button></div>';
    	}else if(msgtype == 'gotolook'){//去看看录课神器
    		txt = '<div style="text-align:left;text-indent:2em;padding:14px 10px;line-height:22px;"><p>'+ txt +'</p></div>';
        	txt = txt + '<hr style="margin:10px -20px;"/><div style="text-align: left;margin-left: 22px;"><button onclick="gotoLook()">去看看</button><button class="getit cancel" onclick="closeParentDialog(this,\''+ msgtype +'\')">我知道了</button></div>';
    	}if(msgtype == 'push'){//保存
    		txt = '<div style="text-align:left;text-indent:2em;padding:14px 10px;line-height:22px;"><p>'+ txt +'</p></div>';
        	txt = txt + '<hr style="margin:10px -20px;"/><div style="text-align: left;margin-left: 22px;"><input type="checkbox" value="" id="notShow" onchange="notShowAgain(\''+ msgtype +'\')" style="margin:0;margin-left: 180px;vertical-align: middle;"/><label for="notShow" style="vertical-align: middle;margin-bottom: 0;">下次不再提醒</label><button class="getit" onclick="closeParentDialog(this,\''+ msgtype +'\')">我知道了</button></div>';
    	}
    }
    ShowPromptDialog(icon + message + txt, notClose);
}
function gotoLook(){
	location.href = '/cloudClass';
}
function ConfirmDialog(msg, operation) {
    var message = '<p style="margin-bottom: 30px"><i class="fa fa-question-circle fa-4x" style="color:deepskyblue;vertical-align: middle; margin-right: 30px"></i>' + msg + '</p>';
    message += '<input onclick="' + operation + '" type="button" class="submit-button" value="确定" style="font-size:13px; width:80px"/>';
    message += '<input onclick="$(\'#prompt-div\').remove()" type="button" class="submit-button" value="取消" style="margin-left:20px;background: gray;width: 80px; font-size:13px"/>';

    $("#prompt-div").remove();
    var pdiv = '<div id="prompt-div">';

    pdiv += '<div class="message-box">';
    pdiv += '<div class="message-box-title">确认操作';
    pdiv += '<i class="fa fa-times fa-lg" style="margin-top:3px;color:white;vertical-align: middle;float: right;cursor: pointer" onclick="$(\'#prompt-div\').remove()"></i>';
    pdiv += '</div><div>';
    pdiv += message;
    pdiv += '</div></div>';
    $("body").append(pdiv);
    $("#prompt-div").fadeIn();
}

function getPlayer(movieName) {
    if (navigator.appName.indexOf("Microsoft") != -1) {
        var reObj=window[movieName];
        try{
            if(reObj.length>0){
                return reObj[0];
            }
            else{
                return reObj;
            }
        }catch(e){

        }
        return document[movieName];
    } else if (navigator.appName.indexOf("Netscape") != -1) {
        var reObj=document[movieName];
        try{
            if(reObj.length>0){
                return reObj[1];
            }
            else{
                return reObj;
            }
        }catch(e){

        }
        return document[movieName];
    }
    else {
        return document[movieName];
    }
}

//取得cookie
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');    //把cookie分割成组
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];                      //取得字符串
        while (c.charAt(0)==' ') {          //判断一下字符串有没有前导空格
            c = c.substring(1,c.length);      //有的话，从第二位开始取
        }
        if (c.indexOf(nameEQ) == 0) {       //如果含有我们要的name
            return c.substring(nameEQ.length,c.length);    //解码并截取我们要值
        }
    }
    return false;
}

//清除cookie
function clearCookie(name) {
    setCookie(name, "", -1);
}

//设置cookie
function setCookie(name, value, seconds) {
    seconds = seconds || 0;   //seconds有值就直接赋值，没有为0，这个根php不一样。
    var expires = "";
    if (seconds != 0 ) {      //设置cookie生存时间
        var date = new Date();
        date.setTime(date.getTime()+(seconds*1000));
        expires = "; expires="+date.toGMTString();
    }
    document.cookie = name+"="+value+expires+"; path=/";   //转码并赋值
}

function notShowAgain(msgtype){
	if($("#notShow").is(':checked')){
		if(msgtype == 'push'){
			setCookie('pushclass','pushT','30*24*60*60*1000');
		}else if(msgtype == 'save'){
			setCookie('saveclass','saveT','30*24*60*60*1000');
		} else if(msgtype == 'savehomework'){
            setCookie('savehomework','saveT','30*24*60*60*1000');
        }

	}
}

function hideBrowserUpdater() {
    $('.browser-updater').hide();
}

String.prototype.startWith=function(str){
    var reg=new RegExp("^"+str);
    return reg.test(this);
}

String.prototype.endWith=function(str){
    var reg=new RegExp(str+"$");
    return reg.test(this);
}

String.prototype.lengthInBytes = function () {
    // Matches only the 10.. bytes that are non-initial characters in a multi-byte sequence.
    var m = encodeURIComponent(this).match(/%[89ABab]/g);
    return this.length + (m ? m.length : 0);
}