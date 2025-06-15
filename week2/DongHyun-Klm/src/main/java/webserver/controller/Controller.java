package webserver.controller;

import http.HttpRequest;
import http.HttpResponder;

public interface Controller {
    void service(HttpRequest httpRequest, HttpResponder httpResponder) throws Exception;
}

