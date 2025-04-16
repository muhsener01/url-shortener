package demo.muhsener01.urlshortener.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AfterHoursExpirationResponse extends ExpirationResponse {


    private LocalDateTime expiresAt;
    @Schema(example = "5")
    private int afterHours;

    public AfterHoursExpirationResponse() {
    }

    public AfterHoursExpirationResponse(String type, LocalDateTime expiresAt, int afterHours) {
        super(type);
        this.expiresAt = expiresAt;
        this.afterHours = afterHours;
    }
}
