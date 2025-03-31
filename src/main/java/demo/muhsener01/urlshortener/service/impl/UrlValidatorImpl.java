package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.exception.InvalidUrlException;
import demo.muhsener01.urlshortener.service.UrlValidator;
import org.springframework.stereotype.Service;

@Service
public class UrlValidatorImpl implements UrlValidator {

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
