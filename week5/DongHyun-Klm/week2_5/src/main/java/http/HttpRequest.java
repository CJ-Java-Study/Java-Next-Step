package http;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Getter
@Slf4j
public class HttpRequest {
    private RequestLine requestLine;
    private RequestParams requestParams = new RequestParams();
    private HttpHeaders headers;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = new RequestLine(createRequestLine(br));
            requestParams.addQueryString(requestLine.getQueryString());
            headers = processHeaders(br);
            requestParams.addBody(IOUtils.readData(br, headers.getContentLength()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpHeaders processHeaders(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line;
        while (!(line = br.readLine()).equals("")) {
            headers.add(line);
        }
        return headers;
    }

    private String createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException();
        }
        return line;
    }

    public HttpCookie getCookies() {
        return headers.getCookies();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public String getHeader(String name) {
        return headers.getHeader(name);
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getParameter(String name) {
        return requestParams.getParameter(name);
    }

    public HttpSession getSession() {
        return headers.getSession();
    }

}

