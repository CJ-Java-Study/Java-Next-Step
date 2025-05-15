package webserver;

import java.io.*;
import java.net.Socket;

import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResponseUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ResponseUtils responseUtils = new ResponseUtils();
    private static final UserRequestHandler userRequestHandler = new UserRequestHandler();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            String path = httpRequest.getPath();

            if ("/user/create".equals(path)) {
                userRequestHandler.handleUserCreate(httpRequest, dos);
            } else if ("/user/login".equals(path)) {
                userRequestHandler.handleUserLogin(httpRequest, dos);
            } else if ("/user/list".equals(path)) {
                if (httpRequest.isLoggedIn()) {
                    responseUtils.send302Redirect(dos, "/user/login.html");
                    return;
                }
                userRequestHandler.handleUserList(dos);
            } else if (path.endsWith(".css")) {
                responseCssResource(dos, path);
            } else {
                responseResource(dos, path);
            }
        } catch (
                IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseCssResource(DataOutputStream dos, String url) {
        byte[] body = ResponseUtils.readWebappFile(url);
        responseUtils.send200CssResponse(dos, body);
    }

    private void responseResource(DataOutputStream dos, String url) {
        byte[] body = ResponseUtils.readWebappFile(url);
        responseUtils.send200Response(dos, body);
    }
}
