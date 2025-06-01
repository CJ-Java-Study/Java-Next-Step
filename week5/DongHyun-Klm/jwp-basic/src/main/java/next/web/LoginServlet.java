package next.web;

import core.db.DataBase;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User sessionUser = (User) req.getSession().getAttribute("user");
        if (sessionUser != null) {
            resp.sendRedirect(req.getContextPath() + "/user/list");
            return;
        }
        req.getRequestDispatcher("/user/login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        // DataBase에서 사용자 조회
        User user = DataBase.findUserById(userId);

        // 로그인 검증
        if (user != null && user.getPassword().equals(password)) {
            // 세션에 로그인 정보 저장
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            log.info("세션 생성 - sessionId: {}, userId: {}", session.getId(), user.getUserId());

            resp.sendRedirect("/user/list");
        } else {
            // 로그인 실패
            req.getRequestDispatcher("login_failed.html")
                    .forward(req, resp);
        }
    }
}
