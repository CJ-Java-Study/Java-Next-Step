package next.webserver;

import next.controller.common.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String REDIRECT = "redirect:";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        Controller controller = RequestMapping.getController(requestUri);
        try {
            String path = controller.execute(request, response);
            if (path.startsWith(REDIRECT)) {
                response.sendRedirect(path.substring(REDIRECT.length()));
                return;
            }

            RequestDispatcher rd = request.getRequestDispatcher(path);
            rd.forward(request, response);

        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}
