package demo.muhsener01.urlshortener.exception;

public class InvalidDomainException extends RuntimeException {


    public InvalidDomainException(String message) {
        super(message);
    }

    public InvalidDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
