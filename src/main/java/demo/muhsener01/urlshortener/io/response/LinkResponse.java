package demo.muhsener01.urlshortener.io.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LinkResponse {
    @Schema(example = "abcs342")
    private String id;
    @Schema(example = "[ URL || IMAGE || TEXT ]")
    private String type;
    @Schema(example = "https://www.shortenly.com/abcs342")
    private String shortLink;

    @Schema(example = "https://www.instragram.com")
    private String originalLink;
    @Schema(example = "[ ACTIVE || PASSIVE || EXPIRED || REMOVED ]")
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

