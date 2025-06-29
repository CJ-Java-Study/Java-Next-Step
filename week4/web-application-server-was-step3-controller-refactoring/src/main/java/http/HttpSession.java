package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    private final String id;
    private final Map<String, Object> attributes = new HashMap<>();
    private boolean isValid = true;

    // ID는 외부(HttpSessions)에서 생성해 전달받음
    public HttpSession(String id) {
        this.id = id;
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
        HttpSessions.remove(id); // 세션 저장소에서도 제거
    }

    private void checkValid() {
        if (!isValid) {
            throw new IllegalStateException("세션이 이미 무효화되었습니다.");
        }
    }
}
