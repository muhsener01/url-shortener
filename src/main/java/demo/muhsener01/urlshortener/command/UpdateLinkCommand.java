package demo.muhsener01.urlshortener.command;

import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateLinkCommand {

    @Schema(example = "www.google.com.tr")
    private String originalUrl;
    @Schema(example = "[ACTIVE || PASSIVE || REMOVED || EXPIRED]")
    private String status;

    @Schema(example = "[after-hours || until-removed || single-use]")
    private String expirationPolicy;
    @Schema(example = "48")
    private int afterHours;


}
