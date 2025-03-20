package demo.muhsener01.urlshortener.exception;

public class NoSuchUrlFoundException extends NotFoundException{

    public NoSuchUrlFoundException( String searchKey, Object searchValue) {
        super("URL", searchKey, searchValue);
    }
}
