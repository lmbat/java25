<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>TMS|综合管理系统</title>

    <%@include file="include/css.jsp"%>
    <!-- iCheck -->
    <link rel="stylesheet" href="/static/plugins/iCheck/square/blue.css">

</head>
<body background="/static/img/1.jpg">
<%--<body background="/static/img/photo.jpg" class="hold-transition login-page" >--%>

<div class="login-box">
    <div class="login-logo">
        <a><b>TMS</b>综合管理系统</a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <c:if test="${not empty message}">
            <p class="login-box-msg">${message}</p>
        </c:if>

        <form method="post">
            <div class="form-group has-feedback">
                <input type="text" class="form-control" name="accountMobile" placeholder="手机号码" value="${mobile}" autofocus style= “background-color:transparent;border-color:black;">
                <span class="glyphicon glyphicon-phone form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" class="form-control" name="accountPassword" placeholder="密码" style= “background-color:transparent;border-color:black;">
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label>
                            <input type="checkbox"> 记住账号
                        </label>
                    </div>
                </div>
                <!-- /.col -->
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
                </div>
                <!-- /.col -->
            </div>
        </form>
        <a href="#">忘记密码</a><br>
    </div>
    <!-- /.login-box-body -->
</div>
<!-- /.login-box -->

<!-- jQuery 3 -->
<script src="/static/plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="/static/bootstrap/js/bootstrap.min.js"></script>
<!-- iCheck -->
<script src="/static/plugins/iCheck/icheck.min.js"></script>
<script>
    $(function () {
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' /* optional */
        });
    });
</script>
</body>
</html>