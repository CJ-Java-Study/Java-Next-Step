package util;

import model.enums.HTTPMethod;
import model.Http.HttpRequest;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void request_GET() throws Exception {
        String httpRequest =
                "GET /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Accept: */*\r\n" +
                        "\r\n";

        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = new HttpRequest(inputStream);

        assertEquals(HTTPMethod.GET, request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParameter("userId"));
    }

    @Test
    public void request_POST() throws Exception {
        String httpRequest =
                "POST /user/create HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "Connection: keep-alive\r\n" +
                        "Content-Length: 46\r\n" +
                        "Content-Type: application/x-www-form-urlencoded\r\n" +
                        "\r\n" +
                        "userId=javajigi&password=password&name=JaeSung";

        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes(StandardCharsets.UTF_8));
        HttpRequest request = new HttpRequest(inputStream);

        assertEquals(HTTPMethod.POST, request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("javajigi", request.getParameter("userId"));
    }
}