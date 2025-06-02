package http;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class HttpResponse {

    @Builder.Default
    private String version = "HTTP/1.1";

    @Builder.Default
    private Map<String, String> headers = new HashMap<>();

    @Builder.Default
    private byte[] body = new byte[0];

    private int statusCode;
    private String statusMessage;
}
