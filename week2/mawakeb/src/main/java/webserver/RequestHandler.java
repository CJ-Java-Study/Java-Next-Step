package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseBuilder;

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
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest httpRequest = new HttpRequest(in);
            String path = httpRequest.getPath();

            if(httpRequest.isPost() && path.equals("/user/create")){
                UserRequestHandler.handleCreateUser(httpRequest, dos);
                return;
            }

            if(httpRequest.isPost() && path.equals("/user/login")){
                UserRequestHandler.handleLoginUser(httpRequest, dos);
                return;
            }

            if(httpRequest.isGet() && path.equals("/user/list")){
                UserRequestHandler.handleGetUserList(httpRequest, dos);
                return;
            }

            // 우측 상단 버튼으로 조회될 수 있게
            if(httpRequest.isGet() && path.equals("/user/list.html")){
                UserRequestHandler.handleGetUserList(httpRequest, dos);
                return;
            }

            // 모두 아닌 경우 파일로 처리
            handleGetFile(httpRequest, dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    // html 혹은 css 파일
    private void handleGetFile(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        if(httpRequest.isGet()) {
            String path = httpRequest.getPath();
            File file = new File("./week2/mawakeb/webapp" + path);

            if(!file.exists()){
                HttpResponseBuilder.response404Header(dos, 0);
                log.error(path + " does not exist.");
            }

            byte[] body = Files.readAllBytes(file.toPath());
            boolean isCss = httpRequest.getPath().endsWith(".css");
            HttpResponseBuilder.response200Header(dos, body.length, isCss, null);
            HttpResponseBuilder.responseBody(dos, body);
        }
    }



}
