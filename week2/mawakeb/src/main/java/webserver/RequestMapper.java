package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.UserListController;
import controller.UserLoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {

    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new UserLoginController());
        controllers.put("/user/list", new UserListController());
        // TODO 우측 상단 버튼으로 진입하고 싶어서 임시로 추가
        controllers.put("/user/list.html", new UserListController());
    }

    public static Controller getController(String uri){
        return controllers.get(uri);
    }
}
