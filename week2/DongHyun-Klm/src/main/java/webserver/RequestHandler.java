package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponder;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.controller.Controller;
import webserver.controller.RequestMapping;
import webserver.controller.impl.CreateUserController;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponder httpResponder = new HttpResponder(out);
            log.info("요청 method: {}, path: {}, queryString: {}", httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getQueryString());

            Controller controller = RequestMapping.getController(httpRequest.getPath());

            if (controller == null) {
                String path = getDefaultPath(httpRequest.getPath());
                httpResponder.forward(path);
            } else {
                controller.service(httpRequest, httpResponder);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }

}
