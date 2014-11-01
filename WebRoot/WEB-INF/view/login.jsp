<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script  type="text/javascript">
	var context = "${pageContext.request.contextPath}";
	//$(document).bind("contextmenu",function(){return false;});  
</script>

<title>欢迎访问经典项目</title>
</head>
<body>
	<form action="/EssenceProject/login.do" method="post">
		<table>
			<tr>
				<td>用户名</td>
				<td><input type="text" name="username"  autofocus x-webkit-speech/></td>     <!-- 实现语音输入 -->
			</tr>
			<tr>
				<td>密码</td>
				<td><input type="password" name="userpwd" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="登录" /></td>
			</tr>
			<tr>
				<td><label>验证码</label>
					<td><input id="random" type="text" /> <img id="authCode"
						src="${pageContext.request.contextPath }/authCode.do" style="cursor:pointer;" alt="点击刷新"
						title="点击刷新" />
				</td></td>
			</tr>
		 
		</table>
	</form>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/lib/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/index.js"></script>
</body>
</html>