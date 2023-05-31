<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>添加事项</title>
</head>

<body>
	<input type="hidden" id="page_id" name="page_id" value="project_add"/>
	事项    ID：<input id="object_id" name="object_id"><br>
	事项名称：<input id="title" name="title"><br>
	详细信息：<input id="content" name="content"><br>
	<button id="add_button" name="add_button">确定添加</button>
</body>
</html>
<script src="jquery.min.js"></script>
<script src="project.js"></script>