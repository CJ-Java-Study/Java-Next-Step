package http;

import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequest {

    private String method;

    private String path;

    private Map<String, String> headers = new HashMap<>();

    private Map<String, String> parameter = new HashMap<>();

    public HttpRequest(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            parseRequestLine(br);
            parseHeaders(br);
            parseBodyIfNecessary(br);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void parseRequestLine(BufferedReader br) throws Exception {
        String line = br.readLine();
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Invalid request line: Request line is null or empty");
        }
        log.info("request line : {}", line);

        String[] tokens = line.split(" ");
        if (tokens.length < 2) {
            throw new IllegalArgumentException("Invalid request line: Not enough tokens");
        }
        method = tokens[0];

        int index = tokens[1].indexOf("?");
        if (index == -1) {
            path = tokens[1];
        } else {
            path = tokens[1].substring(0, index);
            parameter = HttpRequestUtils.parseQueryString(tokens[1].substring(index + 1));
        }
    }

    private void parseHeaders(BufferedReader br) throws Exception {
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            log.debug("header : {}", line);
            String[] headerTokens = line.split(": ");
            if (headerTokens.length < 2) {
                throw new IllegalArgumentException("Invalid header: Not enough tokens");
            }
            headers.put(headerTokens[0].trim(), headerTokens[1].trim());
        }
    }

    private void parseBodyIfNecessary(BufferedReader br) throws Exception {
        if (method.equals("POST")) {
            String contentLength = headers.get("Content-Length");
            if (contentLength != null) {
                int length = Integer.parseInt(contentLength);
                String body = IOUtils.readData(br, length);
                parameter.putAll(HttpRequestUtils.parseQueryString(body));
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParameter(String name) {
        return parameter.get(name);
    }
}
