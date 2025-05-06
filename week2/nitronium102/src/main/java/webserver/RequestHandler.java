package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

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

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // HTTP 요청 정보 파싱
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line = bufferedReader.readLine();
            if (line == null) {
                return;
            }
            // HTTP 요청 URL 파싱
            String url = HttpRequestUtils.getRequestUrl(line);
            log.debug("url : {}", url);

            int contentLength = 0;
            while(!line.isEmpty()) {
                line = bufferedReader.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            if (url.startsWith("/user/create")) {
                String body = IOUtils.readData(bufferedReader, contentLength);
                Map<String, String> params = HttpRequestUtils.parseQueryString(body); // userId=nitronium&password=cjjd
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("user : {}", user);

                // 302 리다이렉트 응답
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "/index.html");
            } else {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = readWebappFile(url);
                sendResponse(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * webapp 디렉토리에서 파일을 읽어 바이트 배열로 반환.
     * 파일이 없을 경우 "File Not Found" 메시지 반환.
     */
    private byte[] readWebappFile(String url) {
        try {
            return Files.readAllBytes(new File("./webapp" + url).toPath());
        } catch (IOException e) {
            log.warn("File not found: {}", url);
            return "File Not Found".getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
    * HTTP 200 헤더와 body를 전송
    */
    private void sendResponse(DataOutputStream dos, byte[] body) {
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void response302Header(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
