package core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {

    private final String forwardUrl;

    public ForwardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        if (forwardUrl == null || forwardUrl.isEmpty()) {
            throw new IllegalArgumentException("Forward URL cannot be null or empty");
        }
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        return forwardUrl;
    }
}
