package demo.muhsener01.urlshortener.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConfigurationPropertiesBinding
public class DateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {


        String[] parts = source.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        return LocalDate.of(year, month, day);
    }
}
