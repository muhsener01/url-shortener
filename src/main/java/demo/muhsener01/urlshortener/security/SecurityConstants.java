package demo.muhsener01.urlshortener.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityConstants {

    private final String signUpUrl = "/api/v1/auth/sign-up";
    private final String headerName = "Authorization";
    private String tokenPrefix;
    private String loginUrl;
    private String secretKey;
    private Long tokenExpirationTime;
    private String adminUsername;
    private String adminPassword;
    private String adminEmail;


}
