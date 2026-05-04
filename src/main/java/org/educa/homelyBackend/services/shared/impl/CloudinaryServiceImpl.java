package org.educa.homelyBackend.services.shared.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.educa.homelyBackend.services.shared.CloudinaryService;
import org.educa.homelyBackend.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final String BASE_DIRECTORY = "homely";
    private static final String AVATARS_DIRECTORY = BASE_DIRECTORY + "/avatars";
    private static final String PROPERTIES_DIRECTORY = BASE_DIRECTORY + "/properties";

    private final Cloudinary cloudinary;

    @Override
    public String uploadAvatarImage(MultipartFile avatarImageFile, Integer userId) {
        return uploadImage(avatarImageFile, AVATARS_DIRECTORY, buildAvatarImageFileName(userId));
    }

    @Override
    public String uploadAvatarImage(byte[] rawAvatarImageFile, Integer userId) {
        return uploadImage(rawAvatarImageFile, AVATARS_DIRECTORY, buildAvatarImageFileName(userId));
    }

    @Override
    public String uploadPropertyImage(MultipartFile propertyImageFile, Integer propertyId, Integer propertyImageOrder) {
        return uploadImage(
                propertyImageFile, PROPERTIES_DIRECTORY, buildPropertyImageFileName(propertyId, propertyImageOrder)
        );
    }

    @Override
    public void deleteAvatarImage(Integer userId) {
        deleteImage(AVATARS_DIRECTORY + "/" + buildAvatarImageFileName(userId));
    }

    @Override
    public void deletePropertyImage(Integer propertyId, Integer propertyImageOrder) {
        deleteImage(PROPERTIES_DIRECTORY + "/" + buildPropertyImageFileName(propertyId, propertyImageOrder));
    }

    private String uploadImage(MultipartFile imageFile, String folder, String fileName) {
        byte[] rawImageFile;

        try {
            rawImageFile = imageFile.getBytes();
        } catch (IOException e) {
            throw ExceptionUtil.manageException(
                    e, HttpStatus.BAD_REQUEST, "Error al subir la imagen a %s/%s".formatted(folder, fileName)
            );
        }

        return uploadImage(rawImageFile, folder, fileName);
    }

    private String uploadImage(byte[] rawImageFile, String folder, String fileName) {
        try {
            return cloudinary.uploader()
                    .upload(rawImageFile, Map.of(
                            "folder", folder,
                            "public_id", fileName
                    ))
                    .get("secure_url")
                    .toString();
        } catch (IOException e) {
            throw ExceptionUtil.manageException(
                    e, HttpStatus.BAD_REQUEST, "Error al subir la imagen a %s/%s".formatted(folder, fileName)
            );
        }
    }

    private void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of("invalidate", true));
        } catch (IOException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.BAD_REQUEST, "Error al eliminar la imagen " + publicId);
        }
    }

    private String buildAvatarImageFileName(Integer userId) {
        return String.valueOf(userId);
    }

    private String buildPropertyImageFileName(Integer propertyId, Integer propertyImageOrder) {
        return propertyId + "-" + propertyImageOrder;
    }
}
