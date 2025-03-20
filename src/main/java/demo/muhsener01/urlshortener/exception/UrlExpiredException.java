package demo.muhsener01.urlshortener.exception;

public class UrlExpiredException extends NotResolvableException {

    public UrlExpiredException(String code) {
        super("URL with code  '%s' has expired!".formatted(code));
    }
}
