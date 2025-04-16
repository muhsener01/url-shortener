package demo.muhsener01.urlshortener.springdoc;

import demo.muhsener01.urlshortener.shared.dto.LinkResponse;
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
        summary = "Get all shortened links for the authenticated user",
        description = "Retrieves a paginated list of all shortened links belonging to the currently authenticated user. " +
                "If pagination parameters are provided wrong such as (negative page-number) or (less than 1 page-size), then default values(page:0, limit:10) are activated."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "401", description = "Attempt to access resources without being authenticated.",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-04-03T15:00:00",
                                    "status": 400,
                                    "path": "/api/v1/links",
                                    "message": "Authentication is required.",
                                    "details": null
                                }
                                """))),
        @ApiResponse(responseCode = "500", description = "Internal server error!",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-04-03T19:39:20.798891",
                                    "status": 500,
                                    "path": "/api/v1/links/bbdf670s/resolve",
                                    "message": "Internal server error!",
                                    "details": null
                                }
                                """)
                )
        )

})
public @interface FindAllSpringDoc {
}
