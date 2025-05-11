package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // 1단계: HTTP 요청 라인 및 헤더 전체 출력
            String line;
            String requestLine = null;

            //I/O 루프 처리에서 자주 쓰이는 패턴
            while (!(line = br.readLine()).equals("")) {
                log.debug("HTTP 요청 헤더: {}", line);
                if (requestLine == null) {
                    requestLine = line;  // 첫 줄 저장
                }
            }

            if (requestLine == null) return; // 요청이 비정상적이면 종료

            // 2단계: 요청 URL 추출
            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String url = tokens[1];

            log.debug("HTTP Method: {}, URL: {}", method, url);

            if (url.equals("/")) {
                url = "/index.html"; // 기본 경로 설정
            }

            // 3단계: 요청 URL에 해당하는 파일 전달
            byte[] body;
            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(WEBAPP_PATH + url);

            if (file.exists()) {
                body = Files.readAllBytes(file.toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                body = "<h1>404 Not Found</h1>".getBytes();
                response404Header(dos, body.length);
                responseBody(dos, body);
            }


//            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World".getBytes();
//            response200Header(dos, body.length);
//            responseBody(dos, body);
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
}
