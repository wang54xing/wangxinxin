/* global Config */
define(function(require,exports,module){
	function fileUpload(sfn,efn){
	    (function($){
	        //文件上传
	        var $list = $('#thelist'),
	            $btn = $('#ctlBtn'),
	            state = 'pending',
	            uploader;

	        uploader = WebUploader.create({
	        	//验证文件总数量
	            fileNumLimit:1,
	            // 不压缩image
	            resize: false,

	            // swf文件路径
	            swf: '../js/Uploader.swf',

	            // 文件接收服务端。
	            server: '/file1/upload.do',

	            // 选择文件的按钮。可选。
	            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	            pick: '#picker'
	        });

	        // 当有文件添加进来的时候
	        uploader.on( 'fileQueued', function( file ) {
	            $list.html( '<div id="' + file.id + '" class="item">' +
	                '<h4 class="info">' + file.name + '</h4>' +
	                '<p class="state">等待上传...</p>' +
	                '</div>' );
	        });

	        // 文件上传过程中创建进度条实时显示。
	        uploader.on( 'uploadProgress', function( file, percentage ) {
	            var $li = $( '#'+file.id ),
	                $percent = $li.find('.progress .progress-bar');

	            // 避免重复创建
	            if ( !$percent.length ) {
	                $percent = $('<div class="progress progress-striped active">' +
	                    '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	                    '</div>' +
	                    '</div>').appendTo( $li ).find('.progress-bar');
	            }

	            $li.find('p.state').text('上传中');

	            $percent.css( 'width', percentage * 100 + '%' );
	        });

	        uploader.on( 'uploadSuccess', function( file ,response) { 
	        	sfn && sfn(response,file.name);
	            $( '#'+file.id ).find('p.state').text('已上传');
	        });

	        uploader.on( 'uploadError', function( file ,response) {
	            $( '#'+file.id ).find('p.state').text('上传出错');
	            efn && efn(response);
	        });

	        uploader.on( 'uploadComplete', function( file ) {
	            $( '#'+file.id ).find('.progress').fadeOut();
	        });

	        uploader.on( 'all', function( type ) {
	            if ( type === 'startUpload' ) {
	                state = 'uploading';
	            } else if ( type === 'stopUpload' ) {
	                state = 'paused';
	            } else if ( type === 'uploadFinished' ) {
	                state = 'done';
	            }

	            if ( state === 'uploading' ) {
	                $btn.text('暂停上传');
	            } else {
	                $btn.text('开始上传');
	            }
	        });

	        $btn.on( 'click', function() {
	            if ( state === 'uploading' ) {
	                uploader.stop();
	            } else {
	                uploader.upload();
	            }
	        });
	    })(jQuery)
	}

	function getClassNameByFileName(name){
		var suffixClass = "icon-default";
		var suffix = name.split(".")[name.split(".").length -1];
		if("avi/mp4/mpg/flv/wmv/mov/mkv".indexOf(suffix) > -1){
			suffixClass = "icon-avi";
			return suffixClass;
		}
		if(suffix == "pdf"){
			suffixClass = "icon-pdf";
			return suffixClass;
		}
		if(suffix == "doc" || suffix == "docx"){
			suffixClass = "icon-doc";
			return suffixClass;
		}
		if(suffix == "xls" || suffix == "xlsx"){
			suffixClass = "icon-xls";
			return suffixClass;
		}
		if(suffix == "jpg" || suffix == "png" || suffix == "png"){
			suffixClass = "icon-jpg";
			return suffixClass;
		}
		if(suffix == "ppt"){
			suffixClass = "icon-ppt";
			return suffixClass;
		}
		if(suffix == "mp3"){
			suffixClass = "icon-mp3";
		}
		return suffixClass;
	}
    module.exports = fileUpload;
})