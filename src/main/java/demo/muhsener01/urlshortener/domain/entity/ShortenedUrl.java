package demo.muhsener01.urlshortener.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.exception.NotResolvableException;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "urls")
@NoArgsConstructor
@Getter
@Setter
public class ShortenedUrl extends BaseEntity<UUID> {


    private UUID userId;
    private String originalUrl;
    @Column(unique = true)
    private String shortenCode;
    @Enumerated(EnumType.STRING)
    private LinkStatus status;
    private LocalDateTime expiresAt;

    @Column(name = "expiration_policy", columnDefinition = "TEXT")
    private String expirationPolicyJson;

    @Transient
    private ExpirationPolicy expirationPolicy;


    public ShortenedUrl(UUID userId, String originalUrl, String shortenCode, ExpirationPolicy expirationPolicy) {
        this.userId = userId;
        this.originalUrl = originalUrl;
        this.shortenCode = shortenCode;
        this.expirationPolicy = expirationPolicy;

        initialize();

    }

    private void initialize() {
        this.id = UUID.randomUUID();
        this.status = LinkStatus.ACTIVE;
        expirationPolicy.initialize(this);

        expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }


    public void resolve() {
        if (!status.equals(LinkStatus.ACTIVE)) {
            throw new NotResolvableException("URL is not active to resolve!");
        }

        expirationPolicy.apply(this);

    }


    public void expire() {
        setStatus(LinkStatus.EXPIRED);
    }

    public void refreshExpirationPolicy() {
        this.expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }

    @PostLoad
    public void afterLoad() {
        this.expirationPolicy = JsonUtils.convertToObject(this.expirationPolicyJson, ExpirationPolicy.class);
    }


    @JsonIgnore
    public boolean isActive() {
        return status.equals(LinkStatus.ACTIVE);
    }


    public void remove() {
        this.status = LinkStatus.REMOVED;
    }
}
