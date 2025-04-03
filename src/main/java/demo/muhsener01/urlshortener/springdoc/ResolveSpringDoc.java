package demo.muhsener01.urlshortener.springdoc;

import demo.muhsener01.urlshortener.io.response.ResolveResponse;
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
        summary = "Resolve a shortened link",
        description = "Resolves a shortened link ID and returns the original content (URL, text, or image url)."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully resolved",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ResolveResponse.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "URL does not exists!",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-04-03T19:39:20.798891",
                                    "status": 404,
                                    "path": "/api/v1/links/bbdf670s/resolve",
                                    "message": "No such 'URL' found with provided 'id' : bbdf670s",
                                    "details": null
                                }
                                """)
                )
        ),
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
public @interface ResolveSpringDoc {
}
