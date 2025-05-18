package http;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponder responder = new HttpResponder(out);

        responder.forward("/index.html");
        String actual = out.toString(StandardCharsets.UTF_8);

        // 상태 라인
        assertTrue(actual.startsWith("HTTP/1.1 200 OK\r\n"));

        // 헤더에 Content-Type / Content-Length
        assertTrue(actual.contains("Content-Type: text/html;charset=utf-8\r\n"));
        assertTrue(actual.contains("Content-Length:"));

        // 바디에 실제 index.html 내용 일부 (예: <html> 태그)
        assertTrue(actual.contains("<!DOCTYPE html>"));
        assertTrue(actual.contains("<html"));
    }

    @Test
    public void responseRedirect() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponder responder = new HttpResponder(out);

        responder.sendRedirect("/index.html");
        String actual = out.toString(StandardCharsets.UTF_8);

        // 상태 라인
        assertTrue(actual.startsWith("HTTP/1.1 302 Found\r\n"));

        // Location 헤더
        assertTrue(actual.contains("Location: /index.html\r\n"));
    }

    @Test
    public void responseCookies() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HttpResponder responder = new HttpResponder(out);

        Map<String, String> extra = new HashMap<>();
        extra.put("Set-Cookie", "logined=true");

        responder.sendRedirect("/index.html", extra);
        String actual = out.toString(StandardCharsets.UTF_8);
        System.out.println(actual);

        // 상태 라인
        assertTrue(actual.startsWith("HTTP/1.1 302 Found\r\n"));

        // Location 헤더
        assertTrue(actual.contains("Location: /index.html\r\n"));

        // 쿠키 헤더
        assertTrue(actual.contains("Set-Cookie: logined=true\r\n"));
    }

}
