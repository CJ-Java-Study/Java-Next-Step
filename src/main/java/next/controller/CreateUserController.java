package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.Controller;
import next.model.User;

import java.sql.SQLException;

public class CreateUserController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("userId={}, password={}, name={}, email={}",
                user.getUserId(), user.getPassword(), user.getName(), user.getEmail());

        UserDao userDao = new UserDao();
        try{
            userDao.insert(user);
            User entity = userDao.findByUserId(user.getUserId());
            log.debug("userId-db={}, password-db={}, name-db={}, email-db={}",
                    entity.getUserId(), entity.getPassword(), entity.getName(), entity.getEmail());
        } catch(SQLException e){
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
}
