package de.unibremen.cs.swp.bokerfi.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguration für den Jackson-JSON-Mapper.
 * <p>
 * Passt das Serialisierungsverhalten an (z.B. Datumsformate, unbekannte Eigenschaften).
 * </p>
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return com.fasterxml.jackson.databind.json.JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .addModule(new JavaTimeModule())
                .build();
    }

    @Bean
    public org.springframework.web.filter.CommonsRequestLoggingFilter logFilter() {
        org.springframework.web.filter.CommonsRequestLoggingFilter filter = new org.springframework.web.filter.CommonsRequestLoggingFilter();
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        return filter;
    }
}
