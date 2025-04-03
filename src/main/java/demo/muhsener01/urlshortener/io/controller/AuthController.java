package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.command.SignUpCommand;
import demo.muhsener01.urlshortener.command.response.SignUpResponse;
import demo.muhsener01.urlshortener.io.handler.GlobalExceptionHandler;
import demo.muhsener01.urlshortener.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;


    @PostMapping("/signup")
    @Operation(
            summary = "Create new User",
            description = "Registers a new user if the username and email are unique"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully."),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username or email already exists",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)))
    })
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpCommand command) {
        SignUpResponse responseBody = userService.createUser(command);

        log.info("New user registered: id: {} email: {} username: {}", responseBody.getId(), responseBody.getEmail(), responseBody.getUsername());

        return ResponseEntity.ok(responseBody);
    }


}
