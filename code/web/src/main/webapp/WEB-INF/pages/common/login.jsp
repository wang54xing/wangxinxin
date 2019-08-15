<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录对话框</title>
</head>
<body>
<form action="login.action" method="post">
<font color="red">
${errorMessage }
</font><br>
用户名：<input name="userName" value="${UserName }">
<br>
密码：<input type="password" name="password">
<input type="submit" value="确定">
</form>
</body>
</html>