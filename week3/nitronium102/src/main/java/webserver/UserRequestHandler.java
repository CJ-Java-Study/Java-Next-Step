package webserver;

import db.DataBase;
import http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.ResponseUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class UserRequestHandler {
    private static final ResponseUtils responseUtils = new ResponseUtils();

    public void handleUserCreate(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        // 파라미터를 HttpRequest에서 바로 꺼냄
        Map<String, String> params = httpRequest.getParameterMap();
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        log.debug("user : {}", user);
        DataBase.addUser(user);

        responseUtils.send302Redirect(dos, "/index.html");
    }

    public void handleUserLogin(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        Map<String, String> params = httpRequest.getParameterMap();
        User user = DataBase.findUserById(params.get("userId"));
        if (user != null && user.getPassword().equals(params.get("password"))) {
            responseUtils.send302RedirectWithCookie(dos, "/index.html", "logined=true");
        } else {
            responseUtils.send302RedirectWithCookie(dos, "/user/login_failed.html", "logined=false");
        }
    }

    public void handleUserList(DataOutputStream dos) {
        StringBuilder sb = new StringBuilder();
        for (User user : DataBase.findAll()) {
            sb.append(user.toString()).append("\n");
        }
        responseUtils.send200Response(dos, sb.toString().getBytes());
    }
}
