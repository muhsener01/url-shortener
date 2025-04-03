package demo.muhsener01.urlshortener.domain.factory;

import demo.muhsener01.urlshortener.domain.entity.expiration.AfterHoursPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.SingleUsePolicy;
import demo.muhsener01.urlshortener.domain.entity.expiration.UntilRemovedPolicy;
import demo.muhsener01.urlshortener.exception.InvalidDomainException;

public class ExpirationPolicyFactory {

    public static ExpirationPolicy create(String type, int afterHours) {
        String lowerCase = type.toLowerCase();
        return switch (lowerCase) {
            case "single-use" -> new SingleUsePolicy();
            case "after-hours" -> new AfterHoursPolicy(afterHours);
            case "until-removed" -> new UntilRemovedPolicy();
            default -> throw new InvalidDomainException("Unknown expiration policy type: " + type);
        };
    }
}
