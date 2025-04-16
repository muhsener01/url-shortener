package demo.muhsener01.urlshortener.ratelimit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("rate-limiter")
@Data
public class RateLimiterConfig {
    private int bucketCapacity;
    private int bucketRefillInterval;
    private int bucketRefillTokens;
}
