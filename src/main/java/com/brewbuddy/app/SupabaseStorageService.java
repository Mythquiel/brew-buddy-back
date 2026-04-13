package com.brewbuddy.app;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupabaseStorageService implements SupabaseStorageRepository {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service.role.key}")
    private String serviceRoleKey;

    /**
     * Generate a signed URL for a private image in Supabase Storage
     * @param bucketName The storage bucket name (e.g., "beverages")
     * @param filePath The file path within the bucket (e.g., "coffee-123.jpg")
     * @param expiresIn Duration in seconds (default: 3600 = 1 hour)
     * @return Signed URL that grants temporary access to the file
     */
    public String generateSignedUrl(String bucketName, String filePath, int expiresIn) {
        String url = String.format("%s/storage/v1/object/sign/%s/%s",
                supabaseUrl, bucketName, filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceRoleKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = Map.of("expiresIn", expiresIn);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getBody() != null && response.getBody().containsKey("signedURL")) {
            String signedPath = (String) response.getBody().get("signedURL");
            if (signedPath.startsWith("/object/")) {
                signedPath = "/storage/v1" + signedPath;
            }
            String fullUrl = supabaseUrl + signedPath;
            return fullUrl;
        }

        throw new RuntimeException("Failed to generate signed URL");
    }

    /**
     * Generate a signed URL with default expiration (1 hour)
     */
    public String generateSignedUrl(String bucketName, String filePath) {
        return generateSignedUrl(bucketName, filePath, 3600);
    }

    public String upload(String bucketName, String filePath, MultipartFile file) {
        String url = String.format("%s/storage/v1/object/%s/%s",
                supabaseUrl, bucketName, filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceRoleKey);
        headers.set("Content-Type", file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        headers.set("x-upsert", "true");

        try {
            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);
            restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return bucketName + "/" + filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }

    public void delete(String bucketName, String filePath) {
        String url = String.format("%s/storage/v1/object/%s/%s",
                supabaseUrl, bucketName, filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceRoleKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);
    }
}
