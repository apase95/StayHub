package com.stayhub.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.stayhub.config.StorageProperties;
import java.io.IOException;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "app.upload.use-cloudinary", havingValue = "true")
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    public CloudinaryStorageService(StorageProperties properties) {
        StorageProperties.Cloudinary settings = properties.getCloudinary();
        if (isBlank(settings.getCloudName()) || isBlank(settings.getApiKey()) || isBlank(settings.getApiSecret())) {
            throw new IllegalStateException("Cloudinary credentials are required when cloud upload is enabled");
        }
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", settings.getCloudName(),
                "api_key", settings.getApiKey(),
                "api_secret", settings.getApiSecret(),
                "secure", true
        ));
    }

    @Override
    public StoredFile store(String folder, MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "stayhub/" + folder,
                    "resource_type", "image"
            ));
            return new StoredFile(result.get("secure_url").toString(), result.get("public_id").toString());
        } catch (IOException exception) {
            throw new StorageException("Unable to upload the property image.", exception);
        }
    }

    @Override
    public void delete(String publicId) {
        if (isBlank(publicId)) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException exception) {
            throw new StorageException("Unable to delete the property image.", exception);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
