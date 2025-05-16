package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private final static String dev = "./week2/mawakeb/webapp";
    private final static String prd = "./webapp";

    public static Path loadPath(String url) throws IOException {
        // 로컬 환경
        Path devPath = Path.of(dev, url);
        if (Files.exists(devPath)) {
            return devPath;
        }

        // AWS 환경
        Path prodPath = Path.of(prd, url);
        if (Files.exists(prodPath)) {
           return prodPath;
        }

        throw new IOException("존재하지 않는 경로입니다. " + prd + url);
    }
}
