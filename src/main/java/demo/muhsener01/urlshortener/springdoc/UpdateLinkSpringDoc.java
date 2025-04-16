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
@Operation(summary = "Update a shortened link.",
        description = "Updates the expiration settings or metadata of a previously shortened link."
)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully updated",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponse.class))),
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
        @ApiResponse(responseCode = "403", description = "Forbidden!",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = """
                                    {
                                        "timestamp": "2025-04-03T21:01:36.269255",
                                        "status": 403,
                                        "path": "/api/v1/links/9f35e7ds",
                                        "message": "No permission to update link with ID: 9f35e7ds ",
                                        "details": null
                                    }
                                    """)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Bad request",
                content = @Content(mediaType = "application/json", examples =
                @ExampleObject(value = """
                            {
                                "timestamp": "2025-04-03T12:34:56",
                                "status": 400,
                                "path": "/api/v1/auth/signup",
                                "message": "No such link status: 'act'",
                                "details": null
                            }
                            """))
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
public @interface UpdateLinkSpringDoc {
}
