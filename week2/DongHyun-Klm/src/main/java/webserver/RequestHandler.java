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
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest httpRequest = new HttpRequest(in);
            log.info("요청 method: {}, path: {}, queryString: {}", httpRequest.getMethod(), httpRequest.getPath(), httpRequest.getQueryString());

            DataOutputStream dos = new DataOutputStream(out);

            // 회원가입 요청 처리
            if(httpRequest.getMethod().isPost() && "/user/create".equals(httpRequest.getPath())) {
                handleUserCreate(br, dos, httpRequest.getContentLength());
                return;
            }

            // 로그인 요청 처리 로직
            if(httpRequest.getMethod().isPost() && "/user/login".equals(httpRequest.getPath())) {
                handleLogin(br, dos, httpRequest.getContentLength());
                return;
            }

            // 사용자 목록 출력 요청 처리 로직
            if (httpRequest.getMethod().isGET() && "/user/list".equals(httpRequest.getPath())) {
                handleLoginList(dos, httpRequest.getHeaders());
            }

            // 정적 리소스 조회
            staticFile(dos, httpRequest.getPath(), httpRequest.getHeaders());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handleLoginList(DataOutputStream dos, Map<String, String> headers) {
        String cookie = headers.get("Cookie");
        boolean loginYn = cookie != null && cookie.contains("logined=true");
        if(!loginYn) {
            response302Header(dos, "/user/login.html");
            return;
        }
        UserListOut(dos);
    }

    private void UserListOut(DataOutputStream dos) {
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>회원목록</title></head><body>");
        sb.append("<h1>회원 목록</h1><table border=\"1\">");
        sb.append("<tr><th>ID</th><th>이름</th><th>이메일</th></tr>");
        for (User u : users) {
            sb.append("<tr>")
                    .append("<td>").append(u.getUserId()).append("</td>")
                    .append("<td>").append(u.getName()).append("</td>")
                    .append("<td>").append(u.getEmail()).append("</td>")
                    .append("</tr>");
        }
        sb.append("</table></body></html>");
        byte[] body = sb.toString().getBytes(StandardCharsets.UTF_8);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void staticFile(DataOutputStream dos, String path, Map<String, String> headers) throws IOException {
        File file = new File("./webapp" + path);
        if(!file.exists()) {
            response404Header(dos);
            return;
        }
        byte[] body = Files.readAllBytes(file.toPath());
        // MIME 타입 결정
        String contentType = Files.probeContentType(file.toPath());
        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }

    private void response404Header(DataOutputStream dos) throws IOException {
        byte[] notFound = "<h1>404 Not Found</h1>".getBytes();
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + notFound.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(notFound);
        dos.flush();
    }

    private void handleLogin(BufferedReader br, DataOutputStream dos, int contentLength) throws IOException {
        String requestBody = IOUtils.readData(br, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);

        String userId   = params.get("userId");
        String password = params.get("password");
        log.info("로그인 요청 - userId: {}, password: {}", userId, password);

        User user = DataBase.findUserById(userId);
        log.info("요청 사용자 - {}", user);
        if(user != null && user.getPassword().equals(password)) { // 로그인 성공
            byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response200HeaderWithCookie(dos, body.length);
            responseBody(dos, body);
        } else { // 로그인 실패
            byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
            response401HeaderWithCookie(dos, body.length);
            responseBody(dos, body);
        }
    }

    private void handleUserCreate(BufferedReader br, DataOutputStream dos, int contentLength) throws IOException {
        String requestBody = IOUtils.readData(br, contentLength);
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);

        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");

        log.info("회원가입 요청 - userId: {}, password : {}, name: {}, email: {}", userId, password, name, email);

        User user = new User(userId, password, name, email);
        DataBase.addUser(user);

        response302Header(dos, "/index.html");
    }

    private void response401HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 401 Unauthorized\r\n");
            dos.writeBytes("Set-Cookie: logined=false" + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+ contentType +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
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

    private void response200HeaderWithCookie(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        try{
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Set-Cookie: logined=true" + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
