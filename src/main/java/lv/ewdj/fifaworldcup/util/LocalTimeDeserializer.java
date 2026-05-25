package lv.ewdj.fifaworldcup.util;


import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class LocalTimeDeserializer extends ValueDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws RuntimeException {
        String valueAsString = p.getValueAsString();
        if (valueAsString == null || valueAsString.isBlank()) {
            return null;
        }
        try {
            return LocalTime.parse(valueAsString, DateTimeFormats.TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format: %s".formatted(valueAsString), e);
        }
    }
}
