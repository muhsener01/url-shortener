package demo.muhsener01.urlshortener.io.controller.rest;

import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final LinkMapper linkMapper;
    private final SecurityOperations securityOperations;


    @GetMapping("/{linkId}")
    public ResponseEntity<LinkResponse> findById(@PathVariable(name = "linkId") UUID linkId) {


        ShortenedUrl url = linkService.findById(linkId);

        LinkResponse response = linkMapper.toResponse(url);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{linkId}")
    public ResponseEntity<LinkResponse> deleteById(@PathVariable("linkId") UUID linkId) {
        ShortenedUrl deleted = linkService.deleteById(linkId);

        return ResponseEntity.ok().body(linkMapper.toResponse(deleted));
    }

    @GetMapping
    public ResponseEntity<List<LinkResponse>> findAllByUserId(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                              @PositiveOrZero @RequestParam(name = "page", defaultValue = "0") int page,
                                                              @PositiveOrZero @RequestParam(name = "limit", defaultValue = "10") int limit) {


        List<ShortenedUrl> allByUserId = linkService.findAllByUserId(userPrincipal.getId(), page, limit);


        return ResponseEntity.ok().body(linkMapper.toResponse(allByUserId));
    }


}
