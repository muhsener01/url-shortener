package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.Link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UrlRepository {

    Link save(Link url);

    Link generateUniqueKeyAndSave(Link link);

    Optional<Link> findById(String shortenCode);

    Link update(Link url);

    boolean existsById(String id);

    Optional<Link> findByIdIfNotRemoved(String id);

    List<Link> findAllByUserIdIfNotRemoved(UUID authenticatedUserId, int page, int limit);
}


