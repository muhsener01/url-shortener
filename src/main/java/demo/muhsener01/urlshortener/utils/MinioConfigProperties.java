package demo.muhsener01.urlshortener.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("minio")
@Component
@Data
public class MinioConfigProperties {

    private String url;
    private String user;
    private String password;
    private String bucketName;
}
