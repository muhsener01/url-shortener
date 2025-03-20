package demo.muhsener01.urlshortener.io.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AfterHoursExpirationResponse extends ExpirationResponse {

    private LocalDateTime expiresAt;
    private int afterHours;

    public AfterHoursExpirationResponse() {
    }

    public AfterHoursExpirationResponse(String type, LocalDateTime expiresAt, int afterHours) {
        super(type);
        this.expiresAt = expiresAt;
        this.afterHours = afterHours;
    }
}
