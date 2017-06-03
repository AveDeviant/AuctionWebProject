package by.buslauski.auction.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.time.LocalDateTime;

/**
 * @author Buslauski Mikita
 */
public class FileUploadingManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PART_HEADER = "content-disposition";
    private static final String PART_FILENAME_HEADER = "filename";
    private static final int TOO_LONG_FILENAME_LENGTH = 70;
    private static final int LENGTH_TO_DELETE = 60;

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
            LOGGER.log(Level.ERROR, e);
        }
        return getFileNameFromPath(filePart);
    }


    /**
     * Creating unique filename from part using <code>LocalDateTime</code>.
     *
     * @param part a {@link Part} from form item that was received within
     *             client {@link javax.servlet.http.HttpServletRequest}
     * @return filename of uploading file.
     */
    private String getFileNameFromPath(Part part) {
        String[] data = part.getHeader(PART_HEADER).split(";");
        for (String st : data) {
            st = st.trim();
            if (st.startsWith(PART_FILENAME_HEADER)) {
                StringBuilder stringBuilder = new StringBuilder();
                LocalDateTime now = LocalDateTime.now();
                String timeAsString = now.toString();
                // Detected problems with filename if I save milliseconds so I decided remove it.
                stringBuilder.append(timeAsString.substring((0), timeAsString.lastIndexOf('.')));
                if (st.length() <= TOO_LONG_FILENAME_LENGTH) {
                    stringBuilder.append(st.substring(st.lastIndexOf("=") + 2, st.length() - 1));
                } else {
                    stringBuilder.append(st.substring(st.lastIndexOf("=") + LENGTH_TO_DELETE, st.length() - 1));
                }
                String fileName = stringBuilder.toString();
                fileName = fileName.replaceAll(Character.toString(':'), "").trim();
                return fileName;
            }
        }
        return "";
    }

}
