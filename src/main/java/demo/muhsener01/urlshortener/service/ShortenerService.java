package demo.muhsener01.urlshortener.service;

import demo.muhsener01.urlshortener.command.ShortenCommand;
import demo.muhsener01.urlshortener.command.response.ShorteningResponse;

public interface ShortenerService {

    ShorteningResponse shortenUrl(ShortenCommand command);

    String resolve(String shortenCode);

    ShorteningResponse shortenText(ShortenCommand command);
}

