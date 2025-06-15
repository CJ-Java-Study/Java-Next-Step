package next.controller;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/user/list")
public class ListUserController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession(false);
        if(session == null || session.getAttribute("user") == null) {
            return "redirect:/user/login";
        }

        req.setAttribute("users", DataBase.findAll());
        return "/user/list.jsp";
    }

}
