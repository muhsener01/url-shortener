package demo.muhsener01.urlshortener.event;

import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.repository.impl.UrlRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlStatusChangedEventListener {

    private final UrlRepositoryImpl urlRepository;


    @EventListener(UrlStatusChangedEvent.class)
    public void onUrlStatusChanged(UrlStatusChangedEvent event) {
        Link source = event.getSource();

        log.debug("Url status has changed. ID: {}", source.getId());
        urlRepository.update(source);

    }
}
