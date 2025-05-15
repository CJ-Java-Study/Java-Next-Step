package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;

@Slf4j
public class RequestHandler extends Thread {
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            String path = getDefaultPath(request.getPath());
            if ("/user/create".equals(path)) {
                createUser(request, response);
            } else if ("/user/login".equals(path)) {
                loginUser(request, response);
            } else if ("/user/list".equals(path)) {
                listUser(request, response);
            } else {
                response.forward(path);
            }
        } catch (
                IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createUser(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getParameterMap();
        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );
        log.debug("user : {}", user);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }

    private void loginUser(HttpRequest request, HttpResponse response) {
        Map<String, String> params = request.getParameterMap();
        User user = DataBase.findUserById(params.get("userId"));
        if (user != null && user.getPassword().equals(params.get("password"))) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }

    private void listUser(HttpRequest request, HttpResponse response) {
        if (request.isLoggedIn()) {
            response.sendRedirect("/user/login.html");
            return;
        }
        response.forwardBody(generateUserListHtml());
    }

    private String generateUserListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='table'>");
        DataBase.findAll().forEach(user -> {
            sb.append("<tr>")
                    .append(String.format("<td>%s</td>", user.getUserId()))
                    .append(String.format("<td>%s</td>", user.getName()))
                    .append(String.format("<td>%s</td>", user.getEmail()))
                    .append("</tr>");
        });
        sb.append("</table>");
        return sb.toString();
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }
}
