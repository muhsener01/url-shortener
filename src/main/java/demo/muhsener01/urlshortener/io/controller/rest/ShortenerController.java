package demo.muhsener01.urlshortener.io.controller.rest;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.service.ShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;


    @PostMapping("/shorten")
    public ResponseEntity<ShorteningResponse> shorten(@RequestParam("type") String inputType, @RequestBody ShortenCommand command) {
        String shortUrl = "";

        ShorteningResponse response = null;
        if (inputType.equalsIgnoreCase("url")) {
            response = shortenerService.shortenUrl(command);
        } else if (inputType.equalsIgnoreCase("text")) {
            response = shortenerService.shortenText(command);

        } else {
            throw new IllegalArgumentException("Invalid operation type: " + inputType);
        }


        //TODO : fix here
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{shortenCode}")
    public ResponseEntity<Void> resolve(@PathVariable(name = "shortenCode") String code, HttpServletResponse response) throws IOException {
        if (code.equalsIgnoreCase("favicon.ico"))
            return null;

        String url = shortenerService.resolve(code);


        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();


    }


}
