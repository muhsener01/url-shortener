package demo.muhsener01.urlshortener.event;

public abstract class DomainEvent<T> {

    private final T source;

    public DomainEvent(T source) {
        this.source = source;
    }

    public T getSource() {
        return source;
    }
}
