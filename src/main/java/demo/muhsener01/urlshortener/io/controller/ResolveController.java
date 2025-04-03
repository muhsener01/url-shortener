package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.service.LinkService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ResolveController {

    private final LinkService linkService;


    @GetMapping("/{shortenCode}")
    public ResponseEntity<Void> resolve(@PathVariable(name = "shortenCode") String code, HttpServletResponse response) throws IOException {
        if (code.equalsIgnoreCase("favicon.ico"))
            return null;

        String url = linkService.resolve(code);


        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();

    }


}
