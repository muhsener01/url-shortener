package demo.muhsener01.urlshortener.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ExpirationResponse {
    @Schema(example = "[after-hours || single-use || until-removed]")
    private String type;

    public ExpirationResponse() {
    }

    public ExpirationResponse(String type) {
        this.type = type;
    }
}
