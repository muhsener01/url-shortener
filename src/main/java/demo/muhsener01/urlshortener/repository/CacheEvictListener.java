package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.Link;
import demo.muhsener01.urlshortener.service.CacheService;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEvictListener {

    private final CacheService<String, Link> cacheService;


    @PreUpdate
    public void onPreUpdate(Link link) {
        String cacheKey = "urls:" + link.getId();
        log.debug("Cache evicted: {}", cacheKey);
        cacheService.delete(cacheKey);
    }
}
