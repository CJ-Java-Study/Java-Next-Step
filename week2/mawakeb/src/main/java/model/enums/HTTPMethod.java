package model.enums;

public enum HTTPMethod {
    GET,
    POST;

    public boolean isGet(){
        return this == GET;
    }

    public boolean isPost(){
        return this == POST;
    }

    public static HTTPMethod parseMethod(String method) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException();
        }

        String upperMethod = method.trim().toUpperCase();
        for (HTTPMethod httpMethod : HTTPMethod.values()) {
            if (httpMethod.name().equals(upperMethod)) {
                return httpMethod;
            }
        }

        throw new IllegalArgumentException();
    }
}
