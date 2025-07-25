package next.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.db.DataBase;
import next.controller.common.AbstractController;

public class HomeController extends AbstractController {
    private static final long serialVersionUID = 1L;

    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users", DataBase.findAll());
        return "index.jsp";
    }
}
