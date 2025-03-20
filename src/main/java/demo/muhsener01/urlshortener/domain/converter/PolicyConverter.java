package demo.muhsener01.urlshortener.domain.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.muhsener01.urlshortener.domain.entity.expiration.ExpirationPolicy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PolicyConverter implements AttributeConverter<ExpirationPolicy, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ExpirationPolicy attribute) {

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExpirationPolicy convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, ExpirationPolicy.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
