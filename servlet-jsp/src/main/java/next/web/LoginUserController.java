package next.web;

import core.db.DataBase;
import lombok.extern.slf4j.Slf4j;
import next.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@WebServlet("/users/login")
public class LoginUserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 로그인 페이지로 리다이렉트
        resp.sendRedirect("/user/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        User user = DataBase.findUserById(userId);

        if(user != null && user.getPassword().equals(password)) {
            // 로그인 성공
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            log.info("User {} logged in successfully", userId);
            resp.sendRedirect("/");
        } else {
            // 로그인 실패
            log.warn("Login failed for userId: {}", userId);
            req.setAttribute("loginFailed", true);
            resp.sendRedirect("/user/login.jsp");
        }
    }
}
