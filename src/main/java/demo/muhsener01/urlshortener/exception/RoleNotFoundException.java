package demo.muhsener01.urlshortener.exception;

public class RoleNotFoundException extends NotFoundException {

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String searchKey, Object searchValue) {
        super("Role", searchKey, searchValue);
    }
}
