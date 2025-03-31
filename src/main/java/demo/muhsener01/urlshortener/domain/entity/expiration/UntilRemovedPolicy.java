package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.exception.NotResolvableException;

@JsonTypeName("until-removed")
public class UntilRemovedPolicy extends ExpirationPolicy {

    @Override
    public void initialize(ShortURL url) {
        this.type = "until-removed";
    }

    @Override
    public void apply(ShortURL shortURL) {
        if (!shortURL.getStatus().equals(LinkStatus.ACTIVE))
            throw new NotResolvableException("Link is not active!");
    }

    @Override
    public String displayExpiresAt() {
        return "until removed";
    }
}
