package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.LinkStatus;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import demo.muhsener01.urlshortener.exception.NotResolvableException;

@JsonTypeName("until-removed")
public class UntilRemovedPolicy extends ExpirationPolicy {

    @Override
    public void initialize(ShortenedUrl url) {
        this.type = "until-removed";
    }

    @Override
    public void apply(ShortenedUrl shortenedUrl) {
        if (!shortenedUrl.getStatus().equals(LinkStatus.ACTIVE))
            throw new NotResolvableException("Link is not active!");
    }

    @Override
    public String displayExpiresAt() {
        return "until removed";
    }
}
