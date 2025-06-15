package http;

import java.util.Map;

public enum HttpResponseEnum {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    FOUND(302, "Found");

    private final int code;
    private final String message;

    HttpResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /** 빈 바디 응답용 빌더 */
    public HttpResponse.HttpResponseBuilder builder() {
        return HttpResponse.builder()
                .statusCode(code)
                .statusMessage(message);
    }

    /** 바디만 있는 응답용 빌더 */
    public HttpResponse.HttpResponseBuilder builderWithBody(byte[] body) {
        return builder()
                .body(body);
    }

    /** 바디ㆍ헤더 있는 응답용 빌더 */
    public HttpResponse.HttpResponseBuilder builderWithBodyAndHeaders(byte[] body, Map<String,String> headers) {
        return builderWithBody(body)
                .headers(headers);
    }
}
