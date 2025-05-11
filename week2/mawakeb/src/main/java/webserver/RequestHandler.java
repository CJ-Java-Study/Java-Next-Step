package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.HttpHeader;
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
            DataOutputStream dos = new DataOutputStream(out);
            HttpHeader header = getHeader(in);
            String path = header.getPath();
            byte[] body;

            if(header.isGet() && path.endsWith(".html")){
                body = Files.readAllBytes(new File("./week2/mawakeb/webapp" + path).toPath());
                response200Header(dos, body.length, false);
                responseBody(dos, body);
            }

            if(header.isGet() && path.endsWith(".css")){
                body = Files.readAllBytes(new File("./week2/mawakeb/webapp" + path).toPath());
                response200Header(dos, body.length, true);
                responseBody(dos, body);
            }

            if(header.isPost() && path.equals("/user/create")){
                response302Header(dos, "/index.html");
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpHeader getHeader(InputStream in) throws IOException {
        InputStreamReader sr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(sr);
        return new HttpHeader(br);
    }

    // POST user/create 처리
    private void createUser(BufferedReader br, int contentLength) throws IOException {
        String params = IOUtils.readData(br, contentLength);
        Map<String, String> paramMap = HttpRequestUtils.parseQueryString(params);
        User user = new User(paramMap);
        log.debug("USER CREATED: " + user);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, boolean isCss) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if (isCss){
                dos.writeBytes("Content-Type: text/css,*/*;q=0.1\r\n");
            } else {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
