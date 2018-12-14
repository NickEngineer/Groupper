$(document).ready(function () {

    setToRegistration();
    setToLogin();

    checkLogin();
    checkRegistration();
});

function setToRegistration() {
    $('#toRegistration').click(function () {
        $('#login_form').css('display', 'none');
        $('#registration_form').css('display', 'block');
    })
}

function setToLogin() {
    $('#toLogin').click(function () {
        $('#login_form').css('display', 'block');
        $('#registration_form').css('display', 'none');
    })
}

function checkLogin() {

    $('#login_form').submit(function () {
        return false;
    });

    $('#login_form').submit(function () {
        var login = $('#login_form input[name=login]').val();
        var password = $('#login_form input[name=pass]').val();

        if (login.length < 3 || password.length <= 3) {
            var login = $('#login_form input[name=login]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
            var password = $('#login_form input[name=pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
        } else {
            isUser(login, password);
        }


        return false;
    });
}

function isUser(usersLogin, usersPassword) {
    $.post(
        "/action",
        {
            action: "isUser",
            userLogin: usersLogin,
            usersPassword: usersPassword
        },
        onAjaxSucces
    );

    function onAjaxSucces(data) {
        console.log(data);
        if ("true" == data) {
            $('#login_form').unbind();
            $('#login_form').submit();
            $('#login_button').click();
        } else {
            var login = $('#login_form input[name=login]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
            var password = $('#login_form input[name=pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
        }
    }
}


function checkRegistration() {
    $('#registration_form').submit(function () {
        return false;
    });

    $('#registration_form').submit(function () {
        var login = $('#registration_form input[name=login]').val();
        var password = $('#registration_form input[name=pass]').val();
        var rePassword = $('#registration_form input[name=rep_pass]').val();

        if (login.length < 3 || password.length <= 3) {
            var login = $('#registration_form input[name=login]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
            var password = $('#registration_form input[name=pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
        } else if (password != rePassword) {
            var login = $('#registration_form input[name=rep_pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
            var password = $('#registration_form input[name=pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
        } else {
            addNewUser(login, password);
        }

        return false;
    });
}

function addNewUser(usersLogin, usersPassword) {
    $.post(
        "/action",
        {
            action: "addNewUser",
            userLogin: usersLogin,
            usersPassword: usersPassword
        },
        onAjaxSucces
    )
    ;

    function onAjaxSucces(data) {
        console.log(data);
        if ("true" == data) {
            var login = $('#registration_form input[name=login]').css("box-shadow",
                "-1px 3px 88px 0px green");
            var login = $('#registration_form input[name=rep_pass]').css("box-shadow",
                "-1px 3px 88px 0px green");
            var password = $('#registration_form input[name=pass]').css("box-shadow",
                "-1px 3px 88px 0px green");

            window.location.reload(true);
        } else {
            var login = $('#registration_form input[name=rep_pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
            var password = $('#registration_form input[name=pass]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
        }
    }
}