package demo.muhsener01.urlshortener.io.controller.rest;

import demo.muhsener01.urlshortener.command.SignUpCommand;
import demo.muhsener01.urlshortener.command.response.SignUpResponse;
import demo.muhsener01.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpCommand command) {
        SignUpResponse responseBody = userService.createUser(command);

        return ResponseEntity.ok(responseBody);
    }
}
