package demo.muhsener01.urlshortener.mapper;

import demo.muhsener01.urlshortener.ApplicationProperties;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;
import demo.muhsener01.urlshortener.domain.entity.expiration.AfterHoursPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.SingleUsePolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.UntilRemovedPolicy;
import demo.muhsener01.urlshortener.io.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkMapper {

    private final ApplicationProperties applicationProperties;

    public LinkResponse toResponse(ShortenedUrl url) {
        return new LinkResponse(
                applicationProperties.getBaseDomain() + "/" + url.getShortenCode(),
                url.getOriginalUrl(),
                url.getStatus().name(),
                expirationResponse(url.getExpirationPolicy())
        );

    }

    public List<LinkResponse> toResponse(List<ShortenedUrl> urls) {
        return urls.stream()
                .map(this::toResponse)
                .toList();
    }

    public ExpirationResponse expirationResponse(ExpirationPolicy policy) {
        return switch (policy) {
            case SingleUsePolicy singleUsePolicy ->
                    new SingleUseExpirationResponse("single-use", singleUsePolicy.isUsed());
            case UntilRemovedPolicy untilRemovedPolicy -> new UntilRemovedExpirationResponse("until-removed");
            case AfterHoursPolicy afterHoursPolicy ->
                    new AfterHoursExpirationResponse("after-hours", afterHoursPolicy.getExpiresAt(), afterHoursPolicy.getAfterHours());
            case null, default ->
                    throw new IllegalArgumentException("Unknown policy type: " + policy.getClass().getSimpleName());
        };
    }
}
