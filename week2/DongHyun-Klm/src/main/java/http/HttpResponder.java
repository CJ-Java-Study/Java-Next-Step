package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

public class HttpResponder {
    private final DataOutputStream dos;

    public HttpResponder(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    /**
     * 응답 만들어 전송
     */
    public void send(HttpResponse resp) throws IOException {
        // Content-Length 헤더 추가
        byte[] body = resp.getBody();
        if (body.length > 0 ) {
            resp.getHeaders().put("Content-Length", String.valueOf(body.length));
        }

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

    /**
     * 302 Redirect + 추가 헤더
     *
     * @param redirectUrl 리다이렉트할 URL
     * @param extraHeaders Response 추가할 헤더
     */
    public void sendRedirect(String redirectUrl, Map<String, String> extraHeaders)
            throws IOException {
        // HttpResponse 빌드
        HttpResponse resp = HttpResponse.builder()
                .statusCode(302)
                .statusMessage("Found")
                .headers(extraHeaders)
                .build();

        resp.getHeaders().put("Location", redirectUrl);

        send(resp);
    }

}

