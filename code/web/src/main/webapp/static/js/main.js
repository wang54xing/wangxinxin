$(function() {
  var $uploadStatus = $('#upload_status'),
    $showLevelButton = $('.show_level'),
    $hideLevelButton = $('.hide_level'),
    $level = $('.control_panel .level');


  window.microphone_recorder_events = function microphone_recorder_events() {
    $('#status').text("Microphone recorder event: " + arguments[0]);
    var name, $controls;
    switch(arguments[0]) {
      case "ready":
        var width = parseInt(arguments[1]);
        var height = parseInt(arguments[2]);
		var appName = arguments[3];
        FWRecorder.uploadFormId = "#uploadForm";
        FWRecorder.uploadFieldName = "upload_file[filename]";
        FWRecorder.connect(appName, 0);
        FWRecorder.recorderOriginalWidth = width;
        FWRecorder.recorderOriginalHeight = height;
        $('.save_button').css({'width': width, 'height': height});
        break;

      case "no_microphone_found":
        break;

      case "microphone_user_request":
        FWRecorder.showPermissionWindow();
        break;

      case "microphone_connected":
        var mic = arguments[1];
        FWRecorder.defaultSize();
        FWRecorder.isReady = true;
//        if(configureMicrophone) {
//          configureMicrophone();
//        }
        recorderApp.startRecord();
        $uploadStatus.css({'color': '#000'}).text("Microphone: " + mic.name);
        FWRecorder.record('audio', 'audio.wav');
        break;

      case "microphone_not_connected":
        FWRecorder.defaultSize();
        break;

      case "microphone_activity":
        $('#activity_level').text(arguments[1]);
        break;

      case "recording":
        name = arguments[1];
        $controls = controlsEl(name);
        FWRecorder.hide();
        $controls.find('.record_button img').attr('src', '/business/images/stop.png');
        $controls.find('.record_button').attr('title', '点击结束录音');
        $controls.find('.play_button').hide();
        break;

      case "recording_stopped":
        name = arguments[1];
        $controls = controlsEl(name);
        var duration = arguments[2];
        FWRecorder.show();
        $controls.find('.record_button img').attr('src', '/business/images/recorder.png');
        $controls.find('.play_button img').attr('src', '/business/images/play.png');
        $controls.find('.record_button').attr('title', '点击开始录音');
        $('#duration').text(duration.toFixed(4) + " seconds");
        $controls.find('.play_button').show();
        break;

      case "microphone_level":
        $level.css({width: arguments[1] * 50 + '%'});
        break;

      case "observing_level":
        $showLevelButton.hide();
        $hideLevelButton.show();
        break;

      case "observing_level_stopped":
        $showLevelButton.show();
        $hideLevelButton.hide();
        $level.css({width: 0});
        break;

      case "playing":
        name = arguments[1];
        $controls = controlsEl(name);
        $controls.find('.record_button img').attr('src', '/business/images/recorder.png');
        $controls.find('.play_button img').attr('src', '/business/images/pause.png');
        $controls.find('.pause_button').show();
        break;

      case "playback_started":
        name = arguments[1];
        var latency = arguments[2];
        break;

      case "stopped":
        name = arguments[1];
        $controls = controlsEl(name);
        $controls.find('.record_button img').attr('src', '/business/images/recorder.png');
        $controls.find('.play_button img').attr('src', '/business/images/play.png');
        $controls.find('.pause_button').hide();
        break;

      case "save_pressed":
    	$("#uploadvoiceloading").show();
        FWRecorder.updateForm();
        break;

      case "saving":
        name = arguments[1];
        break;

      case "saved":
    	 
          //如果是投票选举提交语音
          var voiceUrlStr = arguments[2];
 
          var locationStr=window.location.href;
          if(locationStr.indexOf("notice")>0) //如果是增加通知的话
          {
        	  var noticeInfo=JSON.parse(voiceUrlStr);
        	  var voicePath=JSON.parse(voiceUrlStr).message[0].path;
        	  
        	  
        	  
        	  
        	  newVoice(voicePath).appendTo(jQuery("#voice_notice"));
           
        	  
        	  
        	  jQuery("#voice_notice").show();
        	  $('#recordflash').hide();
              $('.sanjiao').hide();
              $("div[id^=myContent]").hide();
        	  return;
          } else {
              var noticeInfo=JSON.parse(voiceUrlStr);
              var voicePath=JSON.parse(voiceUrlStr).message[0].path;
              homework(voicePath);
              $('#recordflash').hide();
              $('.sanjiao').hide();
              $("div[id^=myContent]").hide();
              return;
          }
          
          if (voiceUrlStr) {
              var voiceUrl = JSON.parse(voiceUrlStr)[0];
              if (voiceUrl.match(/\/elect-/)) {
                  var voiceContainer = $('#recorderApp').closest('.tp-bm-box').find('.vote-audio-container ul');
                  voiceContainer.html('');
                  newVoice(voiceUrl).appendTo(voiceContainer);
                  FWRecorder.hide();
                  $('#recordflash').hide();
                  $('.sanjiao').hide();
                  $("div[id^=myContent]").hide();
                  break;
              }

              else{ //其他地方的音频 by autman
            	  
                  if( $('.voice').length > 0 ){
                      $('.voice').remove();
                  }
                  if($('#reply_section').length > 0){
                      var voiceContainer =  $('#reply_section');
                  }
                  //如果是老师提交语音
                  else if($('#submit_section').length > 0){
                      var voiceContainer =  $('#submit_section');
                  }

                  newVoice(voiceUrl).appendTo(voiceContainer);
                  FWRecorder.hide();
                  $('.sanjiao').hide();
                  $("div[id^=myContent]").hide();


              }
          }

        //name = arguments[1];
        //FWRecorder.hide();
        //$("#uploadvoiceloading").hide();
        //$("div[id^=myContent]").hide();
        //$('.sanjiao').hide();
        //var msgId = null;
        //var courseId = null;
	  //如果是学生提交语音

        	
        break;

      case "save_failed":
        name = arguments[1];
        var errorMessage = arguments[2];
        $uploadStatus.css({'color': '#F00'}).text(name + " failed: " + errorMessage);
        break;

      case "save_progress":
        name = arguments[1];
        var bytesLoaded = arguments[2];
        var bytesTotal = arguments[3];
        $uploadStatus.css({'color': '#000'}).text(name + " progress: " + bytesLoaded + " / " + bytesTotal);
        break;
    }
  };

  function controlsEl(name) {
    return $('.control_panel.'+name);
  }
});

function newVoice(url) {
    return $('<p><a class="voice" onclick="playVoice(\'' + url + '\');" url="' + url + '"><img src="/img/yuyin.png" style="width:160px;height:22px;">播放</a></p>').hover(function () {
        if ($(this).find('img[src="/img/dustbin.png"]').length == 0) {
            $(this).append("<a style=\"margin-left:20px;\" onclick=\"$(this).closest('p').remove();\"><img src=\"/img/dustbin.png\" /></a>");

        } else {
            $(this).find('img[src="/img/dustbin.png"]').show();
        }
    }, function () {
        $(this).find('img[src="/img/dustbin.png"]').hide();
    });

}

function homework(url) {
    $('.work-sign-ul').append('<li img="" pth="'+url+'" tp="3" nm=""><img src="/img/yuyin.png" style="width:160px;height:22px;"><i class="tab-x">删除</i></li>');
    /*关闭图片*/
    $(".tab-x").click(function(event) {
        $(this).parent().remove();
    });
}

function playVoice(url) {
    var player = '<embed src="' + url + '" voice width="0" height="0" autostart="true" />';
    $('embed[voice]').remove();
    $('body').append(player);
}