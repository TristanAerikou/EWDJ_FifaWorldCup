package lv.ewdj.fifaworldcup.util;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class LocalDateDeserializer extends ValueDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws RuntimeException {
        String valueAsString = p.getValueAsString();
        if (valueAsString == null || valueAsString.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(valueAsString, DateTimeFormats.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format: %s".formatted(valueAsString), e);
        }
    }
}
