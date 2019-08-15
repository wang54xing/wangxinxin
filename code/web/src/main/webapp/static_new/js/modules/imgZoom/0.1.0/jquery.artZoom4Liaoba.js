define(['jquery'],function(require,exports,module){
    window.RES_DOMAIN = "http://talk-static.happypingpang.com/";
    window.STATIC_DOMAIN = "http://static.happypingpang.com/";
/* functions for scroll left or right */
function left(obj,cur){
    var selector = 'a[cur='+(--cur)+'] img';
    $(obj).closest('.small-img-ul').find(selector)[0].click();
    return false;
}
function right(obj,cur){
    var selector = 'a[cur='+(++cur)+'] img';
    $(obj).closest('.small-img-ul').find(selector)[0].click();
    return false;
}

/*
 * artZoom 1.0.7
 * Date: 2011-06-22
 * (c) 2009-2011 TangBin, http://www.planeArt.cn
 *
 * This is licensed under the GNU LGPL, version 2.1 or later.
 * For details, see: http://creativecommons.org/licenses/LGPL/2.1/
 */
(function (document, $, log) {

    $.fn.artZoom = function (config) {
        config = $.extend({}, $.fn.artZoom.defaults, config);

        var tmpl, viewport,
            $this = this,
            loadImg = {},
            path = config.path,
            loading = path + '/loading.gif',
            max = path + '/zoomin.cur',
            min = path + '/zoomout.cur';

        new Image().src = loading;

        max = 'url(\'' + max + '\'), pointer';
        min = 'url(\'' + min + '\'), pointer';

        tmpl = [
            '<div class="ui-artZoom-toolbar" style="display:none">',
            '<span class="ui-artZoom-buttons" style="display:none">',
            '<a href="#" data-go="hide" class="p_putup">',
            config.hide,
            '</a>',
            '<span class="line">|</span>',
            '<a href="#" data-go="source" class="tb_icon_ypic">',
            config.source,
            '</a>',
            '<span class="line">|</span>',



            '<a href="#" data-go="left" class="tb_icon_turnleft j_rotation_left">',
            config.left,
            '</a>',
            '<span class="line">|</span>',
            '<a href="#" data-go="right" class="tb_icon_turnright">',
            config.right,
            '</a>',



            '</span>',
            '<span class="ui-artZoom-loading">',
            '<img data-live="stop" src="',
            loading,
            '" style="',
            'display:inline-block;*zoom:1;*display:inline;vertical-align:middle;',
            'width:16px;height:16px;"',
            ' />',
            ' <span>Loading..</span>',
            '</span>',
            '</div>',
            '<div class="ui-artZoom-box" style="display:none;text-align:center;">',
            '<span class="ui-artZoom-photo" data-go="hide"',
            ' style="display:inline-block;*display:inline;*zoom:1;overflow:hidden;position:relative;cursor:',
            min,
            '">',
            '<img data-name="thumb" data-go="hide" data-live="stop" src="',
            loading,
            '" />',
            '</span>',
            '</div>'
        ].join('');

        // jQuery事件代理
        this.on('click', function (event) {
            if (this.nodeName !== 'IMG' && this.getAttribute('data-live') === 'stop') return false;

            var $artZoom, buttonClick,
                that = this,
                $this = $(that),
                $parent = $this.parent(),
                src = that.src,
                show = $this.attr('data-artZoom-show') || src,
                source = $this.attr('data-artZoom-source') || show,
                maxWidth = config.maxWidth || ($parent[0].nodeName === 'A' ? $this.parent() : $this).parent().width(),
                maxHeight = config.maxHeight || 99999;

            maxWidth = maxWidth - config.borderWidth -30;

            // 对包含在链接内的图片进行特殊处理
            if ($parent[0].nodeName === 'A') {
                show = $parent.attr('data-artZoom-show') || $parent.attr('href');
                source = $parent.attr('data-artZoom-source') || $parent.attr('rel');
            };

            // 第一次点击
            if (!$this.data('artZoom')) {
                var wrap = document.createElement('div'),
                    $thumb, $box, $show;

                $artZoom = $(wrap);
                wrap.className = 'ui-artZoom ui-artZoom-noLoad';
                wrap.innerHTML = tmpl;

                ($parent[0].nodeName === 'A' ? $this.parent() : $this).before(wrap);
                $this.data('artZoom', $artZoom);
                $box = $artZoom.find('.ui-artZoom-box');

                $thumb = $artZoom.find('[data-name=thumb]');

                // 快速获取大图尺寸
                imgReady(show, function () {
                    var width = this.width,
                        height = this.height,
                        maxWidth2 = Math.min(maxWidth, width);

                    height = maxWidth2 / width * height;
                    width = maxWidth2;

                    // 插入大图并使用逐渐清晰加载的效果
                    $thumb.attr('src', src).
                        css(config.blur ? {
                            width: width + 'px',
                            height: height + 'px'
                        } : {display: 'none'}).
                        after([
                            '<img class="ui-artZoom-show" title="',
                            that.title,
                            '" alt="',
                            that.alt,
                            '" src="',
                            show,
                            '" style="width:',
                            width-8,
                            'px;height:',
                            height, // IE8 超长图片height属性失效BUG，改用CSS
                            'px;max-height:',
                            config.maxHeight, // lwang: add max-height
                            'px;max-width:',
                            maxWidth, // lwang: add max-height
                            'px;position:absolute;left:0;top:0;background:transparent"',
                            ' />'
                        ].join(''));

                    $show = $artZoom.find('.ui-artZoom-show');
                    $thumb.attr('class', 'ui-artZoom-show');

                    $artZoom.addClass('ui-artZoom-ready');
                    $artZoom.find('.ui-artZoom-buttons').show();
                    $this.data('artZoom-ready', true);
                    $this.hide();

                    //lwang: add filter when show
                    height = ((height+36) < config.minHeight) ? config.minHeight : height+36;
                    var current =  $parent[0].attributes['cur'].value;
                    var total = $parent[0].attributes['total'].value;
                    var curv_w;
                    if(width < maxWidth){
                        curv_w = (maxWidth-width)/2 + 0.3*width;
                    }else{
                        curv_w = 0.3*maxWidth;
                    }

                    var turn_left = '<div class="turn_left" style="background:transparent; width:'+curv_w+'px;height:'+height+'px; ; position:absolute;left:0;top:38px;cursor:url('+STATIC_DOMAIN+"feed/img/prev.cur"+'),pointer;background:#000;opacity: 0;filter: alpha(opacity=0);-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";-moz-opacity: 0;-khtml-opacity: 0;" onclick="left(this,'+current+')"></div>';
                    var turn_right = '<div class="turn_right" style="background:transparent; width:'+curv_w+'px;height:'+height+'px; ; position:absolute;right:0;top:38px;cursor: url('+STATIC_DOMAIN+"feed/img/next.cur"+'),pointer; background:#000;opacity: 0;filter: alpha(opacity=0);-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";-moz-opacity: 0;-khtml-opacity: 0;" onclick="right(this,'+current+')"></div>';
                    var vpic = $this.closest('div.vpic-wrap');
                    width = (width < maxWidth ? maxWidth : width);
                    vpic.css({'height':(height+10)+'px','width':'505px','max-width':'505px','position':'relative','padding':"0 10px 10px 10px"});


                    if(total > 1){
                        if(current == total){
                            //L
                            vpic.append(turn_left);
                        }else if(current == 2 && total == 3){
                            //LR
                            vpic.append(turn_left).append(turn_right);
                        }else{
                            //R
                            vpic.append(turn_right);
                        }
                    }
                    $this.closest('li').show();
                    $this.closest('li').css({'height':(height+20)+'px'}).siblings().hide();

                    $box.show();

                    // 大图完全加载完毕
                }, function () {
                    $thumb.removeAttr('class').hide();
                    $show.css({
                        position: 'static',
                        left: 'auto',
                        top: 'auto'
                    });

                    $artZoom.removeClass('ui-artZoom-noLoad');
                    $artZoom.find('.ui-artZoom-loading').hide();
                    $this.data('artZoom-load', true);

                    // 图片加载错误
                }, function () {
                    $artZoom.addClass('ui-artZoom-error');
                    log('jQuery.fn.artZoom: Load "' + show + '" Error!');
                });

            } else {
                $this.hide();


                //lwang: add filter when not first time show
                $artZoom = $this.data('artZoom');
                $artZoom.find('[data-go]').off('click', buttonClick); //cancel click event to avoid duplicate clicking
                var canvas = $this.closest('li').find('canvas'),max_w,h;
                if(canvas.length > 0){
                    max_w = canvas.attr('width');
                    h = canvas.attr('height')+"px";
                    max_w = (parseInt(max_w) < parseInt(maxWidth) ? parseInt(maxWidth) : parseInt(max_w))+'px';
                }else{
                    var big_img = $this.parent().prev().find('img.ui-artZoom-show');
                    max_w = big_img.css('max-width');
                    h = big_img.css('height');
                    max_w = (parseInt(max_w) < parseInt(maxWidth) ? parseInt(maxWidth) : parseInt(max_w))+'px';
                }
                h = parseFloat(h.replace("/px/i",""));
                h = ((h+36) < config.minHeight) ? config.minHeight : h+36;
                $this.closest('div.vpic-wrap').css({'width':'505px','height':(h+10)+"px",'max-width':'505px','position':'relative','padding':"0 10px 10px 10px"});
                $this.closest('div.vpic-wrap').find(".turn_left").show();
                $this.closest('div.vpic-wrap').find(".turn_right").show();
                $this.closest('li').css({'height':(h+20)+"px"}).show();
                $this.closest('li').siblings('li').hide();
            };

            $artZoom = $this.data('artZoom');
            buttonClick = function (event) {
                var target = this,
                    go = target.getAttribute('data-go'),
                    live = target.getAttribute('data-live'),
                    degree = $this.data('artZoom-degree') || 0,
                    elem = $artZoom.find('.ui-artZoom-show')[0];

                if (live === 'stop') return false;
                if (/img|canvas$/i.test(target.nodeName)) go = 'hide';

                switch (go) {
                    case 'left':
                        degree -= 90;
                        degree = degree === -90 ? 270 : degree;
                        break;
                    case 'right':
                        degree += 90;
                        degree = degree === 360 ? 0 : degree;
                        break;
                    case 'source':
                        window.open(source || show || src);
                        break;
                    case 'hide':
                        $this.show();
                        $artZoom.find('.ui-artZoom-toolbar').hide();
                        $artZoom.hide();
                        $artZoom.find('[data-go]').off('click', buttonClick);

                        //lwang: revert back when hide
                        var eCanvas = '{$canvas}';
                        if($this.data('artZoom-load') && elem[eCanvas]){
//                                            imgRotate(elem, 0, maxWidth, maxHeight);
                            $(elem[eCanvas]).remove();
                            $this.removeData('artZoom-load');
                            $this.removeData('artZoom');
                            $this.data('artZoom-degree', 0);
                        }

                        $this.closest('.small-img-ul').find('img.artZoom').show();
                        $this.closest('.small-img-ul').find('li').removeAttr('style').show();
                        $this.closest('.small-img-ul').find('div.vpic-wrap').removeAttr('style');
                        $this.closest('.small-img-ul').find('.ui-artZoom').hide();
                        $this.closest('.small-img-ul').find(".turn_left").hide();
                        $this.closest('.small-img-ul').find(".turn_right").hide();

                        var position = $this.closest('div.bbs-list').position();
                        window.scrollTo(position.left,position.top);

                        break;
                };

                if ((go === 'left' || go === 'right') && $this.data('artZoom-load')) {
                    imgRotate(elem, degree, maxWidth, maxHeight);
                    $this.data('artZoom-degree', degree);
                };

                return false;
            };
            $artZoom.show().find('.ui-artZoom-toolbar').slideDown(0);
            $artZoom.find('[data-go]').on('click', buttonClick);
            return false;
        });

        // 给目标缩略图应用外部指针样式
        this.on('mouseover', function () {
            if (this.className !== 'ui-artZoom-show') this.style.cursor = max;
        });

        // 预加载指针形状图标
        if (this[0]) this[0].style.cursor = max;

        return this;
    };
    $.fn.artZoom.defaults = {
        path: './images',
        left: '\u5de6\u65cb\u8f6c',
        right: '\u53f3\u65cb\u8f6c',
        source: '\u770b\u539f\u56fe',
        hide: '\xd7',
        blur: true,
        preload: true,
        maxWidth: null,
        maxHeight: null,
        borderWidth: 18
    };

    /**
     * 图片旋转
     * @version	2011.05.27
     * @author	TangBin
     * @param	{HTMLElement}	图片元素
     * @param	{Number}		旋转角度 (可用值: 0, 90, 180, 270)
     * @param	{Number}		最大宽度限制
     * @param	{Number}		最大高度限制
     */
    var imgRotate = $.imgRotate = function () {
        var eCanvas = '{$canvas}',
            isCanvas = !!document.createElement('canvas').getContext;

        return function (elem, degree, maxWidth, maxHeight) {
            var x, y, getContext,
                resize = 1,
                width = elem.naturalWidth,
                height = elem.naturalHeight,
                canvas = elem[eCanvas];

            // 初次运行
            if (!elem[eCanvas]) {

                // 获取图像未应用样式的真实大小 (IE和Opera早期版本)
                if (!('naturalWidth' in elem)) {
                    var run = elem.runtimeStyle, w = run.width, h = run.height;
                    run.width  = run.height = 'auto';
                    elem.naturalWidth = width = elem.width;
                    elem.naturalHeight = height = elem.height;
                    run.width  = w;
                    run.height = h;
                };

                elem[eCanvas] = canvas = document.createElement(isCanvas ? 'canvas' : 'span');
                elem.parentNode.insertBefore(canvas, elem.nextSibling);
                elem.style.display = 'none';
                canvas.className = elem.className;
                canvas.title = elem.title;
                if (!isCanvas) {
                    canvas.img = document.createElement('img');
                    canvas.img.src = elem.src;
                    canvas.appendChild(canvas.img);
                    canvas.style.cssText = 'display:inline-block;*zoom:1;*display:inline;' +
                        // css reset
                        'padding:0;margin:0;border:none 0;position:static;float:none;overflow:hidden;width:auto;height:auto';
                };
            };

            var size = function (isSwap) {
                if (isSwap) width = [height, height = width][0];
                if (width > maxWidth) {
                    resize = maxWidth / width;
                    height =  resize * height;
                    width = maxWidth;
                };
                if (height > maxHeight) {
                    resize = resize * maxHeight / height;
                    width = maxHeight / height * width;
                    height = maxHeight;
                };
                if (isCanvas) (isSwap ? height : width) / elem.naturalWidth;
            };

            switch (degree) {
                case 0:
                    x = 0;
                    y = 0;
                    size();
                    break;
                case 90:
                    x = 0;
                    y = -elem.naturalHeight;
                    size(true);
                    break;
                case 180:
                    x = -elem.naturalWidth;
                    y = -elem.naturalHeight
                    size();
                    break;
                case 270:
                    x = -elem.naturalWidth;
                    y = 0;
                    size(true);
                    break;
            };

            if (isCanvas) {
                canvas.setAttribute('width', width);
                canvas.setAttribute('height', height);
                getContext = canvas.getContext('2d');
                getContext.rotate(degree * Math.PI / 180);
                getContext.scale(resize, resize);
                getContext.drawImage(elem, x, y);
            } else {
                canvas.style.width = (width-10) + 'px';
                canvas.style.height = height + 'px';// 解决IE8使用滤镜后高度不能自适应
                canvas.img.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=' + degree / 90 + ')';
                canvas.img.width = elem.width * resize;
                canvas.img.height = elem.height * resize;
                canvas.img.style.maxWidth = elem.width * resize;
                canvas.img.style.maxHeight = elem.height * resize;
                canvas.img.style.styleFloat = "left";
            };
            //lwang: add width/height when ratio
            height = ((height+45) < 200) ? 200 : height+45;
            $(elem).closest('li').css({'height':(height+20)+'px'});
            $(elem).closest('div.vpic-wrap').css({'width':'505px','height':(height+10)+'px','padding':"0 10px 10px 10px"});

        };
    }();

    /**
     * 图片头数据加载就绪事件 - 更快获取图片尺寸
     * @version	2011.05.27
     * @param	{String}	图片路径
     * @param	{Function}	尺寸就绪
     * @param	{Function}	加载完毕 (可选)
     * @param	{Function}	加载错误 (可选)
     * @example imgReady('http://www.google.com.hk/intl/zh-CN/images/logo_cn.png', function () {
		alert('size ready: width=' + this.width + '; height=' + this.height);
	});
     */
    var imgReady = (function () {
        var list = [], intervalId = null,

        // 用来执行队列
            tick = function () {
                var i = 0;
                for (; i < list.length; i++) {
                    list[i].end ? list.splice(i--, 1) : list[i]();
                };
                !list.length && stop();
            },

        // 停止所有定时器队列
            stop = function () {
                clearInterval(intervalId);
                intervalId = null;
            };

        return function (url, ready, load, error) {
            var onready, width, height, newWidth, newHeight,
                img = new Image();

            img.src = url;

            // 如果图片被缓存，则直接返回缓存数据
            if (img.complete) {
                ready.call(img);
                load && load.call(img);
                return;
            };

            width = img.width;
            height = img.height;

            // 加载错误后的事件
            img.onerror = function () {
                error && error.call(img);
                onready.end = true;
                img = img.onload = img.onerror = null;
            };

            // 图片尺寸就绪
            onready = function () {
                newWidth = img.width;
                newHeight = img.height;
                if (newWidth !== width || newHeight !== height ||
                    // 如果图片已经在其他地方加载可使用面积检测
                    newWidth * newHeight > 1024
                    ) {
                    ready.call(img);
                    onready.end = true;
                };
            };
            onready();

            // 完全加载完毕的事件
            img.onload = function () {
                // onload在定时器时间差范围内可能比onready快
                // 这里进行检查并保证onready优先执行
                !onready.end && onready();

                load && load.call(img);

                // IE gif动画会循环执行onload，置空onload即可
                img = img.onload = img.onerror = null;
            };

            // 加入队列中定期执行
            if (!onready.end) {
                list.push(onready);
                // 无论何时只允许出现一个定时器，减少浏览器性能损耗
                if (intervalId === null) intervalId = setInterval(tick, 40);
            };
        };
    })();



}(document, jQuery, function (msg) {window.console && console.log(msg)}));


    module.exports = function(){
        $('.artZoom').artZoom({
            path: RES_DOMAIN + 'js/imgZoom/images',	// 设置artZoom图片文件夹路径
            preload: true,		// 设置是否提前缓存视野内的大图片
            blur: false,			// 设置加载大图是否有模糊变清晰的效果
            maxWidth: 535,
            minHeight: 200,
            maxHeight: 99999,
            // 语言设置
            left: '向左转',		// 左旋转按钮文字
            right: '向右转',		// 右旋转按钮文字
            source: '查看大图',		// 查看原图按钮文字
            hide:'收起'
        });
    }


})