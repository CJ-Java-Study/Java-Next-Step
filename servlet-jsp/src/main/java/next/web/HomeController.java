package next.web;

import core.controller.Controller;
import core.db.DataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HomeController에서 "/"로 매핑한 후 localhost:8080/로 접속하면
 * HomeController가 호출될 것으로 예상되지만
 * webapp 디렉토리에 index.jsp가 존재하면 HomeController가 아니라 index.jsp가 호출된다
 * (index.jsp가 기본적으로 루트 경로에 매핑되어 있기 때문).
 * 따라서 HomeController에서 이동할 view의 이름을 "home.jsp"로 지정했다.
 */
public class HomeController implements Controller {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setAttribute("users", DataBase.findAll());
        return "home.jsp";
    }
}
