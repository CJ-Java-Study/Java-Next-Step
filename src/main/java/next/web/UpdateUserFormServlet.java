package next.web;

import core.db.DataBase;
import lombok.extern.slf4j.Slf4j;
import next.dto.UserUpdateRequest;
import next.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@WebServlet("/user/update")
public class UpdateUserFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);

        HttpSession session = req.getSession();
        if(session.getAttribute("user") != user) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        req.setAttribute("user", user);
        RequestDispatcher rd = req.getRequestDispatcher("/user/update.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);

        HttpSession session = req.getSession();
        if(session.getAttribute("user") != user) {
            throw new IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        if (user == null) {
            log.warn("User with id {} not found", userId);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }

        user.update(new UserUpdateRequest(
                req.getParameter("password"),
                req.getParameter("name"),
                req.getParameter("email")
        ));

        log.debug("Updated user: {}", user);
        resp.sendRedirect("/user/list");
    }
}
