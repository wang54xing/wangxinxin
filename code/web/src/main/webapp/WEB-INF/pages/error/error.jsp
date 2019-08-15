<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/static/css/error/error.css" rel="stylesheet">

<script type="text/javascript">
 function goPre()
 {
	 history.go(-1);
 }
 
 function goIndex()
 {
	 location.href="/user?version=91&index=6";
 }
</script>
</head>
<body>
<!--===============================start-头部引入======================================-->
<!--===============================end-头部引入======================================-->
<!--=================================内容================================-->
<div class="main">
    <div class="main_info">
        <dl>
            <dt>系统繁忙中，请稍后再试！</dt>
            <dd>您使用的URl可能拼写有误，该页面可能已经移动，或者它可能只是临时脱机</dd>
            <dd>
                <button onclick="goPre()">返回上页</button>
                <button onclick="goIndex()">返回首页</button>
            </dd>
        </dl>
    </div>
</div>
</body>
</html>