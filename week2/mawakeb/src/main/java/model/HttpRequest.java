package model;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private HTTPMethod method;
    private String path;
    private Map<String,String> header = new HashMap<>();
    private Map<String,String> cookies;
    private Map<String,String> parameters;

    public HttpRequest(InputStream in) throws IOException {
        InputStreamReader sr = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(sr);
        String line = br.readLine();
        if (line == null) return;

        parseRequestLine(line);

        if (method.isGet()) parseQueryString();

        parseHeaders(br);

        if (method.isPost()) parseBody(br);
    }

    private void parseRequestLine(String line) {
        String[] tokens = line.split(" ");
        if (tokens.length < 3 ){
            return;
        }
        this.method = HTTPMethod.parseMethod(tokens[0]);
        this.path = tokens[1];
    }

    private void parseQueryString(){
        int queryIndex = path.indexOf("?");
        if(queryIndex > 0) {
            String queryString = path.substring(queryIndex+1);
            parameters = HttpRequestUtils.parseQueryString(queryString);
            path = path.substring(0, queryIndex);
        }
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        String line;
        String[] tokens;

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            tokens = line.split(": ");

            if(tokens[0].equals("Cookie:")){
                int index = line.indexOf(":") + 1;
                cookies = HttpRequestUtils.parseCookies(line.substring(index));
                continue;
            }

            header.put(tokens[0], tokens[1]);
        }
    }

    private void parseBody(BufferedReader br) throws IOException {
        String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
        parameters = HttpRequestUtils.parseQueryString(body);
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String,String> getCookies() {
        return cookies;
    }

    public String getHeader(String key) {
        return header.get(key);
    }

    public Map<String,String> getParametersMap(){
        return parameters;
    }

    public String getParameter(String key){
        return parameters.get(key);
    }
}
