package demo.muhsener01.urlshortener.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.enums.LinkType;
import demo.muhsener01.urlshortener.event.UrlStatusChangedEvent;
import demo.muhsener01.urlshortener.exception.InvalidDomainException;
import demo.muhsener01.urlshortener.repository.CacheEvictListener;
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
public class Link extends BaseEntity<String> {


    private UUID userId;

    private String creatorEmail;

    @Column(length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    private LinkStatus status;

    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    @Column(name = "expiration_policy", columnDefinition = "TEXT")
    private String expirationPolicyJson;

    private String shortUrl;

    @Transient
    private ExpirationPolicy expirationPolicy;


    public Link(UUID userId, String creatorEmail, String content, ExpirationPolicy expirationPolicy, LinkType linkType) {
        this(null, userId, creatorEmail, content, expirationPolicy, linkType);
    }


    public Link(String id, UUID userId, String creatorEmail, String content, ExpirationPolicy expirationPolicy, LinkType linkType) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.expirationPolicy = expirationPolicy;
        this.linkType = linkType;
        this.creatorEmail = creatorEmail;


        initialize();


    }

    private void initialize() {
        validate();
        this.status = LinkStatus.ACTIVE;
        expirationPolicy.initialize(this);
        expirationPolicyJson = JsonUtils.convertToJson(expirationPolicy);
    }

    private void validate() {
        if (userId == null || creatorEmail == null || creatorEmail.isBlank())
            throw new InvalidDomainException("Link must have a userId and non-blank creator email.");

        if (expirationPolicy == null)
            throw new InvalidDomainException("Link must have a valid expiration policy.");

        if (content == null || content.isBlank() || content.length() > 500)
            throw new InvalidDomainException("Link content must be non-blank and at most 500 characters.");

        if (linkType == null)
            throw new InvalidDomainException("Link must have a type such as IMAGE, TEXT or URL");


    }


    public boolean resolve() {
        if (!isActive()) return false;

        boolean resolvable = expirationPolicy.apply(this);

        if (isExpired())
            addEvent(new UrlStatusChangedEvent(this));

        return resolvable;

    }

    @JsonIgnore
    public boolean isRemoved() {
        return status.equals(LinkStatus.REMOVED);
    }

    @JsonIgnore
    public boolean isExpired() {
        return status.equals(LinkStatus.EXPIRED);
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

    public void setId(String id) {
        if (this.id == null)
            this.id = id;
        else
            throw new RuntimeException("ID of ShortURL cannot be changed!");


    }

}
