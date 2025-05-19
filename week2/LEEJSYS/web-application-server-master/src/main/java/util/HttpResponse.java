package util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final DataOutputStream dos;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void forward(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        headers.put("Content-Type", getContentType(path));
        headers.put("Content-Length", String.valueOf(body.length));
        response200Header(body.length);
        responseBody(body);
    }

    public void forwardBody(String bodyText) throws IOException {
        byte[] body = bodyText.getBytes();
        headers.put("Content-Type", "text/html;charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.length));
        response200Header(body.length);
        responseBody(body);
    }

    public void sendRedirect(String location) throws IOException {
        headers.put("Location", location);
        headers.put("Content-Type", "text/html;charset=utf-8");
        byte[] body = "<h1>Redirecting...</h1>".getBytes();
        headers.put("Content-Length", String.valueOf(body.length));
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        processHeaders();
        responseBody(body);
    }

    public void response200Header(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK\r\n");
        processHeaders();
    }

    public void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public void processHeaders() throws IOException {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            dos.writeBytes("Set-Cookie: " + cookie.getKey() + "=" + cookie.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    public void addCookie(String name, String value) {
        cookies.put(name, value);
    }

    private String getContentType(String path) {
        if (path.endsWith(".css")) return "text/css;charset=utf-8";
        if (path.endsWith(".js")) return "application/javascript;charset=utf-8";
        return "text/html;charset=utf-8";
    }
}
