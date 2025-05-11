package model;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpHeader {

    private String method;
    private String path;
    private String queryParams;
    private int contentLength;

    public HttpHeader(BufferedReader br) throws IOException {
        String line = br.readLine();

        String[] tokens = line.split(" ");
        if (tokens.length < 3 ){
            return;
        }
        this.method = tokens[0];
        this.path = tokens[1];

        int queryIndex = path.indexOf("?");
        if(queryIndex > 0) {
            queryParams = path.substring(queryIndex+1);
            path = path.substring(0, queryIndex);
        }

        while ((line = br.readLine()) != null && !line.isEmpty()) {
            tokens = line.split(" ");
            if(tokens[0].equals("Content-Length:")){
                contentLength = Integer.parseInt(tokens[1]);
            }
        }

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

    public String getQueryParams() {
        return queryParams;
    }

    public int getContentLength() {
        return contentLength;
    }
}
