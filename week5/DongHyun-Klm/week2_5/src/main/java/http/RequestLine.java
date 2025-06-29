package http;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLine {

    private HttpMethod method;

    private String path;

    private String queryString;


    public RequestLine(String requestLine) {
        log.debug("request line : {}", requestLine);
        String[] tokens = requestLine.split(" ");
        this.method = HttpMethod.valueOf(tokens[0]);

        String[] url = tokens[1].split("\\?");
        this.path = url[0];

        if (url.length == 2) {
            this.queryString = url[1];
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}