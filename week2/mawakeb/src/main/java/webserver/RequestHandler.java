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
            byte[] body = parseRequest(in); // parse request body
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] parseRequest(InputStream in){

        byte[] body = "404 Error".getBytes();

        try {
            InputStreamReader sr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(sr);

            HttpHeader header = new HttpHeader(br);
            String path = header.getPath();

            if(header.isGet() && path.endsWith(".html")){
                body = Files.readAllBytes(new File("./week2/mawakeb/webapp" + path).toPath());
            }

            if(header.isPost() && path.equals("/user/create")){
                createUser(br, header.getContentLength());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            body = e.getMessage().getBytes();
        }

        return body;
    }

    // POST user/create 처리
    private void createUser(BufferedReader br, int contentLength) throws IOException {
        String params = IOUtils.readData(br, contentLength);
        Map<String, String> paramMap = HttpRequestUtils.parseQueryString(params);
        User user = new User(paramMap);
        log.debug("USER CREATED: " + user);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
