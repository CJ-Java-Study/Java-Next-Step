package http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {

    private final String id;
    private final Map<String, Object> attributes = new HashMap<>();
    private boolean isValid = true;

    public HttpSession() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setAttribute(String name, Object value) {
        checkValid();
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        checkValid();
        return attributes.get(name);
    }

    public void removeAttribute(String name) {
        checkValid();
        attributes.remove(name);
    }

    public void invalidate() {
        checkValid();
        attributes.clear();
        isValid = false;
    }

    private void checkValid() {
        if (!isValid) {
            throw new IllegalStateException("세션이 이미 무효화되었습니다.");
        }
    }
}
