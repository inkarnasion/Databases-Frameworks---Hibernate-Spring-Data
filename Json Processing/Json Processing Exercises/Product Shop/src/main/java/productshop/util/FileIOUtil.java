package productshop.util;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileIOUtil {

    String readFile(String filePath) throws IOException;
    void writeFile(String filePath, String content) throws IOException;
}
