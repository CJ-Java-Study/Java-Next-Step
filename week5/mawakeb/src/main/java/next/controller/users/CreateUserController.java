package next.controller.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.controller.common.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.db.DataBase;
import next.model.User;

public class CreateUserController extends AbstractController {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected String doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);

        return "redirect:/";
    }
}
