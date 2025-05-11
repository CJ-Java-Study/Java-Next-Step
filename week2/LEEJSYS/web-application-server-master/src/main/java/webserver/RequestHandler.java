package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

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
            Map<String, String> headers = new HashMap<>();

            while ((line = br.readLine()) != null && !line.equals("")) {
                log.debug("HTTP 요청 헤더: {}", line);
                if (requestLine == null) {
                    requestLine = line;
                } else{
                    Pair header = HttpRequestUtils.parseHeader(line);
                    if (header != null) {
                        headers.put(header.getKey(), header.getValue());
                    }
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
            }else if (url.equals("/user/create") && method.equals("POST")) {
                handleUserCreatePost(br, headers.get("Content-Length"), dos);
            }else if (url.equals("/user/login") && method.equals("POST")) {
                String contentLengthHeader = headers.get("Content-Length");
                if (contentLengthHeader != null) {
                    handleUserLoginPost(br, contentLengthHeader, dos);
                } else {
                    response400(dos, "Missing Content-Length header");
                }
            }else if (url.equals("/user/list") && method.equals("GET")) {
                handleUserList(dos);
            }else {
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
        DataBase.addUser(user);
        log.debug("회원가입 요청 처리: {}", user);

        byte[] body = "회원가입 완료".getBytes();
        response200(dos, body);
    }

    private void handleUserCreatePost(BufferedReader br, String contentLengthHeader, DataOutputStream dos) throws IOException {
        int contentLength = Integer.parseInt(contentLengthHeader);
        String body = IOUtils.readData(br, contentLength);

        Map<String, String> params = HttpRequestUtils.parseQueryString(body);

        User user = new User(
                params.get("userId"),
                params.get("password"),
                params.get("name"),
                params.get("email")
        );

        log.debug("POST 회원가입 요청 처리: {}", user);

        byte[] responseBody = "회원가입 완료 (POST)".getBytes();
        response302(dos, "/index.html", null);
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

    private void handleUserLoginPost(BufferedReader br, String contentLengthHeader, DataOutputStream dos) throws IOException {
        int contentLength = Integer.parseInt(contentLengthHeader);
        String body = IOUtils.readData(br, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(body);

        String userId = params.get("userId");
        String password = params.get("password");

        log.debug("userId 저장 확인: {}", userId);
        User user = DataBase.findUserById(userId);

        log.debug("로그인 요청 내용: {}", user);

        if (user != null && user.getPassword().equals(password)) {
            response302(dos, "/index.html", "logined=true");
        } else {
            response302(dos, "/user/login_failed.html", "logined=false");
        }
    }

    private void handleUserList(DataOutputStream dos) throws IOException {
        Collection<User> users = DataBase.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h1>사용자 목록</h1>");
        sb.append("<ul>");
        for (User user : users) {
            sb.append("<li>")
                    .append(user.getUserId())
                    .append(" - ")
                    .append(user.getName())
                    .append("</li>");
        }
        sb.append("</ul>");
        sb.append("</body></html>");

        byte[] body = sb.toString().getBytes();
        response200(dos, body);
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

    private void response302Header(DataOutputStream dos, String location, String cookie, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        if (cookie != null && !cookie.isEmpty()) {
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
        }
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response400Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
        dos.writeBytes("Content-Type: text/plain;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
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

    private void response302(DataOutputStream dos, String location, String cookie) throws IOException {
        byte[] body = "<h1>Redirecting...</h1>".getBytes();
        response302Header(dos, location, cookie, body.length);
        responseBody(dos, body);
    }

    private void response400(DataOutputStream dos, String message) throws IOException {
        byte[] body = message.getBytes();
        response400Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response404(DataOutputStream dos, String message) throws IOException {
        byte[] body = message.getBytes();
        response404Header(dos, body.length);
        responseBody(dos, body);
    }
}
