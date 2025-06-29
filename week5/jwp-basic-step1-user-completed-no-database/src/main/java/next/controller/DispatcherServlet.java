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

    private Map<String, Controller> controllerMap;

    @Override
    public void init() throws ServletException {
        this.controllerMap = RequestMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI().substring(req.getContextPath().length());

        Controller controller = controllerMap.get(requestURI);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            String viewName = controller.execute(req, resp);
            if (viewName.startsWith("redirect:")) {
                resp.sendRedirect(viewName.substring("redirect:".length()));
            } else {
                req.getRequestDispatcher(viewName).forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
