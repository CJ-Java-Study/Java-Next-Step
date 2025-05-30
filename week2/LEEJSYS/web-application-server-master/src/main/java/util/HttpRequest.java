package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();

    public HttpRequest(InputStream in) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String requestLine = reader.readLine();
        if (requestLine == null) return;
        parseAndSaveRequestLine(requestLine);

        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.equals("")) {
            int idx = headerLine.indexOf(":");
            if (idx != -1) {
                headers.put(headerLine.substring(0, idx).trim(), headerLine.substring(idx + 1).trim());
            }
        }

        if ("POST".equals(method)) {
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
            String body = IOUtils.readData(reader, contentLength);
            parseQueryString(body);
        }
    }

    private void parseAndSaveRequestLine(String requestLine) throws Exception {
        String[] parts = requestLine.split(" ");
        this.method = parts[0];
        String fullPath = parts[1];

        int idx = fullPath.indexOf("?");
        if (idx != -1) {
            this.path = fullPath.substring(0, idx);
            parseQueryString(fullPath.substring(idx + 1));
        } else {
            this.path = fullPath;
        }
    }

    private void parseQueryString(String query) throws Exception {
        Map<String, String> parsed = HttpRequestUtils.parseQueryString(query);
        for (Map.Entry<String, String> entry : parsed.entrySet()) {
            String decodedKey = URLDecoder.decode(entry.getKey(), "UTF-8");
            String decodedValue = URLDecoder.decode(entry.getValue(), "UTF-8");
            parameters.put(decodedKey, decodedValue);
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
        return parameters.get(name);
    }
}
