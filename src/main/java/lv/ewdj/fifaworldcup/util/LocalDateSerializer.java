package lv.ewdj.fifaworldcup.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateSerializer extends ValueSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        gen.writeString(value.format(DateTimeFormats.DATE_FORMATTER));
    }
}
