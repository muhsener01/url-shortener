package demo.muhsener01.urlshortener.io.controller.rest;

import demo.muhsener01.urlshortener.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text")
@RequiredArgsConstructor
public class TextController {

    private final TextService textService;

    @GetMapping("/{textCode}")
    private ResponseEntity<String> getText(@PathVariable("textCode") String textCode) {
        String text = textService.getText(textCode);

        return ResponseEntity.ok(text);
    }
}
