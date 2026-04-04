package com.brewbuddy.app;

public interface SupabaseStorageRepository {

    String generateSignedUrl(String bucketName, String filePath);
    String generateSignedUrl(String bucketName, String filePath, int expiresIn);

}
