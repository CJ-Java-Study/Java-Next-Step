package next.webserver;

import next.controller.common.Controller;
import next.controller.HomeController;
import next.controller.common.ForwardController;
import next.controller.users.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("", new HomeController());

        // 단순 포워딩
        controllers.put("/users/form", new ForwardController("/user/form.jsp"));
        controllers.put("/users/loginForm", new ForwardController("/user/login.jsp"));

        // 로그인, 로그아웃
        controllers.put("/users/login", new LoginController());
        controllers.put("/users/logout", new LogoutController());

        // 유저 생성, 수정
        controllers.put("/users/create", new CreateUserController());
        controllers.put("/users/update", new UpdateUserController());
        controllers.put("/users/updateForm", new UpdateUserController());

        // 유저 조회
        controllers.put("/users", new ListUserController());
        controllers.put("/users/profile", new ProfileController());
    }

    public static Controller getController(String uri){
        return controllers.get(uri);
    }

}
