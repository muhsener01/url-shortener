package demo.muhsener01.urlshortener.command.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse extends RepresentationModel<SignUpResponse> {

    private UUID id;
    private String email;
    private String username;
}
