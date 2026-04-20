package org.educa.homelyBackend.services.common;

import com.cloudinary.Cloudinary;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Validated
public class CloudinaryService {

    private static final String BASE_DIRECTORY = "homely";

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadAvatarImage(
            @NotNull(message = "Avatar image file is null")
            MultipartFile avatarImageFile,

            @NotNull(message = "ID user is null")
            Integer userId
    ) throws IOException {
        return uploadAvatarImage(avatarImageFile.getBytes(), userId);
    }

    public String uploadAvatarImage(
            @NotNull(message = "Avatar image is null")
            byte[] rawAvatarImage,

            @NotNull(message = "ID user is null")
            Integer userId
    ) throws IOException {
        String subfolder = "avatars";
        String fileName = String.valueOf(userId);

        return uploadImage(rawAvatarImage, buildUploadOptions(subfolder, fileName));
    }

    public String uploadPropertyImage(
            @NotNull(message = "Property image file is null")
            MultipartFile propertyImageFile,

            @NotNull(message = "ID property is null")
            Integer propertyId,

            @NotNull(message = "Property image order is null")
            Integer propertyImageOrder
    ) throws IOException {
        return uploadPropertyImage(propertyImageFile.getBytes(), propertyId, propertyImageOrder);
    }

    public String uploadPropertyImage(
            @NotNull(message = "Property image is null")
            byte[] rawPropertyImage,

            @NotNull(message = "ID property is null")
            Integer propertyId,

            @NotNull(message = "Property image order is null")
            Integer propertyImageOrder
    ) throws IOException {
        String subfolder = "properties";
        String fileName = propertyId + "-" + propertyImageOrder;

        return uploadImage(rawPropertyImage, buildUploadOptions(subfolder, fileName));
    }

    public void deleteAvatarImage(
            @NotNull(message = "ID user is null")
            Integer userId
    ) throws IOException {
        String publicId = BASE_DIRECTORY + "/avatars/" + userId;
        deleteImage(publicId, buildDeleteOptions());
    }

    public void deletePropertyImage(
            @NotNull(message = "ID property is null")
            Integer propertyId,

            @NotNull(message = "Property image order is null")
            Integer propertyImageOrder
    ) throws IOException {
        String publicId = BASE_DIRECTORY + "/properties/" + propertyId + "-" + propertyImageOrder;
        deleteImage(publicId, buildDeleteOptions());
    }

    private String uploadImage(byte[] rawImage, Map<String, String> uploadOptions) throws IOException {
        return cloudinary.uploader()
                .upload(rawImage, uploadOptions)
                .get("secure_url")
                .toString();
    }

    private Map<String, String> buildUploadOptions(String subFolder, String fileName) {
        return Map.of(
                "folder", BASE_DIRECTORY + "/" + subFolder,
                "public_id", fileName
        );
    }

    private void deleteImage(String publicId, Map<String, Object> deleteOptions) throws IOException {
        cloudinary.uploader().destroy(publicId, deleteOptions);
    }

    private Map<String, Object> buildDeleteOptions() {
        return Map.of(
                "invalidate", true
        );
    }
}