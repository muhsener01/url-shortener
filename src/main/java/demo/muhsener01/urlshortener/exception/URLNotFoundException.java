package demo.muhsener01.urlshortener.exception;

public class URLNotFoundException extends NotFoundException{

    public URLNotFoundException(String searchKey, Object searchValue) {
        super("URL", searchKey, searchValue);
    }
}
