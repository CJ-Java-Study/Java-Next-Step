package next.web;

import core.controller.Controller;
import core.db.DataBase;
import lombok.extern.slf4j.Slf4j;
import next.model.User;
import next.util.UserSessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UpdateFormUserController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }
        req.setAttribute("user", user);
        return "/user/updateForm.jsp";
    }
}
