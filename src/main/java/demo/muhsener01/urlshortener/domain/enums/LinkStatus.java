package demo.muhsener01.urlshortener.domain.enums;

import demo.muhsener01.urlshortener.exception.InvalidDomainException;

import java.util.Locale;

public enum LinkStatus {
    ACTIVE, PASSIVE, EXPIRED, REMOVED;

    public static LinkStatus of(String status) {
        String statusLowerCase = status.toLowerCase(Locale.US);

        return switch (statusLowerCase) {
            case "active" -> ACTIVE;
            case "passive" -> PASSIVE;
            case "expired" -> EXPIRED;
            case "removed" -> REMOVED;
            default -> throw new InvalidDomainException("No such link status: '%s'".formatted(status));
        };
    }
}
