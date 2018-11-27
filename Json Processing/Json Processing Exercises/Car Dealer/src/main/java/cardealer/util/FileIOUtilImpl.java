package cardealer.util;

import java.io.*;

public class FileIOUtilImpl implements FileIOUtil {
    @Override
    public String readFile(String filePath) throws IOException {
        File file = new File(filePath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }


    @Override
    public void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);

        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();

    }
}
