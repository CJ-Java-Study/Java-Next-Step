package controller;

import db.DataBase;
import model.Http.HttpRequest;
import model.Http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserListController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(UserListController.class);

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        try {
            String isLoggedIn = request.getCookie("logined");

            if (Boolean.parseBoolean(isLoggedIn)) {
                String userList = getUserList();
                Path filePath = FileUtil.loadPath("/user/list.html");
                String body = Files.readString(filePath);
                body = body.replace("<!-- USER_TABLE_PLACEHOLDER -->", userList);
                response.forwardBody(body);
                log.debug("GET USER LIST SUCCESS");
                return;
            }

            response.sendRedirect("/user/login.html");
            log.debug("LOGIN REQUIRED");
        } catch (Exception e){
            log.error(e.getMessage());
        }
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

