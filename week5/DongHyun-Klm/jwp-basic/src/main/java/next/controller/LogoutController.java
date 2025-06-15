package next.controller;

import core.mvc.Controller;
import core.mvc.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/user/logout")
public class LogoutController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession(false);
        if (session != null) {
            // 현재 세선 무효화
            session.invalidate();
        }
        return "redirect:/";
    }
}
