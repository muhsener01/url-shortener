package demo.muhsener01.urlshortener.mapper;

import demo.muhsener01.urlshortener.ApplicationProperties;
import demo.muhsener01.urlshortener.command.UpdateLinkCommand;
import demo.muhsener01.urlshortener.domain.entity.ShortURL;
import demo.muhsener01.urlshortener.domain.entity.expiration.AfterHoursPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.SingleUsePolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.UntilRemovedPolicy;
import demo.muhsener01.urlshortener.domain.enums.LinkStatus;
import demo.muhsener01.urlshortener.domain.factory.ExpirationPolicyFactory;
import demo.muhsener01.urlshortener.io.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkMapper {

    private final ApplicationProperties applicationProperties;

    public LinkResponse toResponse(ShortURL url) {
        return new LinkResponse(
                url.getId(),
                url.getLinkType().name(),
                applicationProperties.getBaseDomain() + "/" + url.getId(),
                url.getOriginalUrl(),
                url.getStatus().name(),
                url.getCreatedAt(),
                expirationResponse(url.getExpirationPolicy())
        );

    }

    public List<LinkResponse> toResponse(List<ShortURL> urls) {
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

    public void merge(UpdateLinkCommand command, ShortURL shortURL) {
        if (command.getStatus() != null)
            shortURL.setStatus(LinkStatus.of(command.getStatus()));

        if (command.getExpirationPolicy() != null)
            shortURL.setExpirationPolicy(ExpirationPolicyFactory.create(command.getExpirationPolicy(), command.getAfterHours()));

        if (command.getOriginalUrl() != null)
            shortURL.setOriginalUrl(command.getOriginalUrl());


    }
}
