package com.final_project.ua_team_final_project.services;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DigitalOceanStorageService {

    private final S3Client s3Client;

    private final String BUCKET_NAME = Dotenv.load().get("DO_BUCKET_NAME");

    public List<String> listCsvFiles() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents().stream()
                .map(S3Object::key)
                .filter(key -> key.endsWith(".csv"))
                .collect(Collectors.toList());
    }

    public InputStream downloadFile(String fileName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();
        return s3Client.getObject(request);
    }

    public void uploadFile(String fileName, byte[] fileData) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .contentType("text/csv")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
