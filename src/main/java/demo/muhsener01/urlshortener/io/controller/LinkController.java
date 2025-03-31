package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.io.response.LinkResponse;
import demo.muhsener01.urlshortener.mapper.LinkMapper;
import demo.muhsener01.urlshortener.security.UserPrincipal;
import demo.muhsener01.urlshortener.service.LinkService;
import demo.muhsener01.urlshortener.service.SecurityOperations;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final LinkMapper linkMapper;
    private final SecurityOperations securityOperations;


    @GetMapping("/{linkId}")
    public ResponseEntity<LinkResponse> findById(@PathVariable(name = "linkId") String code) {


        ShortURL url = linkService.findById(code);

        LinkResponse response = linkMapper.toResponse(url);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<LinkResponse> update(@PathVariable(name = "code") String code, @RequestBody UpdateLinkCommand command) {


        ShortURL url = linkService.update(command, code);
        LinkResponse response = linkMapper.toResponse(url);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{linkId}")
    public ResponseEntity<LinkResponse> deleteById(@PathVariable("linkId") String urlCode) {
        ShortURL deleted = linkService.deleteById(urlCode);

        return ResponseEntity.ok().body(linkMapper.toResponse(deleted));
    }


    @GetMapping
    public ResponseEntity<List<LinkResponse>> findAllByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @PositiveOrZero @RequestParam(name = "page", defaultValue = "0") int page,
                                                              @PositiveOrZero @RequestParam(name = "limit", defaultValue = "10") int limit) {


        List<ShortURL> allByUserId = linkService.findAllByUserId(userPrincipal.getId(), page, limit);


        return ResponseEntity.ok().body(linkMapper.toResponse(allByUserId));
    }

    @PostMapping()
    public ResponseEntity<ShorteningResponse> shorten(@RequestParam("type") String inputType, @RequestBody ShortenCommand command) {
        String shortUrl = "";

        ShorteningResponse response = null;
        if (inputType.equalsIgnoreCase("url")) {
            response = linkService.shortenUrl(command);
        } else if (inputType.equalsIgnoreCase("text")) {
            response = linkService.shortenText(command);
        } else {
            throw new RuntimeException("Invalid operation type: " + inputType);
        }


        return ResponseEntity.ok(response);
    }

}
