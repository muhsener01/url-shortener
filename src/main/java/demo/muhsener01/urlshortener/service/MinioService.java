package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.utils.MinioConfigProperties;
import io.minio.*;
import io.minio.http.Method;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
@Data
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfigProperties minioConfigProperties;


    public MinioService(MinioConfigProperties minioConfigProperties) {
        this.minioConfigProperties = minioConfigProperties;
        String bucketName = minioConfigProperties.getBucketName();
        String url = minioConfigProperties.getUrl();
        String user = minioConfigProperties.getUser();
        String password = minioConfigProperties.getPassword();

        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(user, password)
                .build();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if (!found)
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());

        } catch (Exception e) {
            log.debug("Error while constructing minio clint due to: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public String putObject(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String objectName = multipartFile.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfigProperties.getBucketName())
                            .object(objectName)
                            .contentType(multipartFile.getContentType())
                            .stream(inputStream, multipartFile.getSize(), -1)
                            .build());


            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioConfigProperties.getBucketName())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
