package http;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessions {
    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    /**
     * 새로운 세션 생성 후 반환
     */
    public static HttpSession createSession() {
        String sessionId = UUID.randomUUID().toString();

        HttpSession session = new HttpSession(sessionId);
        sessions.put(sessionId, session);
        return session;
    }

    /** 기존 세션 조회, 존재하지 않으면 null 반환 */
    public static HttpSession getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /** 세션 무효화 시 호출: 맵에서 제거 */
    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}

