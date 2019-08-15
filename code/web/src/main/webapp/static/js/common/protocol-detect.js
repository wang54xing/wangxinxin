/**
 * Created by Niu Xin on 14/10/30.
 */

//Default State
var isSupported = false;

function skipCheckProtocol(sender) {
    //MessageBox("如果录课神器未启动，请点击 <a href=\"/upload/resources/recorder.exe\" style=\"color:red;\">这里</a> 下载安装。", -1);
    location.href = sender;
    checkUploadStatus();
}

function getUrl(sender){
    return sender.getAttribute('link');
}

function result(sender){
    if(!isSupported) {
        ConfirmDialog('您还没有安装“录课神器”，是否现在安装？', 'location.href = \'/upload/resources/recorder.exe\';$(\'#prompt-div\').remove();');
    } else {
        checkUploadStatus();
    }
}

function checkUploadStatus() {
    //MessageBox("等待录课神器上传中...", 0);
    if (checkUploadByLongPolling.checking) {
        return;
    }
    checkUploadByLongPolling();
}

function checkUploadByLongPolling() {
    checkUploadByLongPolling.checking = true;
    $.ajax({
        url: '/lesson/getUploadStatus.do',
        data: {'lessonId': courseId},
        cache: false,
        success: function(response) {
            checkUploadByLongPolling.checking = false;
            MessageBox("录课神器上传完毕！", 1);
            getLessonVideos();
        },
        error: function(err) {
            checkUploadByLongPolling();
        }
    });
}


/*
 function downloadURL(url) {
 var hiddenIFrameID = 'hiddenDownloader',
 iframe = document.getElementById(hiddenIFrameID);
 if (iframe === null) {
 iframe = document.createElement('iframe');
 iframe.id = hiddenIFrameID;
 iframe.style.display = 'none';
 document.body.appendChild(iframe);
 }
 iframe.src = url;
 }
 */
//Handle Click on Launch button
function checkProtocol(sender){
    if(bowser.firefox){
        launchMozilla(sender);
    }else if(bowser.chrome){
        launchChrome(sender);
    }else if(bowser.msie){
        launchIE(sender);
    } else {
        MessageBox('此功能只能在PC下使用。', -1);
    }
}

//Handle IE
function launchIE(sender){

    var url = getUrl(sender),
        aLink = $('#hiddenLink')[0];

    isSupported = false;
    aLink.href = url;

    //Case 1: protcolLong
    //console.log("Case 1");
    if(navigator.appName=="Microsoft Internet Explorer"
        && aLink.protocolLong=="Unknown Protocol"){
        isSupported = false;
        result(sender);
        return;
    }

    //IE10+
    if(navigator.msLaunchUri){
        navigator.msLaunchUri(url,
            function(){ isSupported = true; result(sender); location.href = sender.getAttribute('link');}, //success
            function(){ isSupported=false; result(sender);  }  //failure
        );
        return;
    }

    //Case2: Open New Window, set iframe src, and access the location.href
    //console.log("Case 2");
    var myWindow = window.open('','','width=0,height=0');
    myWindow.document.write("<iframe src='"+ url + "></iframe>");
    setTimeout(function(){
        try{
            myWindow.location.href;
            isSupported = true;
        }catch(e){
            //Handle Exception
        }

        if(isSupported){
            myWindow.setTimeout('window.close()', 100);
            location.href = sender.getAttribute('link');
        }else{
            myWindow.close();
        }
        result(sender);
    }, 100)

};

//Handle Firefox
function launchMozilla(sender){

    var url = getUrl(sender),
        iFrame = $('#hiddenIframe')[0];

    isSupported = false;

    //Set iframe.src and handle exception
    try{
        iFrame.contentWindow.location.href = url;
        isSupported = true;
        result(sender);
    }catch(e){
        //FireFox
        if (e.name == "NS_ERROR_UNKNOWN_PROTOCOL"){
            isSupported = false;
            result(sender);
        }
    }
}

//Handle Chrome
function launchChrome(sender){

    var url = getUrl(sender),
        protcolEl = $('#protocol')[0];

    isSupported = false;


    protcolEl.focus();
    protcolEl.onblur = function(){
        isSupported = true;
        //console.log("Text Field onblur called");
    };

    //will trigger onblur
    location.href = url;

    //Note: timeout could vary as per the browser version, have a higher value
    setTimeout(function(){
        protcolEl.onblur = null;
        result(sender);
        location.href = sender.getAttribute('link');
    }, 1000);

}