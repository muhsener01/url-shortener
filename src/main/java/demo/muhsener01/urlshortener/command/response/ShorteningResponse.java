package demo.muhsener01.urlshortener.command.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShorteningResponse {

    @Schema(example = "https://www.shortenly.com/asd83af")
    private String shortUrl;
//    private LocalDateTime createdAt;


}
