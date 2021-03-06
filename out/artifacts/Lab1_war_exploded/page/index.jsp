<%--
  Created by IntelliJ IDEA.
  User: Nick
  Date: 28.10.2018
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Main</title>

    <link href=' http://fonts.googleapis.com/css?family=Tangerine' rel='stylesheet' type='text/css'>
    <link href=' http://fonts.googleapis.com/css?family=Quicksand' rel='stylesheet' type='text/css'>
    <link href=' /resources/style/index.css' rel='stylesheet' type='text/css'>


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <script src="<c:url value="/resources/js/index.js" />"></script>


</head>
<body>

<c:if test="${not empty sessionScope.login}">
    <div id="menu">
        <h2 class="status_menu_element">You are logged as
            <span id="userLogin">${sessionScope.login}</span>
        </h2>
        <form class="exit-form status_menu_element" method="post" action="/main">
            <input class="status_menu_element" name="exit" value="true" type="hidden" placeholder="username"/>
            <button class="status_menu_element" type="submit">Exit</button>
        </form>
    </div>
</c:if>


<div id="users_block" class="data_container">
    <c:if test="${not empty sessionScope.role}">
        <h1 id="user_type">You are <span id="userRole">${sessionScope.role}</span></h1>
    </c:if>

    <div class="container">
        <c:if test="${sessionScope.role == 'admin'}">
            <h2 id="users_form_title">Addition a new user</h2>
            <form class="login-form" id="users_form" method="post" action="/main">
                <input name="login" type="text" placeholder="username"/>
                <input name="password" type="password" placeholder="password"/>
                <button id="usersBlockButton" type="submit">Add</button>

            </form>
        </c:if>
    </div>

    <h2>Users(<span id="number_of_users"></span>):</h2>
    <c:if test="${not empty users}">
        <c:if test="${sessionScope.role == 'admin'}">
            <table id="users_block_table">
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td class="id" id="${user.id }">${user.id }</td>
                        <td class="login">${user.login }</td>
                        <td class="password">${user.password }</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${sessionScope.role != 'admin'}">
            <table id="users_block_table">
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td class="id" id="${user.id }">${user.id }</td>
                        <td class="login">${user.login }</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </c:if>
</div>

<div id="related_content_block" class="data_container">
    <h2>Related Content:</h2>

    <h3 id="content_related_block_header"></h3>
    <h4 id="content_related_block_desc"></h4>
    <table id="related_content_table">
        <tbody>

        </tbody>
    </table>
</div>

<div id="group_block" class="data_container">
    <div class="container">
        <c:if test="${sessionScope.role == 'admin'}">
            <h2 id="groups_form_title">Addition a new group</h2>
            <form class="add_group_form" id="groups_form" method="post" action="/group">
                <input name="name" type="text" placeholder="name"/>
                <input name="descript" type="text" placeholder="description"/>
                <button type="submit" id="groupsBlockButton">Add</button>

            </form>
        </c:if>
    </div>

    <h2>Groups:</h2>
    <c:if test="${not empty groups}">
        <table id="group_block_table">
            <c:forEach items="${groups}" var="group">
                <tr>
                    <td class="id" id="${group.id }">${group.id }</td>
                    <td class="name">${group.name }</td>
                    <td class="description">${group.descript }</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</div>

</body>
</html>
