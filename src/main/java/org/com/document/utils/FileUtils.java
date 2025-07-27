package org.com.document.utils;

import org.com.document.enums.ErrorCode;
import org.com.document.exception.ApiException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Component
public class FileUtils {
    public static String getFileName(MultipartFile file) {
        return file.getOriginalFilename();
    }

    public static String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (!filename.contains(".")) {
            throw new ApiException(
                    ErrorCode.BAD_REQUEST.getStatusCode().value(),
                    "file",
                    "Extension file invalid."
            );
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public static String convertToBase64(MultipartFile file) throws Exception {
        byte[] fileBytes = file.getBytes();
        return Base64.getEncoder().encodeToString(fileBytes);
    }
}
