package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.Link;
import lombok.Getter;
import lombok.Setter;

@JsonTypeName("single-use")
@Getter
@Setter
public class SingleUsePolicy extends ExpirationPolicy {

    private boolean used;

    @Override
    public void initialize(Link url) {
        used = false;
    }


    @Override
    public boolean apply(Link link) {
        if (used) {
            return false;
        } else {
            used = true;
            link.expire();
            link.refreshExpirationPolicy();
            return true;
        }
    }

    @Override
    public String displayExpiresAt() {
        return used ? "used" : "not used yet";
    }


    public boolean isUsed() {
        return used;
    }
}
