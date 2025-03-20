package demo.muhsener01.urlshortener.command.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ShorteningResponse {

    private UUID linkId;
    private String shortUrl;


}
