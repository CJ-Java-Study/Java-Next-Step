package webserver;

import java.io.*;
import java.net.Socket;
import controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpResponse;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String WEBAPP_PATH = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            String path = getDefaultPath(request.getPath());
            Controller controller = RequestMapper.getController(path);

            if (controller == null) {
                response.forward(path);
                return;
            }

            controller.service(request, response);

        } catch (Exception e) {
            log.error("예외 발생: ", e);
        }
    }

    private String getDefaultPath(String path){
        if (path.equals("/")){
            return "/index.html";
        }
        return path;
    }
}
