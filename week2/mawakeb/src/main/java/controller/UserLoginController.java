package controller;

import db.DataBase;
import model.Http.HttpRequest;
import model.Http.HttpResponse;
import model.Http.HttpSession;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static model.Http.HttpSessionUtil.USER_SESSION_KEY;

public class UserLoginController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        Map<String, String> paramMap = request.getParametersMap();
        User user = login(paramMap.get("userId"), paramMap.get("password"));

        if (user == null) {
            response.sendRedirect("/user/login_failed.html");
            log.debug("LOGIN FAILED");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, user);
        response.sendRedirect("/index.html");
        log.debug("LOGIN SUCCESS: " + user.getUserId());
    }

    private static User login(String userId, String password){
        User user = DataBase.findUserById(userId);
        if (user != null && user.getPassword().equals(password)){
            return user;
        }

        return null;
    }


}

