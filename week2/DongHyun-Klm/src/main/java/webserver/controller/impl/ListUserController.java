package webserver.controller.impl;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponder;
import http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.controller.AbstractController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
public class ListUserController extends AbstractController {

    @Override
    public void doGet(HttpRequest req, HttpResponder res) throws IOException {
        // 쿠키에서 로그인 여부 확인
        boolean loginYn = req.isLogin();

        log.debug("ListUserController.doGet() 호출, loginYn={}", loginYn);

        // 비로그인이라면 로그인 화면으로 redirect
        if(!loginYn) {
            res.sendRedirect("/user/login.html");
            return;
        }

        // 응답 객체 만들기
        HttpResponse resp = HttpResponse.builder()
                .statusCode(200).statusMessage("OK")
                .body(userListString().getBytes(StandardCharsets.UTF_8))
                .build();

        resp.getHeaders().put("Content-Type", "text/html;charset=utf-8");

        res.send(resp);
    }

    // 유저 목록 string 출력
    private String userListString() {
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>회원목록</title></head><body>");
        sb.append("<h1>회원 목록</h1><table border=\"1\">");
        sb.append("<tr><th>ID</th><th>이름</th><th>이메일</th></tr>");
        for (User u : users) {
            sb.append("<tr>")
                    .append("<td>").append(u.getUserId()).append("</td>")
                    .append("<td>").append(u.getName()).append("</td>")
                    .append("<td>").append(u.getEmail()).append("</td>")
                    .append("</tr>");
        }
        sb.append("</table></body></html>");

        return sb.toString();
    }
}
