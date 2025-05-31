package model.Http;

public class HttpSessionUtil {

    public static boolean isLoggedIn(HttpSession session){
        return null != session.getAttribute("user");
    }
}
