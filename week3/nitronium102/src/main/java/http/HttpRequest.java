package http;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        // `1. 요청 라인 파싱: GET /index.html HTTP/1.1
        String line = br.readLine();
        if (line == null) {
            return;
        }
        log.debug("request line : {}", line);

        String[] tokens = line.split(" ");
        method = tokens[0];

        if (Objects.equals(method, "POST")) {
            path = tokens[1];
            return;
        }

        while (!line.isEmpty()) {
            log.debug("header : {}", line);
            String[] headerTokens = line.split(":");
            headers.put(headerTokens[0].trim(), headerTokens[1].trim());
            line = br.readLine();


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
        return params.get(name);
    }
}
