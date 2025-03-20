package demo.muhsener01.urlshortener.domain.entity.expiration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import demo.muhsener01.urlshortener.domain.entity.ShortenedUrl;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleUsePolicy.class, name = "single-use"),
        @JsonSubTypes.Type(value = UntilRemovedPolicy.class, name = "until-removed"),
        @JsonSubTypes.Type(value = AfterHoursPolicy.class, name = "after-hours")
})
public abstract class ExpirationPolicy {

    @JsonIgnore
    protected String type;

    public abstract void initialize(ShortenedUrl url);

    public abstract void apply(ShortenedUrl shortenedUrl);

    public String getType() {
        return type;
    }

    public abstract String displayExpiresAt();
}
