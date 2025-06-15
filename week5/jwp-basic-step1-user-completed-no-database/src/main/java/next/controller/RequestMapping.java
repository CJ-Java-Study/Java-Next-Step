package next.controller;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    public static Map<String, Controller> initialize() {
        Map<String, Controller> map = new HashMap<>();

        map.put("/", new HomeController());

        map.put("/users", new ListUserController());
        map.put("/users/form", new ForwardController("/user/form.jsp"));
        map.put("/users/create", new CreateUserController());

        map.put("/users/login", new LoginController());
        map.put("/users/loginForm", new ForwardController("/user/login.jsp"));

        map.put("/users/logout", new LogoutController());

        map.put("/users/profile", new ProfileController());

        map.put("/users/update", new UpdateUserController());
        map.put("/users/updateForm", new UpdateUserController());

        return map;
    }
}
