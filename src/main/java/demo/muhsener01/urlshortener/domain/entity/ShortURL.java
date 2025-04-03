package demo.muhsener01.urlshortener.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.enums.LinkType;
import demo.muhsener01.urlshortener.exception.NotResolvableException;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "short_urls")
@NoArgsConstructor
@Getter
@Setter
public class ShortURL extends BaseEntity<String> {


    private UUID userId;

    @Column(length = 1000)
    private String originalUrl;

    @Enumerated(EnumType.STRING)
    private LinkStatus status;

    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    @Column(name = "expiration_policy", columnDefinition = "TEXT")
    private String expirationPolicyJson;


    @Transient
    private ExpirationPolicy expirationPolicy;


    public ShortURL(UUID userId, String originalUrl, ExpirationPolicy expirationPolicy, LinkType linkType) {
        this.userId = userId;
        this.originalUrl = originalUrl;
        this.expirationPolicy = expirationPolicy;
        this.linkType = linkType;
        initialize();

    }

    public ShortURL(String id, UUID userId, String originalUrl, ExpirationPolicy expirationPolicy, LinkType linkType) {
        this.id = id;
        this.userId = userId;
        this.originalUrl = originalUrl;
        this.expirationPolicy = expirationPolicy;
        this.linkType = linkType;
        initialize();

    }

    private void initialize() {
        this.status = LinkStatus.ACTIVE;
        expirationPolicy.initialize(this);
        expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }


    public void resolve() {
        if (!status.equals(LinkStatus.ACTIVE))
            throw new NotResolvableException("URL is not active to resolve!");

        expirationPolicy.apply(this);

    }

    @JsonIgnore
    public boolean isRemoved() {
        return status.equals(LinkStatus.REMOVED);
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


    public void setExpirationPolicy(ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
        expirationPolicy.initialize(this);
        this.expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }
}
