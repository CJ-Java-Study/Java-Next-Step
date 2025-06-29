package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static Map<String, HttpSession> sessions = new HashMap<>();

    public static HttpSession getSession(String sessionId) {
        HttpSession session = sessions.get(sessionId);

        if (session == null) {
            session = new HttpSession(sessionId);
            sessions.put(sessionId, session);
        }
        return session;
    }

    static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
