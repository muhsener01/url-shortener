package demo.muhsener01.urlshortener.release;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

import static demo.muhsener01.urlshortener.release.Release.ReleaseStatus;
import static demo.muhsener01.urlshortener.release.Release.ReleaseType;

@ConfigurationProperties("application.info.release")
@Component
@Data
public class ReleaseProperties {


    private Long id;
    private String version;
    @Enumerated(EnumType.STRING)
    private ReleaseType releaseType;
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private ReleaseStatus status;
    private String source;
    private String note;


}
