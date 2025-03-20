package demo.muhsener01.urlshortener.io.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ExpirationResponse {
    private String type;

    public ExpirationResponse() {
    }

    public ExpirationResponse(String type) {
        this.type = type;
    }
}
