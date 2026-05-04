package org.educa.homelyBackend.services.shared;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
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
        String fileName = String.valueOf(userId);
        byte[] rawAvatarImageFile;

        try {
            rawAvatarImageFile = avatarImageFile.getBytes();
        } catch (IOException e) {
            throw ExceptionUtil.manageException(
                    e,
                    HttpStatus.BAD_REQUEST,
                    "Error al leer el archivo de imagen del avatar para el usuario con ID " + userId
            );
        }

        return uploadImage(rawAvatarImageFile, AVATARS_DIRECTORY, fileName);
    }

    @Override
    public String uploadAvatarImage(byte[] rawAvatarImageFile, Integer userId) {
        return uploadImage(rawAvatarImageFile, AVATARS_DIRECTORY, String.valueOf(userId));
    }

    @Override
    public String uploadPropertyImage(MultipartFile propertyImageFile, Integer propertyId, Integer propertyImageOrder) {
        return "";
    }

    @Override
    public void deleteAvatarImage(Integer userId) {
        deleteImage(AVATARS_DIRECTORY + "/" + userId);
    }

    @Override
    public void deletePropertyImage(Integer propertyId, Integer propertyImageOrder) {
        deleteImage(PROPERTIES_DIRECTORY + "/" + propertyId + "-" + propertyImageOrder);
    }

    private String uploadImage(byte[] rawImageFile, String folder, String fileName) {
        try {
            return cloudinary.uploader()
                    .upload(rawImageFile, Map.of(
                            "folder", folder,
                            "public_id", fileName)
                    )
                    .get("secure_url")
                    .toString();
        } catch (IOException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.BAD_REQUEST, "Error al subir la imagen " + fileName);
        }
    }

    private void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, Map.of("invalidate", true));
        } catch (IOException e) {
            throw ExceptionUtil.manageException(e, HttpStatus.BAD_REQUEST, "Error al eliminar la imagen " + publicId);
        }
    }
}
