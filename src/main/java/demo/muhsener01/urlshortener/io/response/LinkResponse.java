package demo.muhsener01.urlshortener.io.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LinkResponse {
    private String shortLink;
    private String originalLink;
    private String status;
    private ExpirationResponse expiration;


    public LinkResponse(String shortLink, String originalLink, String status, ExpirationResponse expiration) {
        this.shortLink = shortLink;
        this.originalLink = originalLink;
        this.status = status;
        this.expiration = expiration;
    }
}

