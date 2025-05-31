package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private Map<String, Object> values = new HashMap<>();

    private String sessionId;

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAttribute(String key, Object value) {
        values.put(key, value);
    }

    public Object getAttribute(String key) {
        return values.get(key);
    }

    public void removeAttribute(String key) {
        values.remove(key);
    }

    public void invalidate() {
        HttpSessions.removeSession(sessionId);
    }
}
