package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;

import java.util.List;
import java.util.UUID;

public interface LinkService {

    ShortURL findById(String code);

    List<ShortURL> findAllByUserId(UUID userId, int page, int limit);

    ShortURL deleteById(String urlCode);

    ShortURL update(UpdateLinkCommand command, String code);

    ShorteningResponse shortenUrl(ShortenCommand command);

    ShorteningResponse shortenText(ShortenCommand command);


    String resolve(String shortenCode);
}
