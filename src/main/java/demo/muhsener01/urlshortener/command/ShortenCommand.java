package demo.muhsener01.urlshortener.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ShortenCommand {


    @Schema(example = "[www.google.com.tr || (text with 240 characters)]")
    private String input;
    @Schema(example = "[after-hours || single-use || until-removed]")
    private String expirationPolicy;
    @Schema(example = "24")
    private int afterHours;
    @Schema(example = "https://www.shortenly.com")
    private String baseDomain;


}
