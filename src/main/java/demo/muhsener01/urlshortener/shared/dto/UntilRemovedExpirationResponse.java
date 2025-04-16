package demo.muhsener01.urlshortener.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UntilRemovedExpirationResponse extends ExpirationResponse {


    public UntilRemovedExpirationResponse() {
    }

    public UntilRemovedExpirationResponse(String type) {
        super(type);
    }
}
