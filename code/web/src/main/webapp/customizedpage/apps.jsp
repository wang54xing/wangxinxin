<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <meta name="renderer" content="webkit">
    <title>APP下载</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/apps.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    
    <script type="text/javascript">
    
    $(function() {
    
    	 $.ajax({
             url: '/version/recently.do',
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               {
            	 var version="5.0.1";
            	 try
            	 {
            		 version=res.version;
            	 }catch(x)
            	 {}
            	 jQuery("#android_href").attr("href","/upload/resources/k6kt_"+version+".apk");
               }
             }
         });
    	
    });
    
    </script>
</head>
<body>
<%@ include file="/WEB-INF/pages/common/ypxxhead.jsp"%>

<div class="main-container">
    <div class="content-container">
        <div class="right-container">
            <img src="/img/K6KT/phones.png">
        </div>

        <div class="left-container">
            <div class="index-title">
                全向互动<br>
                K6KT-快乐课堂
            </div>
            <div>
                <p>复兰科技全向互动“K6KT—快乐课堂”由来自世界各地顶尖学府(麻省理工大学、科隆大学、复旦大学、香港科技大学等）杰出人才共同开发打造，其中多项技术获得国家发明专利。“K6KT—快乐课堂”基于云平台和B/S架构，支持千万级在线学习，兼容各种终端访问。同时整合了国内外一线教育工作者的实践经验及全球优质教育资源， 结合大数据处理技术和教学反馈机制，全面提升学生学习兴趣和能力。</p>
                <p>通过手机移动端APP，您可以：<br>
                    * 随时接收和在线提交学校发布的家庭作业<br>
                    * 随时接收学校发布的各类通知<br>
                    * 随时和学校、老师、家长进行私信交流，掌握孩子动态<br>
                    * 随时拍照，上传到“微校园”进行分享<br>
                    * 随时随地，家校互动，在线学习，虚拟社区全分享。</p>
            </div>

            <div>
                <a href="https://itunes.apple.com/cn/app/k6kt/id911651765?l=zh&ls=1&mt=8" target="_blank"><span class="button-AppStore app-btn">苹果版</span></a>
                <a id="android_href" href="/upload/resources/k6kt_5.0.1.apk"><span class="button-Android app-btn">安卓版</span></a>
            </div>

            <div class="clear qrcode-div">
            	<img src="/img/ios-app.png" width="150" height="150" style="vertical-align: bottom; margin-right: 56px">
                <img src="/img/an-qrcode.png" width="150" height="150" style="vertical-align: bottom; margin-right: 10px">
	            <span>
	            	客户端下载<br/>扫描对应二维码
	            </span>
            </div>
        </div>
    </div>
</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/pages/common/flippedroot.jsp"%>
<!-- 页尾 -->
</body>
</html>