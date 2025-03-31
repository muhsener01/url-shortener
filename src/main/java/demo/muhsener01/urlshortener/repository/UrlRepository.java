package demo.muhsener01.urlshortener.repository;

import demo.muhsener01.urlshortener.domain.entity.ShortURL;

import java.util.Optional;

public interface UrlRepository {

    ShortURL save(ShortURL url);

    ShortURL generateUniqueKeyAndSave(ShortURL shortURL);

    Optional<ShortURL> findById(String shortenCode);

    ShortURL update(ShortURL url);

    boolean existsById(String id);
}


