package model.Http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import model.enums.ContentType;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();

    private final String dev = "./week2/mawakeb/webapp";
    private final String prd = "/webapp";

    public HttpResponse(OutputStream out){
        dos = new DataOutputStream(out);
    }

    public void addHeader(String key, String val){
        headers.put(key, val);
    }

    /* 200 OK 및 기본 요청 및 uri 유지 */
    public void forward(String url) {
        try {
            log.error("Working dir: " + System.getProperty("user.dir"));
            byte[] body = loadFile(url);

            ContentType contentType = ContentType.fromFileName(url);
            headers.put("Content-Type", contentType.getValue());
            headers.put("Content-Length", String.valueOf(body.length));

            response200Header(body.length);
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    public byte[] loadFile(String url) throws IOException {
        // 로컬 환경
        Path devPath = Path.of(dev, url);
        if (Files.exists(devPath)) {
            return Files.readAllBytes(devPath);
        }

        // AWS 환경
        Path prodPath = Path.of(prd, url);
        if (Files.exists(prodPath)) {
            return Files.readAllBytes(prodPath);
        }

        throw new IOException("존재하지 않는 경로입니다. " + url);
    }

    /* 200 OK 기존요청 유지, 파일 대신 만든 페이지 보여주기 */
    public void forwardBody(String body) {
        byte[] contents = body.getBytes();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", contents.length + "");
        response200Header(contents.length);
        responseBody(contents);
    }

    /* 302 Found로 재요청 방지 */
    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            processHeaders();
            dos.writeBytes("Location: " + redirectUrl + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processHeaders() {
        try {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                dos.writeBytes(key + ": " + headers.get(key) + " \r\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
