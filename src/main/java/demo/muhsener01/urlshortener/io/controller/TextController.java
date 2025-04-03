package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.io.response.TextResponse;
import demo.muhsener01.urlshortener.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/texts")
@RequiredArgsConstructor
public class TextController {

    private final TextService textService;

    @GetMapping("/{textCode}")
    private ResponseEntity<TextResponse> getText(@PathVariable("textCode") String textCode) {
        String text = textService.getText(textCode);

        return ResponseEntity.ok(new TextResponse(textCode, text));
    }


}
