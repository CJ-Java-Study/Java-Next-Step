package core.controller;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * loadOnStartup: 서브릿의 인스턴스를 생성하는 시점과 초기화를 담당하는 init() 메서드를 호출하는 시점을 지정
 */
@Slf4j
@WebServlet(name="dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // 컨트롤러가 반환하는 뷰 이름이 해당 PREFIX로 시작하면 REDIRECT 처리
    private static final String DEFAUL_REDIRECT_PREFIX = "redirect:";

    private RequestMapping requestMapping;

    @Override
    public void init() throws ServletException {
        requestMapping = new RequestMapping();
        requestMapping.initMapping();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        log.debug("requestUri: {}", requestUri);

        Controller controller = requestMapping.getController(requestUri);
        try {
            String viewPath = controller.execute(req, resp);
            processView(viewPath, req, resp);
        } catch (Exception e) {
            log.error("Exception occurred: ", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void processView(String viewPath, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (viewPath.startsWith(DEFAUL_REDIRECT_PREFIX)) {
            String redirectPath = viewPath.substring(DEFAUL_REDIRECT_PREFIX.length());
            log.debug("Redirecting to: {}", redirectPath);
            resp.sendRedirect(redirectPath);
        } else {
            log.debug("Forwarding to: {}", viewPath);
            req.getRequestDispatcher(viewPath).forward(req, resp);
        }
    }
}
