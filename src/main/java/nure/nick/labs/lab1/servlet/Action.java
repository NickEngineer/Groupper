package nure.nick.labs.lab1.servlet;

import com.google.gson.Gson;
import nure.nick.labs.lab1.db.DBConnector;
import nure.nick.labs.lab1.element.Group;
import nure.nick.labs.lab1.element.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "/action")
public class Action extends HttpServlet {

    DBConnector dbConnector = DBConnector.getInstance();
    String userId;
    String groupId;
    String json;
    User user;
    String answer;
    String usersPassword;
    String usersLogin;
    String groupName;
    String groupDescription;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        switch (req.getParameter("action")) {
            case "deleteUser":
                userId = req.getParameter("userId");
                dbConnector.deleteUser(userId);
                break;
            case "deleteGroup":
                groupId = req.getParameter("groupId");
                dbConnector.deleteGroup(groupId);
                break;
            case "getAllGroupsOfUser":
                userId = req.getParameter("userId");

                List<Group> groupList = dbConnector.getAllGroupsOfUser(userId);

                json = new Gson().toJson(groupList);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(json);
                break;
            case "addNewGroupToUser":
                groupId = req.getParameter("groupId");
                userId = req.getParameter("userId");
                dbConnector.addNewGroupToUser(groupId, userId);
                break;
            case "deleteUsersGroup":
                String usersIdSelected = req.getParameter("usersIdSelected");
                String groupIdSelected = req.getParameter("groupIdSelected");
                dbConnector.deleteUsersGroup(usersIdSelected, groupIdSelected);
                break;
            case "getAllUsersOfGroup":
                groupId = req.getParameter("groupId");

                List<User> usersList = dbConnector.getAllUsersOfGroup(groupId);

                json = new Gson().toJson(usersList);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(json);
                break;
            case "updateUser":
                userId = req.getParameter("userId");
                String login = req.getParameter("userLogin");
                String password = req.getParameter("userPassword");

                dbConnector.updateUser(userId, login, password);
                break;
            case "updateGroup":
                groupId = req.getParameter("groupId");
                groupName = req.getParameter("name");
                groupDescription = req.getParameter("descript");

                dbConnector.updateGroup(groupId, groupName, groupDescription);
                break;
            case "getUserByLogin":
                usersLogin = req.getParameter("userLogin");

                user = dbConnector.getUserByLogin(usersLogin);
                json = new Gson().toJson(user);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(json);
                break;
            case "isUser":
                usersLogin = req.getParameter("userLogin");
                usersPassword = req.getParameter("usersPassword");


                answer = "false";
                user = dbConnector.getUserByLogin(usersLogin);

                if (!(user.getId() == null) &&
                        usersLogin.equals(user.getLogin()) &&
                        usersPassword.equals(user.getPassword())) {
                    answer = "true";
                }

                if (usersLogin.equals("admin") &&
                        usersPassword.equals("7777777")) {
                    answer = "true";
                }

                resp.getWriter().write(answer);
                break;
            case "isLogin":
                usersLogin = req.getParameter("userLogin");


                answer = "false";
                user = dbConnector.getUserByLogin(usersLogin);

                if (!(user.getId() == null) &&
                        usersLogin.equals(user.getLogin())) {
                    answer = "true";
                }

                if (usersLogin.equals("admin")) {
                    answer = "true";
                }

                resp.getWriter().write(answer);
                break;
            case "addNewUser":
                usersLogin = req.getParameter("userLogin");
                usersPassword = req.getParameter("usersPassword");


                answer = "false";
                user = dbConnector.getUserByLogin(usersLogin);

                if (user.getId() == null) {
                    dbConnector.addUser(usersLogin, usersPassword);
                    answer = "true";
                }

                resp.getWriter().write(answer);
                break;
            case "getNumberOfUsers":
                int numberOfUsers;
                numberOfUsers = dbConnector.getNumberOfUsers();
                resp.getWriter().write(String.valueOf(numberOfUsers));
                break;
        }
    }
}
