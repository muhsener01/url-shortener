package demo.muhsener01.urlshortener.exception;

public class UserNotFoundException extends NotFoundException {


    public UserNotFoundException(String searchKey, Object searchValue) {
        super("User", searchKey, searchValue);
    }
}
