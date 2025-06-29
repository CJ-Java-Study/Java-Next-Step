package http;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpCookie {
    private Map<String, String> cookies;

    HttpCookie(String value) {
        cookies = HttpRequestUtils.parseCookies(value);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

}