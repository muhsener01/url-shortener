package demo.muhsener01.urlshortener.io.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolveResponse {

    private String type;
    private String originalUrl;
    private String text;
    private String status;
}
