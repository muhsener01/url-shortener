package demo.muhsener01.urlshortener.command.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    private UUID id;
    private String email;
    private String username;
}
