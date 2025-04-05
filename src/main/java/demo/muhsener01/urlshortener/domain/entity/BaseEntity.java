package demo.muhsener01.urlshortener.domain.entity;

import demo.muhsener01.urlshortener.event.DomainEvent;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity<ID> {
    @Id
    protected ID id;
    @CreatedDate
    protected LocalDateTime createdAt;
    @LastModifiedDate
    protected LocalDateTime updateAt;

    @Transient
    protected List<DomainEvent> events = new ArrayList<>();

    protected void addEvent(DomainEvent event) {
        events.add(event);
    }

    public void clearEvents() {
        events.clear();
    }


}
