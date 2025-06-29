package core.controller;

import lombok.extern.slf4j.Slf4j;
import next.web.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestMapping {
    private Map<String, Controller> controllerMap = new HashMap<>();

    void initMapping() {
        controllerMap.put("/", new HomeController());
        controllerMap.put("/users/form", new ForwardController("/user/form.jsp"));
        controllerMap.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        controllerMap.put("/users", new ListUserController());
        controllerMap.put("/users/login", new LoginController());
        controllerMap.put("/users/profile", new ProfileController());
        controllerMap.put("/users/logout", new LogoutController());
        controllerMap.put("/users/create", new CreateUserController());
        controllerMap.put("/users/updateForm", new UpdateFormUserController());
        controllerMap.put("/users/update", new UpdateUserController());

        log.info("Request mapping initialized with {} controllers", controllerMap.size());
    }

    public Controller getController(String requestUrl) {
        if (controllerMap.isEmpty()) {
            initMapping();
        }
        log.debug("Request URL: {}", requestUrl);
        return controllerMap.get(requestUrl);
    }

    void put(String url, Controller controller) {
        controllerMap.put(url, controller);
    }
}
