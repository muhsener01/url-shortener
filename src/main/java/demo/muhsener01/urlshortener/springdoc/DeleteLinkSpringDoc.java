package demo.muhsener01.urlshortener.springdoc;

import demo.muhsener01.urlshortener.io.response.LinkResponse;
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
@Operation(summary = "Deletes the shortened link.", description = "Makes the status of link in database REMOVED")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Deleted successfully.",
                content = @Content(schema = @Schema(implementation = LinkResponse.class))),
        @ApiResponse(responseCode = "404", description = "URL does not exists!",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-04-03T21:01:36.269255",
                                    "status": 404,
                                    "path": "/api/v1/links/9f35e7ds",
                                    "message": "No such 'URL' found with provided 'id': 9f35e7ds",
                                    "details": null
                                }
                                """)
                )
        ),
        @ApiResponse(responseCode = "403", description = "Forbidden!",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-04-03T21:01:36.269255",
                                    "status": 403,
                                    "path": "/api/v1/links/9f35e7ds",
                                    "message": "No permission to delete link with ID: 9f35e7ds ",
                                    "details": null
                                }
                                """)
                )
        ),
        @ApiResponse(responseCode = "401", description = "Authentication is required!",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                {
                                    "timestamp": "2025-04-03T21:01:36.269255",
                                    "status": 401,
                                    "path": "/api/v1/links/9f35e7ds",
                                    "message": "Authentication is required!",
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
}
)
public @interface DeleteLinkSpringDoc {
}
