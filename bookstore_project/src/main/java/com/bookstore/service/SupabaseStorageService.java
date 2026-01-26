package com.bookstore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${bookstore.supabase.url}")
    private String supabaseUrl;

    @Value("${bookstore.supabase.bucket}")
    private String bucket;

    @Value("${bookstore.supabase.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadBookImage(Long bookId, MultipartFile file) {
        try {
            String extension = getExtension(file.getOriginalFilename());
            String filename = "book-" + bookId + "-" + UUID.randomUUID() + "." + extension;

            String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + encoded;

            System.out.println("UPLOAD URL = " + uploadUrl);
            System.out.println("CONTENT TYPE = " + file.getContentType());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + serviceKey);
            headers.set("apikey", serviceKey);
            headers.set("x-upsert", "true");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.POST,   // ✅ Supabase expects POST for upload
                    request,
                    String.class
            );

            System.out.println("SUPABASE RESPONSE = " + response.getStatusCode());
            System.out.println("SUPABASE BODY = " + response.getBody());

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Upload failed: " + response.getBody());
            }

            // Return PUBLIC URL for frontend
            return supabaseUrl +
                    "/storage/v1/object/public/" +
                    bucket +
                    "/" +
                    filename;

        } catch (HttpStatusCodeException e) {
            System.out.println("SUPABASE STATUS = " + e.getStatusCode());
            System.out.println("SUPABASE ERROR BODY = " + e.getResponseBodyAsString());
            throw new RuntimeException("Upload failed: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Upload failed", e);
        }
    }
    
    
    private String getExtension(String name) {
        if (name == null || !name.contains(".")) {
            return "png"; // default fallback
        }
        return name.substring(name.lastIndexOf('.') + 1).toLowerCase();
    }

}
