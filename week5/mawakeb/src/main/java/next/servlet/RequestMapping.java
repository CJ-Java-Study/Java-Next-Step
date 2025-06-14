package next.servlet;

import next.controller.Controller;
import next.controller.HomeController;
import next.controller.users.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("", new HomeController());

        controllers.put("/users/create", new CreateUserController());
        controllers.put("/users/form", new CreateUserController());

        controllers.put("/users", new ListUserController());

        controllers.put("/users/login", new LoginController());
        controllers.put("/users/loginForm", new LoginController());

        controllers.put("/users/logout", new LogoutController());

        controllers.put("/users/profile", new ProfileController());

        controllers.put("/users/update", new UpdateUserController());
        controllers.put("/users/updateForm", new UpdateUserController());
    }

    public static Controller getController(String uri){
        return controllers.get(uri);
    }

}
