package core.mvc;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapping {
    private final Map<String, Controller> mappings = new HashMap<>();

    public ControllerMapping(String basePackage) {
        String path = basePackage.replace('.', '/');
        URL dirURL = getResource(path);
        File dir = new File(dirURL.getFile());

        for (File file : dir.listFiles((d, name) -> name.endsWith(".class"))) {
            registerController(basePackage, file.getName());
        }
    }

    private URL getResource(String path) {
        return ControllerMapping.class.getClassLoader().getResource(path);
    }

    private void registerController(String basePackage, String fileName) {
        String className = basePackage + "." + fileName.substring(0, fileName.length() - 6);
        try {
            Class<?> cls = Class.forName(className);
            if (isValidController(cls)) {
                RequestMapping ann = cls.getAnnotation(RequestMapping.class);
                Controller controller = (Controller) cls.getDeclaredConstructor().newInstance();
                mappings.put(ann.value(), controller);
            }
        } catch (Exception e) {
            throw new RuntimeException("컨트롤러 등록 실패: " + className, e);
        }
    }

    private boolean isValidController(Class<?> cls) {
        return Controller.class.isAssignableFrom(cls) && cls.isAnnotationPresent(RequestMapping.class);
    }

    public Controller findController(String requestUri) {
        String path = requestUri.split("\\?")[0];
        return mappings.get(path);
    }
}