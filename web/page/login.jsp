<%--
  Created by IntelliJ IDEA.
  User: Nick
  Date: 29.10.2018
  Time: 12:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>

    <link href=' /resources/style/login.css' rel='stylesheet' type='text/css'>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src='/resources/js/login.js'></script>

</head>
<body>
<h1 id="system_title">GROUPPER</h1>
<div class="login-page">

    <div class="form" id="login_form">
        <form class="login-form" method="post" action="/login">
            <h1>Login</h1>
            <input name="login" type="text" placeholder="username"/>
            <input name="pass" type="password" placeholder="password"/>
            <p>
                <button id="login_button" type="submit">Login</button>
            </p>
            <p id="toRegistration">Registration</p>
        </form>
    </div>

    <div class="form" id="registration_form">
        <form class="login-form" method="post" action="/login">
            <h1>Registration</h1>
            <input name="login" type="text" placeholder="username"/>
            <input name="pass" type="password" placeholder="password"/>
            <input name="rep_pass" type="password" placeholder="repeat password"/>
            <p>
                <button type="submit">Registration</button>
            </p>
            <p id="toLogin">Login</p>
        </form>
    </div>
</div>
</body>
</html>
