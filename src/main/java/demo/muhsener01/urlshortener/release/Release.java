package demo.muhsener01.urlshortener.release;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "release")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Release {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReleaseType releaseType;

    private LocalDate releaseDate;
    private String version;

    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;
    private String source;
    private String releaseNote;


    enum ReleaseType {
        BE, DE;

    }

    enum ReleaseStatus {
        ACTIVE, INACTIVE;
    }
}
