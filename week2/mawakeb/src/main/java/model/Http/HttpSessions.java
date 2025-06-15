package model.Http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {

    private static Map<String, HttpSession> sessions = new HashMap<>();

    public static HttpSession get(String id){
        return sessions.get(id);
    }

    public static void add(String id){
        sessions.put(id, new HttpSession(id));
    }

    public static void remove(String id){
        sessions.remove(id);
    }
}
