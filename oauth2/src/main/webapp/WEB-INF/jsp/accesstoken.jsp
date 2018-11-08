<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <link href="${pageContext.request.contextPath}/static/css/css.css" rel="stylesheet">
    <title>OAuth2 Server</title>
</head>
<body>
<div class="container">
    <div class="header clearfix">
        <nav>
            <ul class="nav nav-pills pull-right">
            </ul>
        </nav>
        <h3 class="text-muted">OAuth2 Server</h3>
    </div>
	<h3>授权码模式：</h3>
    <div class="row marketing">
        <div class="col-lg-10">
            <form class="form-horizontal" method="post" action="${pageContext.request.contextPath}/accessToken">
                <div class="form-group">
                    <label for="client_id" class="col-sm-4 control-label">应用id</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="client_id" name="client_id" value="oauth_clientid">
                    </div>
                </div>
                <div class="form-group">
                    <label for="client_secret" class="col-sm-4 control-label">应用secret</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="client_secret" name="client_secret" value="oauth_clientsecret">
                    </div>
                </div>
                <div class="form-group">
                    <label for="grant_type" class="col-sm-4 control-label">grant_type</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="grant_type" name="grant_type" placeholder="grant_type" value="authorization_code">
                    </div>
                </div>
                <div class="form-group">
                    <label for="code" class="col-sm-4 control-label">授权码</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="code" name="code" placeholder="授权码">
                    </div>
                </div>
               <div class="form-group">
                   <label for="redirect_uri" class="col-sm-4 control-label">回调地址</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="redirect_uri" name="redirect_uri" value="https://www.baidu.com">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <button type="submit" class="btn btn-default">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <h3>客户端模式：</h3>
    <div class="row marketing">
        <div class="col-lg-10">
            <form class="form-horizontal" method="post" action="${pageContext.request.contextPath}/accessToken">
                <div class="form-group">
                    <label for="client_id" class="col-sm-4 control-label">应用id</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="client_id" name="client_id" value="oauth_clientid">
                    </div>
                </div>
                <div class="form-group">
                    <label for="client_secret" class="col-sm-4 control-label">应用secret</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="client_secret" name="client_secret" value="oauth_clientsecret">
                    </div>
                </div>
                <div class="form-group">
                    <label for="grant_type" class="col-sm-4 control-label">grant_type</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="grant_type" name="grant_type" placeholder="grant_type" value="client_credentials">
                    </div>
                </div>
				<div class="form-group">
                    <label for="scope" class="col-sm-4 control-label">scope</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="scope" name="scope" value="scp01001">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <button type="submit" class="btn btn-default">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <h3>令牌刷新：</h3>
    <div class="row marketing">
        <div class="col-lg-10">
            <form class="form-horizontal" method="post" action="${pageContext.request.contextPath}/accessToken">
                <div class="form-group">
                    <label for="client_id" class="col-sm-4 control-label">应用id</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="client_id" name="client_id" value="oauth_clientid">
                    </div>
                </div>
                <div class="form-group">
                    <label for="client_secret" class="col-sm-4 control-label">应用secret</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="client_secret" name="client_secret" value="oauth_clientsecret">
                    </div>
                </div>
                <div class="form-group">
                    <label for="grant_type" class="col-sm-4 control-label">grant_type</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="grant_type" name="grant_type" placeholder="grant_type" value="refresh_token">
                    </div>
                </div>
                <div class="form-group">
                    <label for="refresh_token" class="col-sm-4 control-label">刷新码</label>
                    <div class="col-sm-8">
                        <input type="text" class="form-control" id="refresh_token" name="refresh_token" placeholder="刷新码">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <button type="submit" class="btn btn-default">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<footer class="footer">
    <p style="text-align:center">&copy; Company 2018</p>
</footer>
</body>
</html>
