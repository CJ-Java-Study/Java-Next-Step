package core.mvc;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private ControllerMapping rm;
    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    public void init() {
        rm = new ControllerMapping("next.controller");
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        log.debug("Request URI: {}", req.getRequestURI());
        Controller c = rm.findController(req.getRequestURI());
        String view = null;
        try {
            view = c.execute(req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (view.startsWith(REDIRECT_PREFIX)) {
            res.sendRedirect(view.substring(REDIRECT_PREFIX.length()));
            return;
        }
        req.getRequestDispatcher(view).forward(req, res);

    }
}
