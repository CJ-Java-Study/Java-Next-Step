package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String WEBAPP_PATH = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            String requestLine = null;

            while ((line = br.readLine()) != null && !line.equals("")) {
                log.debug("HTTP 요청 헤더: {}", line);
                if (requestLine == null) {
                    requestLine = line;
                }
            }

            if (requestLine == null) return;

            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String url = tokens[1];

            log.debug("HTTP Method: {}, URL: {}", method, url);

            DataOutputStream dos = new DataOutputStream(out);

            if (url.startsWith("/user/create") && method.equals("GET")) {
                handleUserCreate(url, dos);
            } else {
                serveIndexFile(url, dos);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handleUserCreate(String url, DataOutputStream dos) throws IOException {
        int index = url.indexOf("?");
        if (index == -1) {
            response404(dos, "잘못된 요청입니다.");
            return;
        }

        String queryString = url.substring(index + 1);
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);

        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );

        log.debug("회원가입 요청 처리: {}", user);

        byte[] body = "회원가입 완료".getBytes();
        response200(dos, body);
    }

    private void serveIndexFile(String url, DataOutputStream dos) throws IOException {
        if (url.equals("/")) {
            url = "/index.html";
        }

        File file = new File(WEBAPP_PATH + url);

        if (file.exists()) {
            byte[] body = Files.readAllBytes(file.toPath());
            response200(dos, body);
        } else {
            response404(dos, "<h1>404 Not Found</h1>");
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200(DataOutputStream dos, byte[] body) throws IOException {
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response404(DataOutputStream dos, String message) throws IOException {
        byte[] body = message.getBytes();
        response404Header(dos, body.length);
        responseBody(dos, body);
    }
}
