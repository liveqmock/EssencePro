<%@ page language="java" pageEncoding="UTF-8"%>
<!-- 界面引入c标签、fmt标签、fn标签 -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
<!-- 取得项目的绝对路径 -->
<c:set var="ctx" value="${pageContext.request.contextPath }"/>

