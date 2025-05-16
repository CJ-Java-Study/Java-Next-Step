package webserver;

import db.DataBase;
import model.Http.HttpRequest;
import model.Http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class UserRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(UserRequestHandler.class);

    // POST user/create 처리
    protected static void handleCreateUser(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        User user = new User(httpRequest.getParametersMap());
        DataBase.addUser(user);
        log.debug("USER CREATED: " + user);

        httpResponse.sendRedirect("/index.html");
    }

    // POST user/login 처리
    protected static void handleLoginUser(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Map<String, String> paramMap = httpRequest.getParametersMap();
        User user = login(paramMap.get("userId"), paramMap.get("password"));

        if (user == null) {
            httpResponse.addHeader("Set-Cookie", "logined=false");
            httpResponse.sendRedirect("/user/login_failed.html");
            log.debug("LOGIN FAILED");
            return;
        }

        httpResponse.addHeader("Set-Cookie", "logined=true");
        httpResponse.sendRedirect("/index.html");
        log.debug("LOGIN SUCCESS: " + user.getUserId());
    }

    // GET user/list 처리
    protected static void handleGetUserList(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String isLoggedIn = httpRequest.getCookie("logined");

        if (Boolean.parseBoolean(isLoggedIn)) {
            String userList = getUserList();
            String body = Files.readString(new File("./week2/mawakeb/webapp/user/list.html").toPath());
            body = body.replace("<!-- USER_TABLE_PLACEHOLDER -->", userList);
            httpResponse.forwardBody(body);
            log.debug("GET USER LIST SUCCESS");
            return;
        }

        httpResponse.sendRedirect("/user/login.html");
        log.debug("LOGIN REQUIRED");
    }


    private static User login(String userId, String password){
        User user = DataBase.findUserById(userId);
        if (user != null && user.getPassword().equals(password)){
            return user;
        }

        return null;
    }

    protected static String getUserList(){
        List<User> users = DataBase.findAll().stream().toList();

        StringBuilder table = new StringBuilder();
        table.append("<table class='table table-hover'>")
                .append("<thead><tr><th>#</th><th>사용자 아이디</th><th>이름</th><th>이메일</th><th></th></tr></thead>")
                .append("<tbody>");

        for (User user : users) {
            table.append("<tr>")
                    .append("<th scope='row'>").append(user.getUserId()).append("</th>")
                    .append("<td>").append(user.getName()).append("</td>")
                    .append("<td>").append(user.getEmail()).append("</td>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }
        table.append("</tbody></table>");

        return table.toString();
    }
}
