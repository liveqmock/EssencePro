<%@ page pageEncoding="UTF-8"%>
<!-- 添加标签c和fn -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- W3C -->
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>经典项目</title>
<!-- 包含的jsp界面 -->
<%-- <%@include file="/WEB-INF/jsp/common/tag.jsp"%> --%>
<%--  添加css样式
<link type="text/css" rel="stylesheet" href="${ctxPath }/css/reset.css" />
 --%>
</head>
<body id="bodydoc">
    哈哈哈，the first project！
    <input type="button" value="点击我" onclick="abc();"/>
    <input type="text" id="project_id" value="李医生"/>
    <input type="text" id="pym" />
			    
    <input id="d11" type="text" onfocus="WdatePicker()"/>
    
    <div title="编辑工具框">
    <textarea rows="10" cols="20" id="editor"></textarea>
    </div>
   <ul id="tree" class="ztree" style="width:300px;overflow:auto;">
   </ul>
	<!-- 引入js -->
	<%-- <script src="${ctxPath }/js/lib/jquery-1.10.0.min.js"></script> --%>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/lib/jquery-1.9.1.min.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ztree/zTreeStyle/zTreeStyle.css">
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/ztree/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/lib/json2.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/lib/pym.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/lib/util.js"></script>
    <script src="${pageContext.request.contextPath }/js/editor/kindeditor.js"></script>
    <script src="${pageContext.request.contextPath }/js/editor/kindeditor-all.js"></script>
    <script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/js/ztree/jquery.ztree.excheck-3.5.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath }/js/index.js"></script>
</body>
</html>
