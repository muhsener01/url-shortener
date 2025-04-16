package demo.muhsener01.urlshortener.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SingleUseExpirationResponse extends ExpirationResponse {

    private boolean used;

    public SingleUseExpirationResponse() {
    }

    public SingleUseExpirationResponse(String type, boolean used) {
        super(type);
        this.used = used;
    }
}
