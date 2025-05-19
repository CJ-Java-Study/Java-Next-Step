package controller;

import db.DataBase;
import model.Http.HttpRequest;
import model.Http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(request.getParametersMap());
        DataBase.addUser(user);
        log.debug("USER CREATED: " + user);

        response.sendRedirect("/index.html");
    }

}

