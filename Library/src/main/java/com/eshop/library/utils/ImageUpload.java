package com.eshop.library.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class ImageUpload {
    private final String UPLOAD_FOLDER = "D:\\Idea\\ProTest\\demo\\E-shop\\Admin\\src\\main\\resources\\static\\img";

    /**
     * Uploads a file to the specified upload folder. If a file with the same name
     * already exists, it is replaced.
     *
     * @param file The MultipartFile to be uploaded.
     * @return True if the file was successfully uploaded, false otherwise.
     */
    public boolean uploadFile(MultipartFile file) {
        boolean isUpload = false;
        try {
            Path targetPath = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());

            // Check and delete the file if it already exists
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }

            // Copy the contents of the MultipartFile to the target path
            Files.copy(file.getInputStream(), targetPath);
            isUpload = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isUpload;
    }

    /**
     * Checks if a file with the same name as the original name of the MultipartFile
     * exists in the upload folder.
     *
     * @param multipartFile The MultipartFile to check.
     * @return True if a file with the same name exists, false otherwise.
     */
    public boolean checkExist(MultipartFile multipartFile) {
        boolean isExist = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + multipartFile.getOriginalFilename());
            isExist = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }
}
