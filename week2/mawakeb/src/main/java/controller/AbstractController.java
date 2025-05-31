package controller;

import model.Http.HttpRequest;
import model.Http.HttpResponse;
import model.enums.HTTPMethod;

public abstract class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response){
        HTTPMethod method = request.getMethod();

        if(method.isGet()){
            doGet(request, response);
        } else  {
            doPost(request,response);
        }
    }

    protected void doPost (HttpRequest request, HttpResponse response) {

    }

    protected void doGet (HttpRequest request, HttpResponse response) {

    }
}
