package org.educa.homelyBackend.service.common;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file, String email) throws IOException {
        return uploadFile(file.getBytes(), email);
    }

    public String uploadFile(byte[] rawFileBytes, String email) throws IOException {
        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("folder", "homely");

        if (email != null && !email.isBlank()) uploadOptions.put("public_id", email);

        return cloudinary.uploader()
                .upload(rawFileBytes, uploadOptions)
                .get("secure_url")
                .toString();
    }

    public void deleteFile(String email) throws IOException {
        String publicId = "homely/" + email;

        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("invalidate", true);

        cloudinary.uploader().destroy(publicId, uploadOptions);
    }
}