package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseBuilder {

    private static final Logger log = LoggerFactory.getLogger(HttpResponseBuilder.class);

    public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, boolean isCss, String cookies) {
        try {
            String contentType = isCss ? "text/css,*/*;q=0.1\r\n" : "text/html;charset=utf-8\r\n" ;

            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");

            if(cookies != null) {
                dos.writeBytes("Set-Cookie: " + cookies + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {

            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response302Header(DataOutputStream dos, String url, String cookies) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");

            if(cookies != null) {
                dos.writeBytes("Set-Cookie: " + cookies + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void response401Header(DataOutputStream dos, int lengthOfBodyContent, String cookies) {
        try {
            dos.writeBytes("HTTP/1.1 401 Unauthorized\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");

            if(cookies != null) {
                dos.writeBytes("Set-Cookie: " + cookies + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
