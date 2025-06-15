package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller{
    private final String viewPath;

    public ForwardController(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return viewPath;
    }
}
