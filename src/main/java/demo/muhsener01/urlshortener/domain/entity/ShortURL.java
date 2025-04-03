package demo.muhsener01.urlshortener.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import demo.muhsener01.urlshortener.CacheEvictListener;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.enums.LinkType;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "short_urls")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(value = {AuditingEntityListener.class, CacheEvictListener.class})
public class ShortURL extends BaseEntity<String> {


    private UUID userId;
    //TODO : assumed that user will not change its email.
    private String creatorEmail;

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


    public ShortURL(UUID userId, String creatorEmail, String originalUrl, ExpirationPolicy expirationPolicy, LinkType linkType) {
        this(null, userId, creatorEmail, originalUrl, expirationPolicy, linkType);
    }

    public ShortURL(String id, UUID userId, String creatorEmail, String originalUrl, ExpirationPolicy expirationPolicy, LinkType linkType) {
        this.id = id;
        this.userId = userId;
        this.originalUrl = originalUrl;
        this.expirationPolicy = expirationPolicy;
        this.linkType = linkType;
        this.creatorEmail = creatorEmail;
        initialize();

    }

    private void initialize() {
        this.status = LinkStatus.ACTIVE;
        expirationPolicy.initialize(this);
        expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }


    public boolean resolve() {
        if (!isActive())
            return false;

        return expirationPolicy.apply(this);

    }

    @JsonIgnore
    public boolean isRemoved() {
        return status.equals(LinkStatus.REMOVED);
    }

    @JsonIgnore
    public boolean isUrl() {
        return linkType.equals(LinkType.URL);
    }

    @JsonIgnore
    public boolean isText() {
        return linkType.equals(LinkType.TEXT);
    }

    @JsonIgnore
    public boolean isImage() {
        return linkType.equals(LinkType.IMAGE);
    }

    public void expire() {
        setStatus(LinkStatus.EXPIRED);
    }

    public void refreshExpirationPolicy() {
        this.expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }

    @PostLoad
    public void afterLoad() throws JsonProcessingException {
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
