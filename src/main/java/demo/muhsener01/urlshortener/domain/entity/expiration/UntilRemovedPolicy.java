package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.Link;

@JsonTypeName("until-removed")
public class UntilRemovedPolicy extends ExpirationPolicy {

    @Override
    public void initialize(Link url) {
        this.type = "until-removed";
    }


    @Override
    public boolean apply(Link link) {
        return !link.isRemoved();
    }

    @Override
    public String displayExpiresAt() {
        return "until removed";
    }
}
