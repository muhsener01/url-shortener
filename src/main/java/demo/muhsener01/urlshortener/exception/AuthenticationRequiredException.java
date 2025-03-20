package demo.muhsener01.urlshortener.exception;

public class AuthenticationRequiredException extends RuntimeException{

    public AuthenticationRequiredException(String message) {
        super(message);
    }
}
