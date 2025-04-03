package demo.muhsener01.urlshortener;

import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.service.CacheService;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEvictListener {

    private final CacheService<String, ShortURL> cacheService;


    @PreUpdate
    public void onPreUpdate(ShortURL shortURL) {
        String cacheKey = "urls:" + shortURL.getId();
        log.debug("Cache evicted: {}", cacheKey);
        cacheService.delete(cacheKey);
    }
}
