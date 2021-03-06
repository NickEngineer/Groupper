var selectdUser = false;
var userId = null;
var usersLogin = null;

var selectedGroup = false;
var groupId = null;
var groupName = null;
var groupDescription = null;

var myUser;
var myUserLogin;

$(document).ready(function () {
        myUser = $('#userRole').html();
        myUserLogin = $('#userLogin').html();

        document.oncontextmenu = cmenu;

        getNumberOfUsers();

        function cmenu() {
            return false;
        }

        $('#users_form').submit(function () {
            return false;
        });
        validateUser();

        //users handling
        $('#users_block_table').on('click', 'tr', function (eventObject) {
            if ($(this).css("background-color") != "rgb(255, 165, 0)") {

                if (selectedGroup == false) {
                    $('#users_block_table tr').removeAttr("style");
                    $('#group_block_table tr').removeAttr("style");
                    $('#related_content_table tbody').html("");

                    $(this).css("background-color", "orange");
                    userId = $(this).find(".id").text();
                    usersLogin = $(this).find(".login").text();

                    selectdUser = true;

                    $('#content_related_block_header').html(`You selected: ` + usersLogin);
                    $('#content_related_block_desc').html('The user is in groups:');

                    getAllGroupsOfUser(userId);
                    createFormForEditionUsersInformation(usersLogin);
                } else {
                    if (myUser == "admin") {
                        $(this).css("background-color", "orange");
                        userId = $(this).find(".id").text();
                        usersLogin = $(this).find(".login").text();

                        addNewUserToGroup(groupId, userId);
                        $('#related_content_table tbody').append("<tr>\n" +
                            "                    <td class=\"id\">" + userId + "</td>\n" +
                            "                    <td class=\"login\">" + usersLogin + "</td>\n" +
                            "                </tr>");
                    }

                }
            } else if (selectdUser == true) {
                $('#users_block_table tr').removeAttr("style");
                $('#group_block_table tr').removeAttr("style");
                $('#related_content_table tbody').html("");
                selectdUser = false;

                createFormAddingNewUsers();
                $('#content_related_block_header').html('');
                $('#content_related_block_desc').html('');
            }
        });

        //groups handling
        $('#group_block_table').on('click', 'tr', function (eventObject) {
            if ($(this).css("background-color") != "rgb(255, 165, 0)") {

                if (selectdUser == false) {
                    $('#group_block_table tr').removeAttr("style");
                    $('#users_block_table tr').removeAttr("style");
                    $('#related_content_table tbody').html("");

                    $(this).css("background-color", "orange");
                    groupId = $(this).find(".id").text();
                    groupName = $(this).find(".name").text();
                    groupDescription = $(this).find(".description").text();

                    selectedGroup = true;
                    $('#content_related_block_header').html(`You selected: ` + groupName);
                    $('#content_related_block_desc').html('The group has some users:');

                    getAllUsersOfGroup(groupId);
                    createFormForEditionGroupsInformation(groupId);
                } else {
                    if (myUser == "admin" || myUserLogin == usersLogin) {
                        $(this).css("background-color", "orange");
                        groupId = $(this).find(".id").text();
                        groupName = $(this).find(".name").text();
                        groupDescription = $(this).find(".description").text();

                        addNewGroupToUser(groupId, userId);
                        $('#related_content_table tbody').append("<tr>\n" +
                            "                    <td class=\"id\">" + groupId + "</td>\n" +
                            "                    <td class=\"name\">" + groupName + "</td>\n" +
                            "                    <td class=\"description\">" + groupDescription + "</td>\n" +
                            "                </tr>");
                    }
                }

            } else if (selectedGroup == true) {
                $('#users_block_table tr').removeAttr("style");
                $('#group_block_table tr').removeAttr("style");
                $('#related_content_table tbody').html("");
                selectedGroup = false;

                createFormAddingNewGroup();
                $('#content_related_block_header').html('');
                $('#content_related_block_desc').html('');
            }
        });

        //delete users handler
        $('#users_block_table').on('contextmenu', 'tr', function (eventObject) {
            if (myUser == "admin") {
                userId = $(this).find(".id").text();
                $(this).remove();
                $('#related_content_table tbody tr td:contains(' + userId + ')').parent().remove();

                if (selectdUser) {
                    selectdUser = false;
                    $('#related_content_table tbody').html("");
                    $('#group_block_table tr').removeAttr("style");
                    $('#content_related_block_header').html('');
                    $('#content_related_block_desc').html('');
                }

                deleteUser(userId);

            }
        });

        //delete groups handler
        $('#group_block_table').on('contextmenu', 'tr', function (eventObject) {
            if (myUser == "admin") {
                userId = $(this).find(".id").text();
                $(this).remove();
                $('#related_content_table tbody tr td:contains(' + userId + ')').parent().remove();

                if (selectedGroup) {
                    selectedGroup = false;
                    $('#related_content_table tbody').html("");
                    $('#users_block_table tr').removeAttr("style");
                    $('#content_related_block_header').html('');
                    $('#content_related_block_desc').html('');
                }

                deleteGroup(groupId);
            }
        });


        //content table handler delete
        $('#related_content_table').on('contextmenu', 'tr', function (eventObject) {
            if (myUser == "admin" || (myUserLogin == usersLogin && selectdUser)) {
                var elementId = $(this).find(".id").text();
                $(this).remove();

                if (selectedGroup) {
                    deleteGroupsUser(groupId, elementId);
                    $('#users_block_table tbody tr td:contains(' + elementId + '):first').parent().removeAttr("style");
                } else {
                    deleteUsersGroup(userId, elementId);
                    $('#group_block_table tbody tr td:contains(' + elementId + '):first').parent().removeAttr("style");
                }
            }
        });

    }
);

function validateUser() {
    $('#users_form').submit(function () {
        var usersLogin = $('#users_form input:first').val();
        var usersPassword = $('#users_form input[name = password]').val();

        if (usersLogin.length < 3 || usersPassword.length <= 3) {
            $('#users_form input:first').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
            $('#users_form input[name=password]').css("box-shadow",
                "-1px 3px 88px 0px rgba(237,14,14,1)");
        } else {
            $.post(
                "/action",
                {
                    action: "isLogin",
                    userLogin: usersLogin,
                    usersPassword: usersPassword
                },
                onAjaxSucces
            );

            function onAjaxSucces(data) {
                console.log(data);
                if (!("true" == data)) {
                    $('#users_form').unbind();

                    $('#usersBlockButton').click();
                } else {
                    $('#users_form input:first').css("box-shadow",
                        "-1px 3px 88px 0px rgba(237,14,14,1)");
                    $('#users_form input[name=password]').css("box-shadow",
                        "-1px 3px 88px 0px rgba(237,14,14,1)");
                }
            }
        }

        return false;
    });
}

function deleteUser(id) {
    $.post(
        "/action",
        {
            action: "deleteUser",
            userId: id
        },
        onAjaxSuccess
    );

    function onAjaxSuccess(data) {
        getNumberOfUsers();
    }
}

function deleteGroup(id) {
    $.post(
        "/action",
        {
            action: "deleteGroup",
            groupId: id
        },
        onAjaxSuccess
    );

    function onAjaxSuccess(data) {
    }
}

function getAllGroupsOfUser(userId) {
    $.post(
        "/action",
        {
            action: "getAllGroupsOfUser",
            userId: userId
        },
        onAjaxSuccess
    );

    function onAjaxSuccess(data) {
        console.log(data);
        console.log(data.length);

        var obj;
        var index;
        for (index = 0; index < data.length; ++index) {

            obj = data[index];

            paintSelectedGroups(obj.id);

            $('#related_content_table tbody').append("<tr>\n" +
                "                    <td class=\"id\">" + obj.id + "</td>\n" +
                "                    <td class=\"name\">" + obj.name + "</td>\n" +
                "                    <td class=\"description\">" + obj.descript + "</td>\n" +
                "                </tr>");

        }
    }

    function paintSelectedGroups(groupIdSelected) {
        $('#group_block_table tr').find('[id = ' + groupIdSelected + ']')
            .parent().css("background-color", "orange");
    }
}


function addNewGroupToUser(groupIdSelected, userIdSelected) {
    $.post(
        "/action",
        {
            action: "addNewGroupToUser",
            userId: userIdSelected,
            groupId: groupIdSelected
        }
    );
}

function deleteUsersGroup(usersIdSelected, groupIdSelected) {
    $.post(
        "/action",
        {
            action: "deleteUsersGroup",
            usersIdSelected: usersIdSelected,
            groupIdSelected: groupIdSelected
        }
    );
}

function getAllUsersOfGroup(groupIdSelected) {
    $.post(
        "/action",
        {
            action: "getAllUsersOfGroup",
            groupId: groupIdSelected
        },
        onAjaxSuccess
    );

    function onAjaxSuccess(data) {
        console.log(data);
        console.log(data.length);

        var obj;
        var index;
        for (index = 0; index < data.length; ++index) {

            obj = data[index];

            paintSelectedUsers(obj.id);

            $('#related_content_table tbody').append("<tr>\n" +
                "                    <td class=\"id\">" + obj.id + "</td>\n" +
                "                    <td class=\"name\">" + obj.login + "</td>\n" +
                "                </tr>");

        }
    }

    function paintSelectedUsers(userIdSelectedP) {
        $('#users_block_table tr').find('[id = ' + userIdSelectedP + ']')
            .parent().css("background-color", "orange");
    }
}


function deleteGroupsUser(groupIdSelected, usersIdSelected) {
    $.post(
        "/action",
        {
            action: "deleteUsersGroup",
            usersIdSelected: usersIdSelected,
            groupIdSelected: groupIdSelected
        }
    );
}

function addNewUserToGroup(groupIdSelected, userIdSelected) {
    $.post(
        "/action",
        {
            action: "addNewGroupToUser",
            userId: userIdSelected,
            groupId: groupIdSelected
        }
    );
}


function createFormForEditionUsersInformation(usersLoginValue) {
    $('#users_form_title').html("Change information");

    $('#users_form input:first').val(usersLoginValue);

    $("#users_form").submit(function () {
        updateUser(usersLoginValue);
        return false;
    });

    $('#usersBlockButton').html("Edit");
}

function createFormAddingNewUsers() {
    $('#users_form_title').html("Addition a new user");

    $('#users_form input:first').val("");
    $('#users_form input[name = password]').val("");

    $("#users_form").unbind();

    $('#usersBlockButton').html("Add");
}

function updateUser(usersLoginValue) {

    var usersLogin = $('#users_form input:first').val();
    var usersPassword = $('#users_form input[name = password]').val();

    $('#content_related_block_header').html("You selected: " + usersLogin);

    $('#users_block_table td:contains("' + usersLoginValue + '"):first').html(usersLogin);
    $('#users_block_table td:contains("' + usersLoginValue + '"):first').next().html(usersPassword);

    $.post(
        "/action",
        {
            action: "updateUser",
            userId: userId,
            userLogin: usersLogin,
            userPassword: usersPassword
        }
    );
}


function createFormForEditionGroupsInformation(groupsLoginValue) {
    $('#groups_form_title').html("Change information");

    $('#groups_form input:first').val(
        $('#group_block_table td:contains("' + groupsLoginValue + '"):first').next().html()
    );


    $("#groups_form").submit(function () {
        updateGroup(groupsLoginValue);
        return false;
    });

    $('#groupsBlockButton').html("Edit");
}

function createFormAddingNewGroup() {
    $('#groups_form_title').html("Addition a new group");

    $('#groups_form input:first').val("");
    var groupDesc = $('#groups_form input[name = descript]').val("");

    $("#groups_form").unbind();

    $('#usersBlockButton').html("Add");
}

function updateGroup(groupsLoginValue) {

    var groupsName = $('#groups_form input:first').val();
    var groupDesc = $('#groups_form input[name = descript]').val();

    $('#content_related_block_header').html("You selected: " + groupsName);

    $('#group_block_table td:contains("' + groupsLoginValue + '"):first').next().html(groupsName);
    $('#group_block_table td:contains("' + groupsLoginValue + '"):first').next().next().html(groupDesc);

    $.post(
        "/action",
        {
            action: "updateGroup",
            groupId: groupsLoginValue,
            name: groupsName,
            descript: groupDesc
        }
    );
}

function getNumberOfUsers() {
    $.post(
        "/action",
        {
            action: "getNumberOfUsers"
        },
        onSuccess
    );

    function onSuccess(data) {
        console.log(data);
        if (data == -777) {
            $('#number_of_users').html("no");
        } else {
            $('#number_of_users').html(data);
        }
    }
}