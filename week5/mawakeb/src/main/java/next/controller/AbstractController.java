package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        if(request.getMethod().equals("GET")){
            return doGet(request, response);
        } else  {
            return doPost(request,response);
        }
    }

    protected String doPost (HttpServletRequest request, HttpServletResponse response) {

    }

    protected String doGet (HttpServletRequest request, HttpServletResponse response) {

    }
}
