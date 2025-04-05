package demo.muhsener01.urlshortener.event;

import demo.muhsener01.urlshortener.domain.entity.Link;

public class UrlStatusChangedEvent extends DomainEvent<Link>{


    public UrlStatusChangedEvent(Link source) {
        super(source);
    }
}
