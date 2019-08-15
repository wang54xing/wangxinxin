ypxxutility = {};
ypxxutility.homepage = {};
ypxxutility.uiutility = function() {

}
ypxxutility.uiutility.template = function() {
	var tmp = ypxxutility.uiutility.template;
	tmp.list = {};
	tmp.addTemplate = function(name, tp) {
		if (typeof tmp[name] == 'undefined') {
			tmp[name] = tp;
			tmp.list[name] = tp;
		}
	}
	tmp.removeTemplate = function(name) {
		if (typeof tmp[name] != 'undefined') {
			tmp[name] = undefined;
			tmp.list[name] = undefined;
		}
	}
}
ypxxutility.uiutility.tooltip = function(option) {
	var uty = ypxxutility.uiutility;
	var w = typeof option.width == 'undefined' ? 240 : option.width;
	var h = typeof option.height == 'undefined' ? 160 : option.height;
	var t = typeof option.left == 'undefined' ? 0 : option.left;
	var l = typeof option.top == 'undefined' ? 0 : option.top;
	if (uty.template['tooltip']) {
		//
	} else {
		uty.template.addTemplate('tooltip', '<div class="ypxxtip" sttyle="width:' + w + 'px;height:' + h + 'px;"><div class="t_title">图片上传<a style="float:right;">X</a></div></div>');
	}

	$('body').append($(uty.template['tooltip'])
		.css({
			position: 'absolute',
			top: t,
			left: l
		})
	);
}
ypxxutility.uiutility.centerShow = function(id) {
	$(id).css('position', 'fixed');
	$(id).css('left', '50%');
	$(id).css('top', '50%');
	$(id).css('margin-left', '-' + $(id).width() / 2 + 'px');
	$(id).css('margin-top', '-' + $(id).height() / 2 + 'px');
	showDialog(id);
}


ypxxutility.homepage.picswitch = function(container, option) {
	var startShow = ypxxutility.uiutility.centerShow;
	var pics = $(container).find('img');
	$('#fullbg').bind('click', function() {
		closeDialog('#picswitch');
		$('#picswitch').remove();
	});
	var width = typeof option.width == 'undefined' ? 600 : option.width;
	var height = typeof option.height == 'undefined' ? 400 : option.height;
	pics.each(function(i) {
		$(this).bind('click', function() {
			var showUI = $('<div id="picswitch" style="width:' + width + 'px;height:' + height + 'px;border-radius:4px;padding:20px;background:rgba(0,0,0,0.5);filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#5a5a5a,endColorstr=#5a5a5a);text-align:center;display:table;"><div class="picswitch"></div></div>');
			var size = checkSize(this.naturalWidth, this.naturalHeight);
			// var wTxt = this.naturalWidth > width ? '100%' : 'auto';
			// var hTxt=this.naturalHeight>height?'100%':'auto';
			// var hTxt = this.naturalHeight > height ? '400px' : 'auto';
			var wTxt = size.width;
			var hTxt = size.height;
			var newImg = $(this).clone().css({
				width: wTxt,
				height: hTxt
			}).removeClass('content-img').removeClass('homework-img').attr('title','');
			if($(this).hasClass('teacher-check')) {
				showUI.append('<button class="check-homeworkbtn">批改</button>');
			}
			showUI.find('.picswitch').append(newImg);
			showUI.append($('<a style="position: absolute;top:15px;right:15px;"><img src="/img/oldman_del.png"/></a>').click(function() {
				closeDialog(showUI);
				showUI.remove();
			}));
			var currentImg = $(this);
			showUI.append($('<a style="position: absolute;top:' + (height / 2) + 'px;right:20px"><img src="/img/next.png"/></a>').click(function() {
				if (currentImg.next().length > 0) {
					currentImg = currentImg.next();
					size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
					wTxt = size.width;
					hTxt = size.height;
					showUI.find('.picswitch').html(currentImg.clone().css({
						width: wTxt,
						height: hTxt
					}).removeClass('content-img').removeClass('homework-img').attr('title',''));
				}
			}));
			showUI.append($('<a style="position: absolute;top:' + (height / 2) + 'px;left:20px"><img src="/img/prev.png"/></a>').click(function() {

				if (currentImg.prev().length > 0) {
					currentImg = currentImg.prev();
					size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
					wTxt = size.width;
					hTxt = size.height;
					showUI.find('.picswitch').html(currentImg.clone().css({
						width: wTxt,
						height: hTxt
					}).removeClass('content-img').removeClass('homework-img').attr('title',''));
				}
			}));
			showUI.attr('tabindex', '10').bind('keydown', function(e) {
				if (e.KeyCode == 37) {
					if (currentImg.prev().length > 0) {
						currentImg = currentImg.prev();
						size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
						wTxt = size.width;
						hTxt = size.height;
						showUI.find('.picswitch').html(currentImg.clone().css({
							width: wTxt,
							height: hTxt
						}).removeClass('content-img').removeClass('homework-img').attr('title',''));
					}
				}
				if (e.KeyCode == 39) {
					if (currentImg.next().length > 0) {
						currentImg = currentImg.next();
						size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
						wTxt = size.width;
						hTxt = size.height;
						showUI.find('.picswitch').html(currentImg.clone().css({
							width: wTxt,
							height: hTxt
						}).removeClass('content-img').removeClass('homework-img').attr('title',''));
					}
				}
			});
			$('body').append(showUI);
			startShow(showUI);
			$(showUI).draggable();
		});
	});
}

ypxxutility.homepage.picswitch2 = function(container, option) {
    var startShow = ypxxutility.uiutility.centerShow;
    var pics = $(container).find('img');
    $('#fullbg').bind('click', function() {
        closeDialog('#picswitch');
        $('#picswitch').remove();
    });
    var width = typeof option.width == 'undefined' ? 600 : option.width;
    var height = typeof option.height == 'undefined' ? 400 : option.height;
    pics.each(function(i) {
        $(this).bind('click', function() {
            var showUI = $('<div id="picswitch" style="width:' + width + 'px;height:' + height + 'px;border-radius:4px;padding:20px;background:rgba(0,0,0,0.5);filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#5a5a5a,endColorstr=#5a5a5a);text-align:center;display:table;"><div class="picswitch"></div></div>');
            var size = checkSize(this.naturalWidth, this.naturalHeight);
            // var wTxt = this.naturalWidth > width ? '100%' : 'auto';
            // var hTxt=this.naturalHeight>height?'100%':'auto';
            // var hTxt = this.naturalHeight > height ? '400px' : 'auto';
            var wTxt = size.width;
            var hTxt = size.height;
            var newImg = $(this).clone().css({
                width: wTxt,
                height: hTxt
            }).removeClass('content-img').removeClass('homework-img').attr('title','');
            if($(this).hasClass('teacher-check')) {
                showUI.append('<button class="check-homeworkbtn">批改</button>');
            }
            showUI.find('.picswitch').append(newImg);
            showUI.append($('<a style="position: absolute;top:15px;right:15px;"><img src="/img/oldman_del.png"/></a>').click(function() {
                closeDialog(showUI);
                showUI.remove();
            }));
            var currentImg = $(this);
            showUI.append($('<a style="position: absolute;top:' + (height / 2) + 'px;right:20px"><img src="/img/next.png"/></a>').click(function() {
                if (currentImg.next().length > 0) {
                    currentImg = currentImg.next();
                    size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
                    wTxt = size.width;
                    hTxt = size.height;
                    showUI.find('.picswitch').html(currentImg.clone().css({
                        width: wTxt,
                        height: hTxt
                    }).removeClass('content-img').removeClass('homework-img').attr('title',''));
                }
            }));
            showUI.append($('<a style="position: absolute;top:' + (height / 2) + 'px;left:20px"><img src="/img/prev.png"/></a>').click(function() {

                if (currentImg.prev().length > 0) {
                    currentImg = currentImg.prev();
                    size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
                    wTxt = size.width;
                    hTxt = size.height;
                    showUI.find('.picswitch').html(currentImg.clone().css({
                        width: wTxt,
                        height: hTxt
                    }).removeClass('content-img').removeClass('homework-img').attr('title',''));
                }
            }));
            showUI.attr('tabindex', '10').bind('keydown', function(e) {
                if (e.KeyCode == 37) {
                    if (currentImg.prev().length > 0) {
                        currentImg = currentImg.prev();
                        size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
                        wTxt = size.width;
                        hTxt = size.height;
                        showUI.find('.picswitch').html(currentImg.clone().css({
                            width: wTxt,
                            height: hTxt
                        }).removeClass('content-img').removeClass('homework-img').attr('title',''));
                    }
                }
                if (e.KeyCode == 39) {
                    if (currentImg.next().length > 0) {
                        currentImg = currentImg.next();
                        size = checkSize(currentImg[0].naturalWidth, currentImg[0].naturalHeight);
                        wTxt = size.width;
                        hTxt = size.height;
                        showUI.find('.picswitch').html(currentImg.clone().css({
                            width: wTxt,
                            height: hTxt
                        }).removeClass('content-img').removeClass('homework-img').attr('title',''));
                    }
                }
            });
            $('body').append(showUI);
            startShow(showUI);
        });
    });
}







function checkSize(targertWidth, targetHeight) {
	var size = {};
	if (targertWidth < 600) {
		if (targetHeight < 400) {
			size = {
				width: 'auto',
				height: 'auto'
			}
			return size;
		} else {
			size = {
				width: 'auto',
				height: '400px'
			}
			return size;
		}
	} else {
		if (targetHeight < 400) {
			size = {
				width: '600px',
				height: 'auto'
			}
			return size;
		} else {
			if (targertWidth / targetHeight >= 1.5) {
				size = {
					width: '600px',
					height: 'auto'
				}
				return size;
			} else {
				size = {
					width: 'auto',
					height: '400px'
				}
				return size;
			}
		}
	}
}
