package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;

import java.util.List;
import java.util.UUID;

public interface LinkService {

    ShortenedUrl findById(UUID linkId);

    List<ShortenedUrl> findAllByUserId(UUID id, int page, int limit);

    ShortenedUrl deleteById(UUID linkId);
}
