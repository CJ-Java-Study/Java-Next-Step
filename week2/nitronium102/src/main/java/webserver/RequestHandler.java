package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
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
            boolean isLogin = false;
            while (!line.isEmpty()) {
                line = bufferedReader.readLine();
                if (line.contains("Cookie")) {
                    String cookie = line.split(":")[1].trim();
                    if (cookie.contains("logined=true")) {
                        isLogin = true;
                    }
                }
                if (line.contains("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            switch (url) {
                case "/user/create":
                    userRequestHandler.handleUserCreate(bufferedReader, contentLength, dos);
                    break;
                case "/user/login":
                    userRequestHandler.handleUserLogin(bufferedReader, contentLength, dos);
                    break;
                case "/user/list":
                    if (!isLogin) {
                        responseUtils.send302Redirect(dos, "/user/login.html");
                        return;
                    }
                    userRequestHandler.handleUserList(dos);

                    break;
                default:
                    byte[] body = readWebappFile(url);
                    responseUtils.send200Response(dos, body);
                    break;
            }
        } catch (
                IOException e) {
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
}
