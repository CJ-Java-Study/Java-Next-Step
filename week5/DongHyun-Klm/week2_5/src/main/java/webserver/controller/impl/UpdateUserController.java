package webserver.controller.impl;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponder;
import http.RequestParams;
import lombok.extern.slf4j.Slf4j;
import model.User;
import webserver.controller.AbstractController;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class UpdateUserController extends AbstractController {

    @Override
    public void doPost(HttpRequest req, HttpResponder res) throws IOException {
        RequestParams params = req.getRequestParams();

        User user = new User(
                params.getParameter("userId"),
                params.getParameter("password"),
                params.getParameter("name"),
                params.getParameter("email")
        );

        // 유저 정보 업데이트 후 홈으로 redirect
        log.info("회원정보 수정 요청 - user: {}", user);
        DataBase.updateUser(user);
        res.sendRedirect("/index.html");
    }
}
