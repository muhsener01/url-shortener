package demo.muhsener01.urlshortener.exception;

public class TextNotFoundException extends NotFoundException {


    public TextNotFoundException(String searchKey, Object searchValue) {
        super("Text", searchKey, searchValue);
    }

    public TextNotFoundException(String message) {
        super(message);
    }
}
