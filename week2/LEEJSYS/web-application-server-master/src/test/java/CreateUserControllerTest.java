import controller.Controller;
import controller.CreateUserController;
import org.junit.Test;
import util.HttpRequest;
import util.HttpResponse;

import java.io.*;

public class CreateUserControllerTest {
    private final String testDirectory = "./src/test/resources/";

    @Test
    public void testUserCreation() throws Exception {
        InputStream in = new FileInputStream(new File(testDirectory + "Http_CreateUser.txt"));
        OutputStream out = new FileOutputStream(new File(testDirectory + "Http_CreateUser_Response.txt"));

        HttpRequest request = new HttpRequest(in);
        HttpResponse response = new HttpResponse(out);

        Controller controller = new CreateUserController();
        controller.service(request, response);

    }
}
