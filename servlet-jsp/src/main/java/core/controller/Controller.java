package core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    /**
     * redirect로 이동할 경우 redirect:/로 시작하는 URL을 반환합니다.
     * forward로 이동할 경우 jsp 경로를 반환합니다
     */
    String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
