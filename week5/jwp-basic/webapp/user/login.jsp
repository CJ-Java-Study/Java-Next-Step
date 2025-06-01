<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="kr">
<head>
  <meta charset="UTF-8">
  <title>로그인</title>
  <link href="<c:url value='/css/bootstrap.min.css' />" rel="stylesheet">
  <link href="<c:url value='/css/styles.css' />" rel="stylesheet">
</head>
<body>
<!-- 공통 네비게이션(헤더) include -->
<jsp:include page="/commons/nav.jsp"/>

<div class="container" id="main">
  <div class="col-md-6 col-md-offset-3">
    <div class="panel panel-default content-main">
      <form method="post" action="<c:url value='/user/login' />">
        <div class="form-group">
          <label for="userId">사용자 아이디</label>
          <input type="text" class="form-control" id="userId" name="userId"
                 placeholder="User ID"
                 value="<c:out value='${param.userId}'/>"
                 required>
        </div>
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input type="password" class="form-control" id="password" name="password"
                 placeholder="Password" required>
        </div>
        <button type="submit" class="btn btn-success pull-right">로그인</button>
        <div class="clearfix"></div>
      </form>
    </div>
  </div>
</div>

<script src="<c:url value='/js/jquery-2.2.0.min.js' />"></script>
<script src="<c:url value='/js/bootstrap.min.js' />"></script>
</body>
</html>
