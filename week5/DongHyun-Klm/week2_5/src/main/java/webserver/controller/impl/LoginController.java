package webserver.controller.impl;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponder;
import http.HttpResponse;
import http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.controller.AbstractController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class LoginController extends AbstractController {

    @Override
    public void doPost(HttpRequest req, HttpResponder res) throws IOException {
        String userId   = req.getParameter("userId");
        String password = req.getParameter("password");
        log.info("로그인 요청 - userId: {}, password: {}", userId, password);

        User user = DataBase.findUserById(userId);
        log.info("요청 사용자 - {}", user);

        if(user != null && user.getPassword().equals(password)) { // 로그인 성공
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            res.sendRedirect("/index.html");
        } else { // 로그인 실패
            byte[] body = Files.readAllBytes(new File("/user/login_failed.html").toPath());
            Map<String, String> headers = new HashMap<>();
            headers.put("Set-Cookie", "logined=true");

            // 응답 객체 만들기
            HttpResponse resp = HttpResponse.builder()
                    .statusCode(401).statusMessage("Unauthorized")
                    .headers(headers)
                    .body(body)
                    .build();

            res.send(resp);
        }
    }
}
