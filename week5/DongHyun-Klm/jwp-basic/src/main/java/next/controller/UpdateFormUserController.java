package next.controller;

import core.db.DataBase;
import core.mvc.Controller;
import core.mvc.RequestMapping;
import next.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequestMapping("/user/updateForm")
public class UpdateFormUserController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // 로그인 세션 체크
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 로그인되지 않은 상태
            return "/user/login.jsp";
        }

        // 요청 파라미터 사용자 조회
        String requestedId = req.getParameter("userId");
        User requestUser = DataBase.findUserById(requestedId);

        User sessionUser = (User) session.getAttribute("user");
        if (!sessionUser.getUserId().equals(requestedId)) {
            // 다른 사용자의 정보 수정 시도 → 권한 없음 혹은 목록으로 리다이렉트
            return "redirect:/user/list";
        }

        req.setAttribute("user", requestUser);
        return "/user/updateForm.jsp";
    }
}
