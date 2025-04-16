package demo.muhsener01.urlshortener.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResolveResponse {

    @Schema(example = "[URL || IMAGE || TEXT]")
    private String type;
    @Schema(example = "[ (a URL) || (an image URL) || (text) ]")
    private String content;
    @Schema(example = "[ACTIVE || PASSIVE || EXPIRED || REMOVED]")
    private String status;
    @Schema(example = "muhsener@gmail.com")
    private String creatorEmail;
}
