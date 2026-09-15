package com.stayhub.storage;

import static org.assertj.core.api.Assertions.assertThat;

import com.stayhub.config.StorageProperties;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class LocalStorageServiceTest {

    @TempDir
    Path tempDirectory;

    @Test
    void storesAndDeletesGeneratedFileInsideConfiguredDirectory() {
        StorageProperties properties = new StorageProperties();
        properties.setLocalDirectory(tempDirectory.toString());
        properties.setLocalBaseUrl("/uploads");
        LocalStorageService storageService = new LocalStorageService(properties);
        MockMultipartFile file = new MockMultipartFile(
                "file", "ignored.jpg", "image/jpeg", new byte[] {1, 2, 3});

        StoredFile storedFile = storageService.store("properties/42", file);

        assertThat(storedFile.url()).startsWith("/uploads/properties/42/").endsWith(".jpg");
        assertThat(Files.exists(tempDirectory.resolve(storedFile.publicId()))).isTrue();

        storageService.delete(storedFile.publicId());
        assertThat(Files.exists(tempDirectory.resolve(storedFile.publicId()))).isFalse();
    }
}
