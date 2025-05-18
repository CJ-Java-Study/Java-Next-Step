package controller;

import db.DataBase;
import model.User;
import util.HttpRequest;
import util.HttpResponse;

public class LoginController extends AbstractController{
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) throws Exception {
        User user = DataBase.findUserById(request.getParameter("userId"));
        if (user != null && user.getPassword().equals(request.getParameter("password"))) {
            response.addCookie("logined", "true");
            response.sendRedirect("/index.html");
        } else {
            response.addCookie("logined", "false");
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
