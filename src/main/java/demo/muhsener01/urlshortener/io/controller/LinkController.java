package demo.muhsener01.urlshortener.io.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.io.response.LinkResponse;
import demo.muhsener01.urlshortener.io.response.ResolveResponse;
import demo.muhsener01.urlshortener.mapper.LinkMapper;
import demo.muhsener01.urlshortener.service.LinkService;
import demo.muhsener01.urlshortener.springdoc.*;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final LinkMapper linkMapper;


    @ShortenEndpointSpringDoc
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ShorteningResponse> shorten(
            @Parameter(description = "The type of the input to shorten (url, text, image)", required = true, example = "url")
            @RequestParam("type") String inputType,
            @Parameter(description = "JSON representation of the shorten command", required = true, content = @Content(schema = @Schema(implementation = ShortenCommand.class)))
            @RequestParam("body") String bodyJson,
            @Parameter(description = "Optional file for image shortening (only required when type=image)")
            @RequestParam(value = "file", required = false) MultipartFile multipartFile) {

        ShortenCommand command = null;
        try {
            command = JsonUtils.convertToObject(bodyJson, ShortenCommand.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid json format.");
        }


        ShorteningResponse response = null;
        if (inputType.equalsIgnoreCase("url"))
            response = linkService.shorten("url",command);
        else if (inputType.equalsIgnoreCase("text"))
            response = linkService.shorten("text",command);
        else if (inputType.equalsIgnoreCase("image")) {
            if (multipartFile == null)
                throw new IllegalArgumentException("Multipart file cannot be null when type=image");

            response = linkService.shortenImage(command, multipartFile);
        } else
            throw new IllegalArgumentException("Invalid operation type: " + inputType);


        return ResponseEntity.ok(response);
    }





    @ResolveSpringDoc
    @GetMapping("/{linkId}/resolve")
    public ResponseEntity<ResolveResponse> resolve(
            @Parameter(description = "Unique identifier of the shortened link", required = true, example = "abcs342")
            @PathVariable(name = "linkId") String code) {


        ResolveResponse resolveResponse = linkService.resolve(code);

        return ResponseEntity.ok(resolveResponse);
    }

    @UpdateLinkSpringDoc
    @PutMapping("/{linkId}")
    public ResponseEntity<LinkResponse> update(
            @Parameter(description = "Unique identifier of the shortened link", required = true, example = "abcs342")
            @PathVariable(name = "linkId") String linkId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New link properties. If you do not want to make any changes in a property, make it 'null'.", required = true)
            @RequestBody UpdateLinkCommand command) {


        Link url = linkService.update(command, linkId);
        LinkResponse response = linkMapper.toResponse(url);

        return ResponseEntity.ok(response);
    }


    @DeleteLinkSpringDoc
    @DeleteMapping("/{linkId}")
    public ResponseEntity<LinkResponse> deleteById(
            @Parameter(description = "Unique identifier of the shortened link", required = true, example = "abcs342")
            @PathVariable("linkId") String urlCode) {
        Link deleted = linkService.deleteById(urlCode);

        return ResponseEntity.ok().body(linkMapper.toResponse(deleted));
    }


    @FindAllSpringDoc
    @GetMapping
    public ResponseEntity<List<LinkResponse>> findAll(
            @Parameter(description = "Page number (zero-based)", example = "0")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (at least 1)", example = "5")
            @RequestParam(name = "limit", defaultValue = "10") int limit) {


        if (page < 0 || limit < 1) {
            page = 0;
            limit = 10;
        }

        List<Link> allByUserId = linkService.findAllOfAuthenticatedUser(page, limit);
        return ResponseEntity.ok().body(linkMapper.toResponse(allByUserId));
    }


}
