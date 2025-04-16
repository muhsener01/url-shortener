package demo.muhsener01.urlshortener.ratelimit;

import demo.muhsener01.urlshortener.io.handler.GlobalExceptionHandler;
import demo.muhsener01.urlshortener.utils.JsonUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final RateLimiterConfig rateLimiterConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        log.debug("Rate limit control for IP: {}", ip);

        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for IP: {}", ip);
            GlobalExceptionHandler.ErrorResponse errorResponse = new GlobalExceptionHandler.ErrorResponse(LocalDateTime.now(), 429, request.getRequestURI(), "Too many request!");
            response.setStatus(429);
            response.getWriter().append(JsonUtils.convertToJson(errorResponse));
            response.setContentType("application/json");
            return;
        }

    }

    private Bucket createNewBucket() {
        return Bucket.builder().addLimit(Bandwidth.classic(rateLimiterConfig.getBucketCapacity(),
                        Refill.intervally(rateLimiterConfig.getBucketRefillTokens(),
                                Duration.ofMillis(rateLimiterConfig.getBucketRefillInterval()))))
                .build();
    }

    @PostConstruct
    public void postConstruct() {
        if (rateLimiterConfig == null)
            throw new IllegalStateException("Rate limiter config is null!");

        log.warn("Rate limiter is configured as bucket-capacity: {}, bucket-refill-tokens: {}, bucket-refill-interval-in-ms: {}",
                rateLimiterConfig.getBucketCapacity(),
                rateLimiterConfig.getBucketRefillTokens(),
                rateLimiterConfig.getBucketRefillInterval());
    }
}
