package com.brewbuddy.app;

import org.springframework.web.multipart.MultipartFile;

public interface SupabaseStorageRepository {

    String generateSignedUrl(String bucketName, String filePath);
    String generateSignedUrl(String bucketName, String filePath, int expiresIn);
    String upload(String bucketName, String filePath, MultipartFile file);
    void delete(String bucketName, String filePath);

}
