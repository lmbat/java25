<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TMS|系统管理/新增权限</title>

    <%@include file="../../include/css.jsp"%>

</head>
<body class="hold-transition skin-red-light sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../../include/topHead.jsp"%>

    <jsp:include page="../../include/sider.jsp">
        <jsp:param name="menu" value="manage_permission"/>
    </jsp:include>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
        <h1>权限管理</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-header">
                    <h3>新增权限</h3>
                    <div class="box-tools">
                        <a href="/manage/permission" class="btn btn-success btn-sm">取消</a>
                    </div>
                    <div class="box-body">
                        <form id="saveForm" method="post">
                            <div class="form-group">
                                <label>权限名称</label>
                                <input type="text" name="permissionName" class="form-control">
                            </div>
                            <div class="form-group">
                                <label>权限代号</label>
                                <input type="text" name="permissionCode" class="form-control">
                            </div>
                            <div class="form-group">
                                <label>资源URL</label>
                                <input type="text" name="url" class="form-control">
                            </div>
                            <div class="form-group">
                                <label>权限类型</label>
                                <select name="permissionType" class="form-control">
                                    <option value="菜单">菜单</option>
                                    <option value="按钮">按钮</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>父权限</label>
                                <select name="parentId" class="form-control">
                                    <option value="0">最高级权限</option>
                                    <c:forEach items="${permissionList}" var="permission">
                                        <option value="${permission.id}">${permission.permissionName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="box-footer">
                        <button class="btn pull-right btn-primary" id="saveBtn">保存</button>
                    </div>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
</div>
<!-- ./wrapper -->

<%@include file="../../include/js.jsp"%>
<script>
    $(function () {
        $("#saveBtn").click(function() {
            $("#saveForm").submit();
        });
    })
</script>
</body>
</html>