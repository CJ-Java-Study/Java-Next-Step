package next.web;

import core.db.DataBase;
import next.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user/update")
public class UpdateUserFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 로그인 세션 체크
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 로그인되지 않은 상태
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        // 요청 파라미터 사용자 조회
        String requestedId = req.getParameter("userId");
        User requestUser = DataBase.findUserById(requestedId);

        if (requestUser == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        if (!sessionUser.getUserId().equals(requestedId)) {
            // 다른 사용자의 정보 수정 시도 → 권한 없음 혹은 목록으로 리다이렉트
            resp.sendRedirect("/user/list");
            return;
        }

        req.setAttribute("user", requestUser);
        RequestDispatcher rd = req.getRequestDispatcher("/user/update.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String userId   = req.getParameter("userId");
        String password = req.getParameter("password");
        String name     = req.getParameter("name");
        String email    = req.getParameter("email");

        User updatedUser = new User(userId, password, name, email);
        DataBase.updateUser(updatedUser);
        session.setAttribute("user", updatedUser);

        resp.sendRedirect("/user/list");
    }
}

