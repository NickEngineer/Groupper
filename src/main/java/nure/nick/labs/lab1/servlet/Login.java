package nure.nick.labs.lab1.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "/")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("page/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");

        //if request is not from HttpServletRequest, you should do a typecast before
        HttpSession session = req.getSession();

        //setting session to expiry in 10 mins
        //session.setMaxInactiveInterval(10*60);


        if (login.equals("admin")) {
            session.setAttribute("role", "admin");
            System.out.println("admin");
        } else {
            session.setAttribute("role", "user");
            System.out.println("user");
        }

        session.setAttribute("login", login);
        Cookie message = new Cookie("message", "Welcome");
        resp.addCookie(message);

        //ServletContext context= getServletContext();
        // RequestDispatcher rd= context.getRequestDispatcher("page/index.jsp");
        // resp.sendRedirect();
        //rd.forward(req, resp);
        resp.sendRedirect("/main");
        //resp.sendRedirect("/main");
        /*req.getRequestDispatcher("/main").forward(req, resp);*/
    }
}
