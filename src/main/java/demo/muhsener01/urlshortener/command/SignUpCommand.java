package demo.muhsener01.urlshortener.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpCommand {

    private String username;
    private String email;
    private String password;



    public SignUpCommand(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
