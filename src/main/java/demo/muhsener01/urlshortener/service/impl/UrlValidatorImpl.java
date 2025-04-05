package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.exception.InvalidUrlException;
import demo.muhsener01.urlshortener.service.UrlValidator;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

@Service
public class UrlValidatorImpl implements UrlValidator {

    private static final String URL_REGEX = "^(http://|https://).*";

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?://)?(www\\.)?([a-zA-Z0-9.-]+)\\.[a-zA-Z]{2,6}(:\\d+)?(/.*)?$"
    );


    @Override
    public String validateAndFormatUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new InvalidUrlException("URL cannot be empty!");
        }

        url = url.trim();

        if (!url.matches("^(http://|https://).*")) {
            url = "https://" + url;
        }
//
//        if (!isValidUrl(url)) {
//            throw new InvalidUrlException("Invalid URL format: " + url);
//        }
//
//        if (isIpAddress(url)) {
//            throw new InvalidUrlException("IP address-based URLs are not allowed!");
//        }

        return url;

    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url);
            return URL_PATTERN.matcher(url).matches();
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private boolean isIpAddress(String url) {
        return url.matches(".*://(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})(:\\d+)?(/.*)?$");
    }
}
