package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponder {
    private final DataOutputStream dos;
    private final Map<String, String> customHeaders = new LinkedHashMap<>();

    public HttpResponder(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    /**
     * 응답 만들어 전송
     */
    public void send(HttpResponse resp) throws IOException {
        resp.getHeaders().putAll(customHeaders);
        // 상태 라인
        dos.writeBytes("%s %d %s\r\n"
                .formatted(resp.getVersion(), resp.getStatusCode(), resp.getStatusMessage()));
        // 헤더
        for (var e : resp.getHeaders().entrySet()) {
            dos.writeBytes(e.getKey() + ": " + e.getValue() + "\r\n");
        }
        // 헤더 종료
        dos.writeBytes("\r\n");
        // 바디
        if (resp.getBody().length > 0) {
            dos.write(resp.getBody());
        }
        dos.flush();
        customHeaders.clear();
    }

    /**
     * forward - 200 OK + static 파일 바디
     */
    public void forward(String path) throws IOException {
        File file = new File("./webapp" + path);
        if(!file.exists()) {
            HttpResponse notFound = HttpResponse.builder()
                    .statusCode(404).statusMessage("Not Found")
                    .build();
            send(notFound);
            return;
        }
        byte[] body = Files.readAllBytes(file.toPath());

        HttpResponse resp = HttpResponse.builder()
                .statusCode(200)
                .statusMessage("OK")
                .body(body)
                .build();

        resp.getHeaders().put("Content-Type", "text/html;charset=utf-8");
        resp.getHeaders().put("Content-Length", String.valueOf(body.length));

        send(resp);
    }

    /**
     *  redirect - 302 Found + Location 헤더
     */
    public void sendRedirect(String redirectUrl) throws IOException {
        HttpResponse resp = HttpResponse.builder()
                .statusCode(302)
                .statusMessage("Found")
                .build();

        resp.getHeaders().put("Location", redirectUrl);

        send(resp);
    }

    public HttpResponder addHeader(String name, String value) {
        customHeaders.put(name, value);
        return this;
    }
}

