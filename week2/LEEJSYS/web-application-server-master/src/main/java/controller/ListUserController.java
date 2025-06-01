package controller;

import db.DataBase;
import model.User;
import util.HttpRequest;
import util.HttpRequestUtils;
import util.HttpResponse;

import java.util.Collection;
import java.util.Map;

public class ListUserController extends AbstractController{
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws Exception {
        String cookieHeader = request.getHeader("Cookie");
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieHeader);
        boolean isLogined = Boolean.parseBoolean(cookies.getOrDefault("logined", "false"));

        if (!isLogined) {
            response.sendRedirect("/user/login.html");
            return;
        }

        Collection<User> users = DataBase.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h1>사용자 목록</h1>");
        sb.append("<ul>");
        for (User user : users) {
            sb.append("<li>")
                    .append(user.getUserId())
                    .append(" - ")
                    .append(user.getName())
                    .append("</li>");
        }
        sb.append("</ul>");
        sb.append("</body></html>");

        response.forwardBody(sb.toString());
    }
}
