package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

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
            HttpRequest httpRequest = getHttpRequest(in);
            String path = httpRequest.getPath();

            if(httpRequest.isPost() && path.equals("/user/create")){
                handleCreateUser(httpRequest, dos);
                return;
            }

            // 모두 아닌 경우 파일로 처리
            handleGetFile(httpRequest, dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest getHttpRequest(InputStream in) throws IOException {
        InputStreamReader sr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(sr);
        return new HttpRequest(br);
    }

    // POST user/create 처리
    private void handleCreateUser(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        Map<String, String> paramMap = HttpRequestUtils.parseQueryString(httpRequest.getBody());
        User user = new User(paramMap);
        log.debug("USER CREATED: " + user);

        HttpResponseBuilder.response302Header(dos, "/index.html");
    }

    private void handleGetFile(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        if(httpRequest.isGet()) {
            File file = new File("./week2/mawakeb/webapp" + httpRequest.getPath());

            if(!file. exists()){
                HttpResponseBuilder.response404Header(dos, 0);
            }

            byte[] body = Files.readAllBytes(file.toPath());
            boolean isCss = httpRequest.getPath().endsWith(".css");
            HttpResponseBuilder.response200Header(dos, body.length, isCss);
            HttpResponseBuilder.responseBody(dos, body);
        }
    }



}
