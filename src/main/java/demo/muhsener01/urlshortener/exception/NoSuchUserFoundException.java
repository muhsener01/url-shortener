package demo.muhsener01.urlshortener.exception;

public class NoSuchUserFoundException extends NotFoundException {


    public NoSuchUserFoundException(String searchKey, Object searchValue) {
        super("User", searchKey, searchValue);
    }
}
