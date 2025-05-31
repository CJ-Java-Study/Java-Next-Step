package next.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.db.DataBase;
import next.model.User;

@WebServlet("/users/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/user/login.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);

        // 레퍼보다 컴팩트, lazy or 활용
        if (user == null || !user.getPassword().equals(password)) {
            req.setAttribute("loginFailed", true);
            RequestDispatcher rd = req.getRequestDispatcher("/user/login.jsp");
            rd.forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
        resp.sendRedirect("/");
    }

}