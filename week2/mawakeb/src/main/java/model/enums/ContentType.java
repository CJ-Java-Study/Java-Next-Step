package model.enums;

public enum ContentType {
    HTML("text/html;charset=utf-8"),
    CSS("text/css"),
    JS("application/javascript"),
    JSON("application/json"),
    PNG("image/png"),
    JPG("image/jpeg"),
    GIF("image/gif");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }

    public static ContentType fromFileName(String filename) {
        if (filename.endsWith(".css")) return CSS;
        if (filename.endsWith(".js")) return JS;
        if (filename.endsWith(".json")) return JSON;
        if (filename.endsWith(".png")) return PNG;
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return JPG;
        if (filename.endsWith(".gif")) return GIF;
        return HTML;
    }
}
