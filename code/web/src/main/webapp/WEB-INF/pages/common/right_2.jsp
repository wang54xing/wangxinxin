<script>
    function lxfScroll(main,titleli,alt,speed){
        var lxfscroll = $(main);
        var ul = lxfscroll.find("ul");
        var li = lxfscroll.find("li");
        var tli = $(titleli);
        var alt = $(alt);
        var cutspeed = 1;//切换的速度
        var autospeed = speed;//自动播放的速度
        var n = 0;
        var imgwidth = li.find("img").attr("width");//获取图片高度
        var lilength = li.length;//获取图片数量
        var timer;
        li.eq(0).clone().appendTo(ul);
        /* 标题按钮事件 */
        function hoverscroll() {
            tli.mouseenter(function(){
                var index = tli.index($(this));
                var lipoint = index*imgwidth;
                var imgTitle = li.find("img").eq(index).attr("alt");
                alt.text(imgTitle);
                tli.removeClass("cur");
                $(this).addClass("cur");
                ul.stop(true,false).animate({"left":-lipoint+"px"},cutspeed);
            });
        };
        /* 自动轮换 */
        function autoroll() {
            /*最后一个回到第一个的时候*/
            if(n >= lilength) {tli.removeClass("cur").eq(0).addClass("cur"); ul.stop(true,false).css({left:"0px"});n = 0;};
            var lipoint = n*imgwidth;
            var imgTitle = li.find("img").eq(n).attr("alt");
            ul.stop(true,false).animate({"left":-lipoint+"px"},cutspeed);
            tli.removeClass("cur").eq(n).addClass("cur");
            if(n >= lilength){tli.removeClass("cur").eq(0).addClass("cur"); };
            alt.text(imgTitle);
            n++;
            timer = setTimeout(autoroll, autospeed);
            if(n >= lilength) {alt.text(li.find("img").eq(0).attr("alt"));};
        };
        /* 鼠标悬停即停止自动轮换 */
        function stoproll() {
            li.hover(function() {
                        clearTimeout(timer);
                        n = $(this).prevAll().length+1;
                    }
                    , function() {
                        timer = setTimeout(autoroll, autospeed);
                    });
            tli.hover(function() {
                        clearTimeout(timer);
                        n = $(this).prevAll().length+1;
                    }
                    , function() {
                        timer = setTimeout(autoroll, autospeed);
                    });
        };
        hoverscroll();
        autoroll();//启动自动播放功能
        stoproll();//启动鼠标悬停功能
    };
    $(function(){
        getBusinessActivityImage();
        lxfScroll(".lxfscroll",".lxfscroll-title li",".lxfscroll-alt",4000);
        getPetImage();
    });

    function getPetImage() {
        $.ajax({
            url: "/pet/selectedPet.do",
            type: "post",
            success: function (data) {
                if(data.petInfo!=null){
                    $("#petImage").attr("src",data.petInfo.petimage)
                }else{
                    $("#petImage").attr("src","/img/egg.png");
                }
            }
        });
    }

    function getBusinessActivityImage(){
        $.ajax({
            url: "/business/getBusinessActivityImage.do",
            data:{
                skip:0,
                limit:4
            },
            async: false,
            type: "post",
            success: function (data) {
            	if(!data.list || data.list.length==0)
            	{
            		$(".homepage-right-top").hide();
            	}
            	
                if(data.list!=null){
                    var html1="";
                    var html2="";
                    for(var i=0;i<data.list.length;i++){
                        var obj=data.list[i];
                        html1+='<li><a href="/business/fieryactivitylist.do"><img src="'+obj.picFile+'" width="680" height="100" alt="'+(i+1)+'"/></a></li>';
                        if(i==0){
                            html2+='<li class="cur">'+(i+1)+'</li>';
                        }else{
                            html2+='<li>'+(i+1)+'</li>';
                        }
                    }
                    $("#ulObj").html(html1);
                    $("#ulIndex").html(html2);
                }
            }
        });
    }
</script>
<style type="text/css">

    .lxfscroll { width:680px !important; margin-left:auto; margin-right:auto; position: relative; height: 100px; overflow: hidden; }
    .lxfscroll ul li {height: 100px;width: 680px !important;text-align: center;line-height: 100px;font-size: 40px;font-weight: bold;background-color: #CCC;float: left;}
    .lxfscroll-title {width: 400px !important;position: relative!important;top: -30px!important; right: -565px!important;}
    .lxfscroll-title li { float: left; text-align: center; border: 1px solid #CCC; margin-top: 4px; cursor: pointer; height: 20px; width: 20px; line-height: 20px; margin-right: 4px; border-radius:10px; }
    .cur { color: #FFF; font-weight: bold; background:#666; }.lxfscroll ul { position: absolute; width: 3405px; }
    .lxfscroll-alt { position: absolute; bottom: 0px; z-index: 5; background-color: #666; color: #FFF; padding: 8px; Opacity=80);-moz-opacity:0.5; opacity: 0.5; width: 400px; overflow: hidden; }
    .info { text-align: center; color: #666; }.info a { color:#0000FF; }
</style>

<div class="homepage-right-top" style="overflow: visible">
    <div class="homepage-img-right-left" style="overflow: hidden!important;width:680px;height: 100px;float: left">
        <div class="lxfscroll">
            <ul id="ulObj">

            </ul>
        </div>
        <div class="lxfscroll-title">
            <ul id="ulIndex">

            </ul>
        </div>
    </div>
    <div style="overflow: visible">
        <a href="/petbag" target="_blank">
            <img  id="petImage"class="homepage-img-right-right" style="cursor: pointer;width: 100px;height: 100px;" src="/img/egg.png">
        </a>

        <span id="pet_span"></span>

    </div>

</div>