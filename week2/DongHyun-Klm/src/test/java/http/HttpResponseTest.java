package http;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;


public class HttpResponseTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void responseForward() throws Exception {
        // Http_Forward.txt 파일에는 body에 index.html이 포함되어 있어야 함.
        HttpResponder response =
                new HttpResponder(createOutputStream("Http_Forward.txt"));
        response.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws Exception {
        // Http_Redirect.txt 파일에는 header에
        // Location 정보가 /index.html 표시되어 있어야 함.
        HttpResponder response =
                new HttpResponder(createOutputStream("Http_Redirect.txt"));
        response.sendRedirect("/index.html");
    }

    @Test
    public void responseCookies() throws Exception {
        // Http_Cookie.txt 파일에는 header에 Set-Cookie 값으로
        // logined=true 정보가 있어야함.
        HttpResponder response =
                new HttpResponder(createOutputStream("Http_Cookie.txt"));
        response.addHeader("Set-Cookie", "logined=true");
        response.sendRedirect("/index.html");
    }

    private OutputStream createOutputStream(String filename)
            throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
}
