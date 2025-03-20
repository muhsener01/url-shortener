package demo.muhsener01.urlshortener.command;

import lombok.Data;

@Data
public class ShortenCommand {

    private String input;
    private String expirationPolicy;
    private int afterHours;


}
