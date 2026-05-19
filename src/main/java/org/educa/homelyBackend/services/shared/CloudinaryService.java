package org.educa.homelyBackend.services.shared;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadAvatarImage(MultipartFile avatarImageFile, Integer userId);

    String uploadAvatarImage(byte[] rawAvatarImageFile, Integer userId);

    String uploadPropertyImage(MultipartFile propertyImageFile, Integer propertyId, Integer propertyImageOrder);

    void deleteAvatarImage(Integer userId);

    void deletePropertyImage(Integer propertyId, Integer propertyImageOrder);
}
