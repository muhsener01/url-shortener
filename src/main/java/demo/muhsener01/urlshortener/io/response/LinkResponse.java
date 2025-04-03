package demo.muhsener01.urlshortener.io.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LinkResponse {
    private String id;
    private String type;
    private String shortLink;
    private String originalLink;
    private String status;
    private LocalDateTime createdAt;
    private ExpirationResponse expiration;



    public LinkResponse(String shortLink, String originalLink, String status, ExpirationResponse expiration) {
        this.shortLink = shortLink;
        this.originalLink = originalLink;
        this.status = status;
        this.expiration = expiration;
    }

    public LinkResponse(String type, String shortLink, String originalLink, String status, ExpirationResponse expiration) {
        this.type = type;
        this.shortLink = shortLink;
        this.originalLink = originalLink;
        this.status = status;
        this.expiration = expiration;
    }

    public LinkResponse(String id,String type, String shortLink, String originalLink, String status, LocalDateTime createdAt, ExpirationResponse expiration) {
        this.id = id ;
        this.type = type;
        this.shortLink = shortLink;
        this.originalLink = originalLink;
        this.status = status;
        this.createdAt = createdAt;
        this.expiration = expiration;
    }
}

