package com.stayhub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.upload")
public class StorageProperties {

    private boolean useCloudinary;
    private String localDirectory = "uploads";
    private String localBaseUrl = "/uploads";
    private DataSize maxFileSize = DataSize.ofMegabytes(10);
    private int maxImagesPerProperty = 10;
    private Cloudinary cloudinary = new Cloudinary();

    @Getter
    @Setter
    public static class Cloudinary {
        private String cloudName;
        private String apiKey;
        private String apiSecret;
    }
}
