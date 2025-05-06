package webserver;

import db.DataBase;
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

public class UserRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(UserRequestHandler.class);
    private static final ResponseUtils responseUtils = new ResponseUtils();

    public void handleUserCreate(BufferedReader bufferedReader, int contentLength, DataOutputStream dos) throws IOException {
        String body = IOUtils.readData(bufferedReader, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(body); // userId=nitronium&password=cjjd
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        log.debug("user : {}", user);
        DataBase.addUser(user);

        // 302 리다이렉트 응답
        responseUtils.send302Redirect(dos, "/index.html");
    }

    public void handleUserLogin(BufferedReader bufferedReader, int contentLength, DataOutputStream dos) throws IOException {
        String body = IOUtils.readData(bufferedReader, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);

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
