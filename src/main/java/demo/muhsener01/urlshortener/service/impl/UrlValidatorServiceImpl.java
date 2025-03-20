package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.exception.InvalidUrlException;
import demo.muhsener01.urlshortener.service.UrlValidatorService;
import org.springframework.stereotype.Service;

@Service
public class UrlValidatorServiceImpl implements UrlValidatorService {

    private static final String URL_REGEX = "^(http://|https://).*";


    @Override
    public String validateAndFormatUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new InvalidUrlException("URL cannot be empty!");
        }

        if (url.matches(URL_REGEX))
            return url;

        return "https://" + url;

    }
}
