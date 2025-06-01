package http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSession {
    // 세션 ID
    private final String sessionId;

    // 세션에 저장된 속성들
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    // 세션 무효화 여부 플래그
    private boolean invalidated = false;

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return sessionId;
    }

    public void setAttribute(String name, Object value) {
        if (invalidated) {
            throw new IllegalStateException("이미 무효화된 세션입니다.");
        }
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        if (invalidated) {
            throw new IllegalStateException("이미 무효화된 세션입니다.");
        }
        return attributes.get(name);
    }

    public void removeAttribute(String name) {
        if (invalidated) {
            throw new IllegalStateException("이미 무효화된 세션입니다.");
        }
        attributes.remove(name);
    }

    public void invalidate() {
        // 1) 속성 초기화
        attributes.clear();
        // 2) 무효화 플래그
        invalidated = true;
        // 3) 세션 매니저에게 자신을 제거해 달라고 요청 (세션 매니저 코드를 호출)
        HttpSessions.removeSession(sessionId);
    }

}
