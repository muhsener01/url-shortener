package demo.muhsener01.urlshortener.exception;

public class NotFoundException extends RuntimeException {


    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String objectName, String searchKey, Object searchValue) {
        super("No such %s found with provided '%s': '%s'".formatted(objectName, searchKey, searchValue.toString()));
    }
}
