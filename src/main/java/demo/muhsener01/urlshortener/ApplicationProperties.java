package demo.muhsener01.urlshortener;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application.properties")
@Data
public class ApplicationProperties {

    private String baseDomain;


}
