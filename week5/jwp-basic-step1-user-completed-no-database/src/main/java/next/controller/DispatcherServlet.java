package next.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private final Map<String, Controller> controllerMap = new HashMap<>();

    @Override
    public void init() throws ServletException{
        controllerMap.put("/", new HomeController());

        controllerMap.put("/users", new ListUserController());
        controllerMap.put("/users/form", new ForwardController("/user/form.jsp"));
        controllerMap.put("/users/create", new CreateUserController());

        controllerMap.put("/users/login", new LoginController());
        controllerMap.put("/users/loginForm", new ForwardController("/user/login.jsp"));

        controllerMap.put("/users/logout", new LogoutController());

        controllerMap.put("/users/profile", new ProfileController());

        controllerMap.put("/users/update", new UpdateUserController());
        controllerMap.put("/users/updateForm", new UpdateUserController());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI().substring(req.getContextPath().length());

        Controller controller = controllerMap.get(requestURI);
        if(controller== null){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            String viewName = controller.execute(req, resp);
            if(viewName.startsWith("redirect:")){
                resp.sendRedirect(viewName.substring("redirect:".length()));
            } else{
                req.getRequestDispatcher(viewName).forward(req,resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
