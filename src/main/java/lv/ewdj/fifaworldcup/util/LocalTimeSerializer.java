package lv.ewdj.fifaworldcup.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.time.LocalTime;

public class LocalTimeSerializer extends ValueSerializer<LocalTime> {

    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializationContext ctxt) throws JacksonException {
        gen.writeString(value.format(DateTimeFormats.TIME_FORMATTER));
    }
}
