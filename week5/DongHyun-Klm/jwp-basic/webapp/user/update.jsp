<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="kr">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>회원정보 수정</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <![endif]-->
    <link href="../css/styles.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-fixed-top header">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="../index.html" class="navbar-brand">SLiPP</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse1">
                <i class="glyphicon glyphicon-search"></i>
            </button>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse1">
            <form class="navbar-form pull-left">
                <div class="input-group" style="max-width:470px;">
                    <input type="text" class="form-control" placeholder="Search" name="srch-term" id="srch-term">
                    <div class="input-group-btn">
                        <button class="btn btn-default btn-primary" type="submit">
                            <i class="glyphicon glyphicon-search"></i>
                        </button>
                    </div>
                </div>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="glyphicon glyphicon-bell"></i></a>
                    <ul class="dropdown-menu">
                        <li><a href="https://slipp.net" target="_blank">SLiPP</a></li>
                        <li><a href="https://facebook.com" target="_blank">Facebook</a></li>
                    </ul>
                </li>
                <li><a href="../user/list"><i class="glyphicon glyphicon-user"></i></a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="navbar navbar-default" id="subnav">
    <div class="col-md-12">
        <div class="navbar-header">
            <a href="#" style="margin-left:15px;" class="navbar-btn btn btn-default btn-plus dropdown-toggle" data-toggle="dropdown">
                <i class="glyphicon glyphicon-home" style="color:#dd1111;"></i> Home <small><i class="glyphicon glyphicon-chevron-down"></i></small>
            </a>
            <ul class="nav dropdown-menu">
                <li><a href="../user/profile"><i class="glyphicon glyphicon-user" style="color:#1111dd;"></i> Profile</a></li>
                <li class="nav-divider"></li>
                <li><a href="#"><i class="glyphicon glyphicon-cog" style="color:#dd1111;"></i> Settings</a></li>
            </ul>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse2">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="../index">Posts</a></li>
                <li><a href="../user/login" role="button">로그인</a></li>
                <li><a href="../user/form" role="button">회원가입</a></li>
                <li class="active"><a href="#" role="button">회원정보수정</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container" id="main">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default content-main">
            <!-- 실제로는 request에 담겨 있는 User 객체를 EL로 꺼내온다고 가정 -->
            <!-- 예: request.setAttribute("user", userDto); -->
            <form name="updateForm" method="post" action="<c:url value='/user/update'/>">
                <div class="form-group">
                    <label for="userId">사용자 아이디</label>
                    <input
                            type="text"
                            class="form-control"
                            id="userId"
                            name="userId"
                            value="${user.userId}"
                            readonly
                    >
                </div>

                <div class="form-group">
                    <label for="password">비밀번호</label>
                    <input
                            type="password"
                            class="form-control"
                            id="password"
                            name="password"
                            placeholder="변경할 비밀번호 입력 (변경하지 않으려면 비워두기)"
                    >
                </div>

                <div class="form-group">
                    <label for="name">이름</label>
                    <input
                            type="text"
                            class="form-control"
                            id="name"
                            name="name"
                            value="${user.name}"
                            placeholder="Name"
                            required
                    >
                </div>

                <div class="form-group">
                    <label for="email">이메일</label>
                    <input
                            type="email"
                            class="form-control"
                            id="email"
                            name="email"
                            value="${user.email}"
                            placeholder="Email"
                            required
                    >
                </div>

                <button type="submit" class="btn btn-primary pull-right">수정하기</button>
                <div class="clearfix"></div>
            </form>
        </div>
    </div>
</div>

<!-- script references -->
<script src="../js/jquery-2.2.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../js/scripts.js"></script>
</body>
</html>
