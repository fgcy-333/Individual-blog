package com.fgcy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author fgcy
 * @Date 2022/5/25
 */
@Component("COSConfig")
@ConfigurationProperties(prefix = "cos")
public class COSConfig {
    private String baseUrl;
    private String accessKey;
    private String secretKey;
    private String regionName;
    private String bucketName;
    private String folderPrefix;

    @Override
    public String toString() {
        return "COSConfig{" +
                "baseUrl='" + baseUrl + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", regionName='" + regionName + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", folderPrefix='" + folderPrefix + '\'' +
                '}';
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public COSConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public COSConfig setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public COSConfig setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public String getRegionName() {
        return regionName;
    }

    public COSConfig setRegionName(String regionName) {
        this.regionName = regionName;
        return this;
    }

    public String getBucketName() {
        return bucketName;
    }

    public COSConfig setBucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public String getFolderPrefix() {
        return folderPrefix;
    }

    public COSConfig setFolderPrefix(String folderPrefix) {
        this.folderPrefix = folderPrefix;
        return this;
    }
}
