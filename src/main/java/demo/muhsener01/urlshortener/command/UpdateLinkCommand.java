package demo.muhsener01.urlshortener.command;

import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import lombok.Data;

@Data
public class UpdateLinkCommand {


    private String originalUrl;
    private LinkStatus status;
    private String expirationPolicy;
    private int afterHours;


}
