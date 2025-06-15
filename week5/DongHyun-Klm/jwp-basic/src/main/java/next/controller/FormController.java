package next.controller;

import core.mvc.Controller;
import core.mvc.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/user/form")
public class FormController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return "/user/form.jsp";
    }
}
