package demo.muhsener01.urlshortener.io.controller;

import demo.muhsener01.urlshortener.release.Release;
import demo.muhsener01.urlshortener.release.ReleaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/release")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseJpaRepository releaseJpaRepository;

    @Value("${application.info.release.id}")
    private Long releaseId;

    @GetMapping
    public Release get() {
        return releaseJpaRepository.findById(releaseId).orElse(null);
    }
}
