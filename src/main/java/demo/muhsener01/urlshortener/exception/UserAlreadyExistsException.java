package demo.muhsener01.urlshortener.exception;

public class UserAlreadyExistsException extends RuntimeException {


    public UserAlreadyExistsException(String username, String email) {
        super("User already exists with provided username '%s' or email '%s'".formatted(username, email));
    }
}
