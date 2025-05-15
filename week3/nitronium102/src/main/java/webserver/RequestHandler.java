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
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            String path = httpRequest.getPath();

            if ("/user/create".equals(path)) {
                Map<String, String> params = httpRequest.getParameterMap();
                User user = new User(
                        params.get("userId"),
                        params.get("password"),
                        params.get("name"),
                        params.get("email")
                );
                log.debug("user : {}", user);
                DataBase.addUser(user);
                response.sendRedirect("/index.html");
            } else if ("/user/login".equals(path)) {
                Map<String, String> params = httpRequest.getParameterMap();
                User user = DataBase.findUserById(params.get("userId"));
                if (user != null && user.getPassword().equals(params.get("password"))) {
                    response.addHeader("Set-Cookie", "logined=true");
                    response.sendRedirect("/index.html");
                } else {
                    response.sendRedirect("/user/login_failed.html");
                }
            } else if ("/user/list".equals(path)) {
                if (httpRequest.isLoggedIn()) {
                    response.sendRedirect("/user/login.html");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (User user : DataBase.findAll()) {
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("<tr>");
                }
                response.forwardBody(sb.toString());
            } else {
                response.forward(path);
            }
        } catch (
                IOException e) {
            log.error(e.getMessage());
        }
    }
}
