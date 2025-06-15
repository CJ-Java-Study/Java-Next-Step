package webserver;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import http.HttpRequest;
import http.HttpResponder;
import http.HttpSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.Controller;
import webserver.controller.RequestMapping;

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
            log.info("요청 method: {}, path: {}, RequestParams: {}", httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getRequestParams());

            String sessionId = httpRequest.getCookies().getCookie(HttpSessions.SESSION_ID_NAME);

            if(sessionId == null) {
                sessionId = HttpSessions.SESSION_ID_NAME + "=" + UUID.randomUUID();
            }

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
