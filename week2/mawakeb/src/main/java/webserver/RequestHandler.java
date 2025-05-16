package webserver;

import java.io.*;
import java.net.Socket;

import model.enums.HTTPMethod;
import model.Http.HttpRequest;
import model.Http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            HttpResponse httpResponse = new HttpResponse(out);

            HTTPMethod method = httpRequest.getMethod();
            String path = httpRequest.getPath();

            if(method.isPost() && path.equals("/user/create")){
                UserRequestHandler.handleCreateUser(httpRequest, httpResponse);
                return;
            }

            if(method.isPost() && path.equals("/user/login")){
                UserRequestHandler.handleLoginUser(httpRequest, httpResponse);
                return;
            }

            if(method.isGet() && path.equals("/user/list")){
                UserRequestHandler.handleGetUserList(httpRequest, httpResponse);
                return;
            }

            // 우측 상단 버튼으로 조회될 수 있게
            if(method.isGet() && path.equals("/user/list.html")){
                UserRequestHandler.handleGetUserList(httpRequest, httpResponse);
                return;
            }

            // 모두 아닌 경우 파일로 처리
            if(httpRequest.getMethod().isGet()) {
                httpResponse.forward(path);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
