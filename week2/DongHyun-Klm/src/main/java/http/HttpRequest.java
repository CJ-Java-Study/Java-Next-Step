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
import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
public class HttpRequest {
    private HttpMethod method;
    private String url;
    private String path;
    private Map<String, String> queryString;
    private Map<String, String> bodyParams;
    private int contentLength;
    private Map<String, String> headers;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 첫 줄 파싱
            String firstLine = br.readLine();
            String[] tokens = firstLine.split(" ");
            if(tokens.length != 3) {
                throw new IllegalArgumentException(firstLine + "이 형식에 맞지 않습니다.");
            }
            this.method = HttpMethod.valueOf(tokens[0]);
            this.url = tokens[1];
            this.path = HttpRequestUtils.getPath(url);
            this.queryString = HttpRequestUtils.parseQueryString(HttpRequestUtils.getQueryString(url));

            // 나머지 헤더 파싱
            this.headers = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] keyValue = line.split(":", 2);
                headers.put(keyValue[0].trim(), keyValue[1].trim());
            }

            this.contentLength = headers.containsKey("Content-Length")
                    ? Integer.parseInt(headers.get("Content-Length"))
                    : 0;

            // Post body 파싱
            if (this.method.isPost() && this.contentLength > 0) {
                String body = IOUtils.readData(br, contentLength);
                this.bodyParams = HttpRequestUtils.parseQueryString(body);
            }
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }
    }

    public String getParameter(String name) {
        // POST 파라미터 우선, 없으면 GET 쿼리스트링
        if (this.bodyParams != null && this.bodyParams.containsKey(name)) {
            return this.bodyParams.get(name);
        }
        return queryString.get(name);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public boolean isLogin() {
        String cookie = headers.get("Cookie");
        return cookie != null && cookie.contains("logined=true");
    }
}

