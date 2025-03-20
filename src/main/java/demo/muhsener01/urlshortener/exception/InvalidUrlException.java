package demo.muhsener01.urlshortener.exception;

public class InvalidUrlException extends RuntimeException{

    public InvalidUrlException(String message) {
        super(message);
    }
}
