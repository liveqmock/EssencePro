<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>Error Page</title>    
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="0" />       
  </head>
  
  <body>
    <h2>Exception: ${title }</h2> 
    <a href="javascript:document.getElementById('show').style.display = 'block';void(0);">
        详细信息
    </a> 
    <div id="show" style="color: red; display: none;">
        ${exception }
    </div> 
  </body>
</html>