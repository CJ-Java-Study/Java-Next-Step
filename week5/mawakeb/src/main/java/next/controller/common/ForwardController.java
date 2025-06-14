package next.controller.common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ForwardController extends AbstractController {

    private String forwardUrl;

    public ForwardController(String forwardUrl){
        this.forwardUrl = forwardUrl;
        if(forwardUrl == null){
            throw new NullPointerException("forwardUrl is null. Please enter a url");
        }
    }

    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return forwardUrl;
    }
}
