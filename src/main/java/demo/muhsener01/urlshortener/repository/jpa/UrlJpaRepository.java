package demo.muhsener01.urlshortener.repository.jpa;

import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UrlJpaRepository extends JpaRepository<ShortURL, String> {


    List<ShortURL> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsByIdAndStatus(String id, LinkStatus status);
}
