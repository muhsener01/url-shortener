package demo.muhsener01.urlshortener.exception;

public class FeedbackNotFoundException extends NotFoundException {

    public FeedbackNotFoundException(String searchKey, Object searchValue) {
        super("Feedback", searchKey, searchValue);
    }
}
