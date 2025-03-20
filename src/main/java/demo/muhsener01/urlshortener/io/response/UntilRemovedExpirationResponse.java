package demo.muhsener01.urlshortener.io.response;

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
