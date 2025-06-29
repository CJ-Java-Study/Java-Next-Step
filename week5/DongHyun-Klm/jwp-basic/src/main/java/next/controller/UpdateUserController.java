package next.controller;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import next.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@RequestMapping("/user/update")
public class UpdateUserController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String userId   = req.getParameter("userId");
        String password = req.getParameter("password");
        String name     = req.getParameter("name");
        String email    = req.getParameter("email");

        User updatedUser = new User(userId, password, name, email);
        DataBase.updateUser(updatedUser);
        log.debug("Update User : {}", updatedUser);
        session.setAttribute("user", updatedUser);

        return "redirect:/user/list";
    }
}
