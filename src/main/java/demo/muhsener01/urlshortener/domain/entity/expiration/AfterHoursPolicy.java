package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonTypeName;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import demo.muhsener01.urlshortener.exception.UrlExpiredException;
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
    }

    @Override
    public void initialize(ShortenedUrl url) {
        this.type = "after-hours";
        if (expiresAt == null) {
            expiresAt = LocalDateTime.now().plusHours(afterHours);
        }
    }

    @Override
    public void apply(ShortenedUrl shortenedUrl) {
        if (expiresAt.isBefore(LocalDateTime.now())) {
            shortenedUrl.expire();
            throw new UrlExpiredException(shortenedUrl.getShortenCode());
        }


    }

    @Override
    public String displayExpiresAt() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        return dateFormat.format(expiresAt);
    }
}
