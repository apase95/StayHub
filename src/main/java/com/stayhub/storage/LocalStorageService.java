package com.stayhub.storage;

import com.stayhub.config.StorageProperties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@ConditionalOnProperty(name = "app.upload.use-cloudinary", havingValue = "false", matchIfMissing = true)
public class LocalStorageService implements StorageService {

    private final Path rootDirectory;
    private final String baseUrl;

    public LocalStorageService(StorageProperties properties) {
        this.rootDirectory = Path.of(properties.getLocalDirectory()).toAbsolutePath().normalize();
        this.baseUrl = stripTrailingSlash(properties.getLocalBaseUrl());
    }

    @Override
    public StoredFile store(String folder, MultipartFile file) {
        String extension = extensionFor(file.getContentType());
        String publicId = folder + "/" + UUID.randomUUID() + extension;
        Path target = safePath(publicId);
        Path temporaryFile = null;

        try {
            Files.createDirectories(target.getParent());
            temporaryFile = Files.createTempFile(target.getParent(), ".upload-", ".tmp");
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, temporaryFile, StandardCopyOption.REPLACE_EXISTING);
            }
            Files.move(temporaryFile, target, StandardCopyOption.ATOMIC_MOVE);
            return new StoredFile(baseUrl + "/" + publicId, publicId);
        } catch (IOException exception) {
            deletePartialFile(temporaryFile);
            throw new StorageException("Unable to store the property image.", exception);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            return;
        }
        try {
            Files.deleteIfExists(safePath(publicId));
        } catch (IOException exception) {
            throw new StorageException("Unable to delete the property image.", exception);
        }
    }

    private Path safePath(String publicId) {
        Path path = rootDirectory.resolve(publicId).normalize();
        if (!path.startsWith(rootDirectory)) {
            throw new StorageException("Invalid storage path.");
        }
        return path;
    }

    private String extensionFor(String contentType) {
        return switch (contentType == null ? "" : contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new StorageException("Unsupported image type.");
        };
    }

    private String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private void deletePartialFile(Path path) {
        if (path == null) {
            return;
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // The original storage failure remains the actionable error.
        }
    }
}
