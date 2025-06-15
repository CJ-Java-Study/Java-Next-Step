package webserver;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import controller.Controller;
import model.Http.HttpRequest;
import model.Http.HttpResponse;
import model.Http.HttpSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);

            // Ch.6 Add HttpSession
            // set cookie if it is a new session
            if(httpRequest.getCookie("JSESSIONID") == null){
                String uuid = UUID.randomUUID().toString();
                // Apparently it is okay and common to have multiple set-cookie lines in http response
                httpResponse.addHeader("Set-Cookie", "JSESSIONID=" + uuid);
                HttpSessions.add(uuid);
            }

            String path = httpRequest.getPath();
            Controller controller = RequestMapper.getController(httpRequest.getPath());

            if (controller == null){
                // set default path
                path = path.equals("/") ? "/index.html" : path;
                httpResponse.forward(path);
            } else {
                controller.service(httpRequest, httpResponse);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
