package controller;

import model.Http.HttpRequest;
import model.Http.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
