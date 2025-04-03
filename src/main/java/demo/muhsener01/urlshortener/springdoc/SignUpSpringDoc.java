package demo.muhsener01.urlshortener.springdoc;

import demo.muhsener01.urlshortener.io.handler.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Operation(
        summary = "Create new User",
        description = "Registers a new user if the username and email are unique."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "User created successfully."),
        @ApiResponse(
                responseCode = "409",
                description = "Username or email already exists",
                content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                        examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-04-03T12:34:56",
                                        "status": 409,
                                        "path": "/api/v1/auth/signup",
                                        "message": "User already exists by provided username 'muhsener' or email 'muhsener@gmail.com'",
                                        "details": null
                                    }
                                    """))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Server error",
                content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class),
                        examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-04-03T12:34:56",
                                        "status": 500,
                                        "path": "/api/v1/auth/signup",
                                        "message": "Internal server error",
                                        "details": null
                                    }""")))
})
public @interface SignUpSpringDoc {
}
