package http;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
class HttpHeaders {
    private static final String COOKIE = "Cookie";

    private static final String CONTENT_LENGTH = "Content-Length";

    private Map<String, String> headers = new HashMap<>();

    void add(String header) {
        log.debug("header : {}", header);
        String[] splitedHeaders = header.split(":");
        headers.put(splitedHeaders[0], splitedHeaders[1].trim());
    }

    String getHeader(String name) {
        return headers.get(name);
    }

    int getIntHeader(String name) {
        String header = getHeader(name);
        return header == null ? 0 : Integer.parseInt(header);
    }

    int getContentLength() {
        return getIntHeader(CONTENT_LENGTH);
    }

    HttpCookie getCookies() {
        return new HttpCookie(getHeader(COOKIE));
    }

    HttpSession getSession() {
        return HttpSessions.getSession(getCookies().getCookie("JSESSIONID"));
    }
}
