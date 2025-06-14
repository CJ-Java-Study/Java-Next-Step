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
    private static final String JWP_BASIC = "/jwp-basic";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        if (requestUri.startsWith(JWP_BASIC)) {
            requestUri = requestUri.substring(JWP_BASIC.length());
        }

        if (requestUri.isEmpty()) {
            requestUri = "/"; // 또는 빈 문자열 대신 기본 경로 처리
        }

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
