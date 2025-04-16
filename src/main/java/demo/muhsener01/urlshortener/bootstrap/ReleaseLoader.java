package demo.muhsener01.urlshortener.bootstrap;

import demo.muhsener01.urlshortener.release.Release;
import demo.muhsener01.urlshortener.release.ReleaseJpaRepository;
import demo.muhsener01.urlshortener.release.ReleaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReleaseLoader {

    private final ReleaseProperties releaseProperties;
    private final ReleaseJpaRepository releaseJpaRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {


        releaseJpaRepository.findById(releaseProperties.getId()).ifPresentOrElse(
                release -> log.info("Release with id '{}' started.", release.getId()),
                () -> {
                    Release release = Release.builder()
                            .id(releaseProperties.getId())
                            .version(releaseProperties.getVersion())
                            .releaseType(releaseProperties.getReleaseType())
                            .releaseDate(releaseProperties.getReleaseDate())
                            .status(releaseProperties.getStatus())
                            .source(releaseProperties.getSource())
                            .releaseNote(releaseProperties.getNote())
                            .build();
                    releaseJpaRepository.save(release);
                    log.info("Release with id '{}' persisted into db.", release.getId());
                });

    }

}
