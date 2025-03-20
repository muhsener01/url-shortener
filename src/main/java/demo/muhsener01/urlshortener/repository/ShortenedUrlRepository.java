package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.LinkStatus;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, UUID> {

    boolean existsByShortenCode(String shortenCode);

    ShortenedUrl findByShortenCode(String shortenCode);

    boolean existsByShortenCodeAndStatus(String shortenCode, LinkStatus status);

    List<ShortenedUrl> findAllByUserId(UUID userId, Pageable pageable);
}
