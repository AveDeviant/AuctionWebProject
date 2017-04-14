package by.buslauski.auction.service;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Acer on 16.03.2017.
 */
public class Uploading {
    private static final String PART_HEADER = "content-disposition";
    private static final String PART_FILENAME_HEADER = "filename";

    public String uploadFile(String absolutePath, Part filePart) {
        try {
            InputStream stream = filePart.getInputStream();

            File path = new File(absolutePath);
            if (!path.exists()) {
                path.mkdir();
            }
            File data = new File(path, getFileNameFromPath(filePart));
            Files.copy(stream, data.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getFileNameFromPath(filePart);
    }


    private String getFileNameFromPath(Part part) {
        String[] data = part.getHeader(PART_HEADER).split(";");
        for (String st : data) {
            st = st.trim();
            if (st.startsWith(PART_FILENAME_HEADER)) {
                return st.substring(st.lastIndexOf("=") + 2, st.length() - 1);
            }
        }
        return "";
    }

}
