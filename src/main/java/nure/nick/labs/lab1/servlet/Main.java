package nure.nick.labs.lab1.servlet;

import nure.nick.labs.lab1.db.DBConnector;
import nure.nick.labs.lab1.element.Group;
import nure.nick.labs.lab1.element.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "/main")
public class Main extends HttpServlet {

    DBConnector dbConnector = DBConnector.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!request.getParameterMap().containsKey("exit")) {
            dbConnector.addUser(
                    request.getParameter("login"),
                    request.getParameter("password"));
            response.sendRedirect("/main");
        } else {
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session.getAttribute("login") != null) {

                List<User> myUsers = dbConnector.getAllUsers();
                List<Group> myGroups = dbConnector.getAllGroups();

                request.setAttribute("users", myUsers);
                request.setAttribute("groups", myGroups);

                request.getRequestDispatcher("page/index.jsp").forward(request, response);

            } else {
                response.sendRedirect("/");
            }


        } catch (Exception ex) {
        }


    }
}