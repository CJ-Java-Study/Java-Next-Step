package webserver;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapper {
    private static final Map<String, Controller> mappings = new HashMap<>();

    static {
        mappings.put("/user/create", new CreateUserController());
        mappings.put("/user/login", new LoginController());
        mappings.put("/user/list", new ListUserController());
    }

    public static Controller getController(String path) {
        return mappings.get(path);
    }
}
