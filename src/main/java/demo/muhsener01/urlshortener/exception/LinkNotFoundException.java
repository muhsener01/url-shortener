package demo.muhsener01.urlshortener.exception;

public class LinkNotFoundException extends NotFoundException{

    public LinkNotFoundException(String searchKey, Object searchValue) {
        super("Link", searchKey, searchValue);
    }
}
