package webserver;

import db.DataBase;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseBuilder;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpResponseBuilder.responseBody;

public class UserRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(UserRequestHandler.class);

    // POST user/create 처리
    protected static void handleCreateUser(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        Map<String, String> paramMap = HttpRequestUtils.parseQueryString(httpRequest.getBody());

        User user = new User(paramMap);
        DataBase.addUser(user);
        log.debug("USER CREATED: " + user);

        HttpResponseBuilder.response302Header(dos, "/index.html");
    }

    // POST user/login 처리
    protected static void handleLoginUser(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        Map<String, String> paramMap = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        User user = login(paramMap.get("userId"), paramMap.get("password"));

        if (user == null) {
            byte[] body = Files.readAllBytes(new File("./week2/mawakeb/webapp/user/login_failed.html").toPath());
            HttpResponseBuilder.response200Header(dos, body.length, false,"logined=false");
            responseBody(dos, body);
            log.debug("LOGIN FAILED");
            return;
        }

        byte[] body = Files.readAllBytes(new File("./week2/mawakeb/webapp/index.html").toPath());
        HttpResponseBuilder.response200Header(dos, body.length, false, "logined=true");
        responseBody(dos, body);
        log.debug("LOGIN SUCCESS: " + user.getUserId());
    }

    protected static User login(String userId, String password){
        User user = DataBase.findUserById(userId);
        if (user != null && user.getPassword().equals(password)){
            return user;
        }

        return null;
    }
}
