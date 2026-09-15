package com.stayhub.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    StoredFile store(String folder, MultipartFile file);

    void delete(String publicId);
}
