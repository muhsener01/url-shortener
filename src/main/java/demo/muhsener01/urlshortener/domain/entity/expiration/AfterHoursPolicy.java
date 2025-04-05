package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.exception.InvalidDomainException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@JsonTypeName("after-hours")
@Getter
@Setter
@NoArgsConstructor
public class AfterHoursPolicy extends ExpirationPolicy {

    private Integer afterHours;
    private LocalDateTime expiresAt;

    public AfterHoursPolicy(Integer afterHours) {
        this.afterHours = afterHours;

        if (afterHours <= 0) {
            throw new InvalidDomainException("afterHours must be positive.");
        }
    }

    @Override
    public void initialize(Link url) {
        this.type = "after-hours";
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusHours(afterHours);
//            expiresAt = LocalDateTime.now().plusSeconds(10); // For test.
        }
    }


    @Override
    public boolean apply(Link link) {
        if (expiresAt.isBefore(LocalDateTime.now())) {
            link.expire();
            return false;
//            throw new UrlExpiredException(shortURL.getId());
        }
        return true;
    }

    @Override
    public String displayExpiresAt() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return dateFormat.format(expiresAt);
    }
}
