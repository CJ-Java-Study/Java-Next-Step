package model.Http;

public class HttpSessionUtil {

    public static final String USER_SESSION_KEY = "user";

    public static boolean isLoggedIn(HttpSession session){
        return null != session.getAttribute(USER_SESSION_KEY);
    }
}
