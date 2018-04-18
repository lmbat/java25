<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TMS|系统管理/角色管理</title>

    <%@include file="../../include/css.jsp"%>

</head>
<body class="hold-transition skin-red-light sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../../include/topHead.jsp"%>

    <jsp:include page="../../include/sider.jsp">
        <jsp:param name="menu" value="manage_roles"/>
    </jsp:include>

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>角色管理</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">角色列表</h3>
                    <div class="box-tools">
                        <a href="/manage/roles/new" class="btn btn-vimeo btn-sm"><i class="fa fa-plus"></i>新增角色</a>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table tree">
                        <tbody>
                            <c:forEach items="${rolesList}" var="roles">
                                <tr class="bg-info">
                                    <th>
                                        角色名称：<strong>${roles.rolesName}</strong>
                                        <span class="pull-right">
                                            <a style="color: #0c0c0c;" href="/manage/roles/${roles.id}/edit"><i class="fa fa-edit"></i></a>
                                            <a style="color: #0c0c0c;" class="del" rel="${roles.id}" href="javascript:;"><i class="fa fa-trash"></i></a>
                                        </span>
                                    </th>
                                </tr>
                                <tr>
                                    <td>
                                        <c:forEach items="${roles.permissionList}" var="per">
                                            <i class="fa fa-navicon"></i>${per.permissionName}
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
</div>
<!-- ./wrapper -->

<%@include file="../../include/js.jsp"%>
<script src="/static/plugins/treegrid/js/jquery.treegrid.min.js"></script>
<script src="/static/plugins/treegrid/js/jquery.treegrid.bootstrap3.js"></script>
<script src="/static/plugins/layer/layer.js"></script>

<script>
    $(function () {
        $('.tree').treegrid();

        $(".del").click(function(){
            var id = $(this).attr("rel");
            layer.confirm("确定要删除吗",function (index) {
                layer.close(index);
                $.get("/manage/permission/"+id+"/del").done(function (result) {
                    if(result.status == 'success') {
                        history.go(0);
                    } else {
                        layer.msg(result.message);
                    }
                }).error(function () {
                    layer.msg("服务器忙");
                });
            })
        });
    });

</script>


</body>
</html>