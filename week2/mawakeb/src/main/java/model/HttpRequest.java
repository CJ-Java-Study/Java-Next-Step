package model;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private Map<String,String> queryParams;

    private int contentLength;
    private Map<String,String> cookies;

    private String body;

    public HttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();

        String[] tokens = line.split(" ");
        if (tokens.length < 3 ){
            return;
        }
        this.method = tokens[0];
        this.path = tokens[1];

        int queryIndex = path.indexOf("?");
        if(queryIndex > 0) {
            String queryString = path.substring(queryIndex+1);
            queryParams = HttpRequestUtils.parseQueryString(queryString);
            path = path.substring(0, queryIndex);
        }

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            tokens = line.split(" ");
            if(tokens[0].equals("Content-Length:")){
                contentLength = Integer.parseInt(tokens[1]);
            }
            if(tokens[0].equals("Cookie:")){
                int index = line.indexOf(":") + 1;
                cookies = HttpRequestUtils.parseCookies(line.substring(index));
            }
        }

        body = IOUtils.readData(br, contentLength);
    }

    public boolean isGet(){
        return method.equals("GET");
    }

    public boolean isPost(){
        return method.equals("POST");
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getBody() {
        return body;
    }

    public Map<String,String> getCookies() {
        return cookies;
    }
}
