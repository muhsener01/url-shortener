package demo.muhsener01.urlshortener.service;

public interface TransactionService {
    void executeAfterCompletion(Runnable runnable);
}
