package demo.muhsener01.urlshortener.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {

    private UUID userId;
    private String email;
    private String username;
}
