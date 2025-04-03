package demo.muhsener01.urlshortener.command.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    private UUID id;
    @Schema(example = "muhsener98@gmail.com")
    private String email;
    @Schema(example = "muhsener98")
    private String username;
}
