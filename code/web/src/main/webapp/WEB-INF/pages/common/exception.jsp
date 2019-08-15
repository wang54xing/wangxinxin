<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>异常页面</title>
		<script>
		    function toggleDetailDisplay(div){
		        var dediv=document.getElementById("details");
		        if(dediv.style.display=="none"){
		        	dediv.style.display="";
		        	div.innerText="- 详细信息------------"
		        }else{
		        	dediv.style.display="none";
		        	div.innerText="+ 详细信息------------"
		        }
		    }
		</script>
	</head>
	<body>
	<div>
	   <h2> 响应请求时发生异常！</h2>
	   异常描述：<font color="red">${exception}</font>	 
	</div><br/>
	<div style="cursor:pointer;color:blue" onclick="toggleDetailDisplay(this)">
	+ 详细信息------------
	</div>
	<div id="details" style="display:none;border:1px solid gray">
	${exceptionStack}
	</body>
</html>