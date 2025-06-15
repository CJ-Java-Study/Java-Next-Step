package next.controller;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/user/login")
@Slf4j
public class LoginController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");

        // DataBase에서 사용자 조회
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }

        // 로그인 검증
        if (user.getPassword().equals(password)) {
            // 세션에 로그인 정보 저장
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            log.info("세션 생성 - sessionId: {}, userId: {}", session.getId(), user.getUserId());
            return "redirect:/";
        }
        // 로그인 실패
        req.setAttribute("loginFailed", true);
        return "/user/login.jsp";

    }
}
