package next.web;

import core.controller.Controller;
import core.db.DataBase;
import next.model.User;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class LoginController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);

        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }

        if (Objects.equals(user.getPassword(), password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }
}
