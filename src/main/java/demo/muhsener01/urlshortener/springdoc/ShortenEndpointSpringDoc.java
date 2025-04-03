package demo.muhsener01.urlshortener.springdoc;

import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
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
        summary = "Shorten URL, Text or Image",
        description = "Shortens a URL, text, or an image file based on the input type. The request must be sent as 'multipart/form-data'."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully shortened",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ShorteningResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json", examples =
                @ExampleObject(value = """
                            {
                                "timestamp": "2025-04-03T12:34:56",
                                "status": 400,
                                "path": "/api/v1/auth/signup",
                                "message": "Multipart file cannot be null when type=image.",
                                "details": null
                            }
                            """))),
        @ApiResponse(responseCode = "401", description = "Attempt to access without being authenticated.",
                content = @Content(mediaType = "application/json", examples =
                @ExampleObject(value = """
                            {
                                "timestamp": "2025-04-03T12:34:56",
                                "status": 401,
                                "path": "/api/v1/auth/signup",
                                "message": "Authentication is required!",
                                "details": null
                            }
                            """))),
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
public @interface ShortenEndpointSpringDoc {
}
