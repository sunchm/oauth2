<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>Login Page | Amaze UI Example</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <link rel="alternate icon" type="image/png" href="${pageContext.request.contextPath}/static/i/favicon.png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/amazeui.min.css"/>
   <style>
    html,body{
      height: 100%;
    }

    .login-content{
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: 70%;
    }

    .header {
      text-align: center;
      background: #1e1e28;
    }
    .header .am-g{
      text-align: left;
      padding-left: 3rem;
      padding-bottom: 1rem;
      font-size: 1.4rem;
      color: #f1f1f1;
    }
    .header h1 {
      font-size: 200%;
      color: #f1f1f1;
      margin-top: 3rem;
      margin-bottom: 0.5rem;
    }

    .am-form .am-form-group i{
      width: 3.2rem;
      text-align: center;
      font-size: 2.4rem;
      vertical-align: middle;
    }

    .am-form .am-form-group input{
      display: inline-block;
      width: calc(100% - 3.2rem);
    }

    .am-form .am-form-group span.error{
      font-size: 1.4rem;
      width: 100%;
      color: red;
    }
  </style>
</head>
<body>
<div class="login-content">
  <div class="header">
    <div class="am-g">
      <h1>授权登录</h1>
      <div style="font-size: 1.8rem">Cloudrive</div>
      <div>智云互联行车系统</div>
    </div>
  </div>
  <form method="post" class="am-form am-form-horizontal" action="authorize">
  <div class="am-g">
    <div class="col-lg-6 col-md-8 col-sm-centered">
	    <input type="hidden" id="client_id" name="client_id" value="${param.client_id}">
        <input type="hidden" id="response_type" name="response_type" value="${param.response_type}">
        <input type="hidden" id="redirect_uri" name="redirect_uri" value="${param.redirect_uri}">
        <input type="hidden" id="state" name="state" value="${param.state}">
        <input type="hidden" id="scope" name="scope" value="${param.scope}">
        <input type="hidden" id="st" name="st" value="${requestScope.st}">
        <input type="hidden" id= "flag" name="flag" value="1">
        
        <div class="am-form-group">
          <div class="col-sm-12">
          	<div style="width: 38px;height: 38px;float: left;">
          		<img src="${pageContext.request.contextPath}/static/i/account.png" style="width: 2rem;height: 2rem;margin: 0.8rem 0.8rem 0.8rem 0.8rem;">
          	</div>
            <input type="text" id="username" name="username" placeholder="请输入账号">
          </div>
        </div>

        <div class="am-form-group">
          <div class="col-sm-12">
            <div style="width: 38px;height: 38px;float: left;">
          		<img src="${pageContext.request.contextPath}/static/i/password.png" style="width: 2rem;height: 2rem;margin: 0.8rem 0.8rem 0.8rem 0.8rem;">
          	</div>
            <input type="password" id="password" name="password" placeholder="密码">
          </div>
        </div>

        <div class="am-form-group">
          <div class="col-sm-12" style="text-align: right">
            <span class="error">${error}</span>
          </div>
        </div>

        <div class="am-form-group">
          <div class="col-sm-12" style="text-align: center">
            <button style="padding: .625em 4em;border-radius: 1rem" type="submit" class="am-btn am-btn-default">授权</button>
          </div>
        </div>
    </div>
  </div>
  </form>
</div>
</body>
</html>