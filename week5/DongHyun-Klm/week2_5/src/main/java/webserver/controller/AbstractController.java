package webserver.controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponder;

import java.io.IOException;

public class AbstractController implements Controller {

    @Override
    public void service(HttpRequest httpRequest, HttpResponder httpResponder) throws Exception {
        switch (httpRequest.getMethod()) {
            case GET:
                doGet(httpRequest, httpResponder);
                break;
            case POST:
                doPost(httpRequest, httpResponder);
                break;
            default:
        }
    }

    public void doPost(HttpRequest req, HttpResponder res) throws IOException {

    }

    public void doGet(HttpRequest req, HttpResponder res) throws IOException {

    }
}
