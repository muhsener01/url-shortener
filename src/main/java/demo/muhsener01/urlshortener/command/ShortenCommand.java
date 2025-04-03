package demo.muhsener01.urlshortener.command;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShortenCommand {

    @Size(max = 240, message = "Input must be less than 240 characters.")
    private String input;
    private String expirationPolicy;
    private int afterHours;
    private String baseDomain;


}
