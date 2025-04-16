package demo.muhsener01.urlshortener.domain.entity.feedback;

import demo.muhsener01.urlshortener.domain.entity.BaseEntity;
import demo.muhsener01.urlshortener.domain.entity.MyUser;
import demo.muhsener01.urlshortener.exception.InvalidDomainException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "feedback")
@Getter
@NoArgsConstructor
public class Feedback extends BaseEntity<UUID> {

    @Column(name = "user_id")
    private UUID userId;

    private String title;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MyUser user;


    public Feedback(UUID userId, String title, String content) {
        this.userId = userId;
        this.content = content;
        this.title = title;


        if (content == null || content.isBlank() || content.length() > 500)
            throw new InvalidDomainException("Feedback content must be non-blank and at most 500 characters.");

        this.id = UUID.randomUUID();
    }
}
