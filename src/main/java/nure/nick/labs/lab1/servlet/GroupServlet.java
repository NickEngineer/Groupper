package nure.nick.labs.lab1.servlet;

import nure.nick.labs.lab1.db.DBConnector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "/group")
public class GroupServlet extends HttpServlet {

    DBConnector dbConnector = DBConnector.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dbConnector.addGroup(
                req.getParameter("name"),
                req.getParameter("descript"));
        resp.sendRedirect("/main");
    }
}
