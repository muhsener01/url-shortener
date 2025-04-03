package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;

@JsonTypeName("until-removed")
public class UntilRemovedPolicy extends ExpirationPolicy {

    @Override
    public void initialize(ShortURL url) {
        this.type = "until-removed";
    }


    @Override
    public boolean apply(ShortURL shortURL) {
        return !shortURL.isRemoved();
    }

    @Override
    public String displayExpiresAt() {
        return "until removed";
    }
}
