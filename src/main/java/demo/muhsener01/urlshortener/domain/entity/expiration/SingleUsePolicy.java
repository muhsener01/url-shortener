package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.exception.NotResolvableException;
import lombok.Getter;
import lombok.Setter;

@JsonTypeName("single-use")
@Getter
@Setter
public class SingleUsePolicy extends ExpirationPolicy {

    private boolean used;

    @Override
    public void initialize(ShortURL url) {
        used = false;
    }

    @Override
    public void apply(ShortURL shortURL) {
        if (used) {
            throw new NotResolvableException("Link was one-time use.");
        } else {
            used = true;
            shortURL.expire();
            shortURL.refreshExpirationPolicy();

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
