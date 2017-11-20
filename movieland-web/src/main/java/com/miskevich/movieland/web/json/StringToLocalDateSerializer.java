package com.miskevich.movieland.web.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public class StringToLocalDateSerializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        TemporalAccessor parsed = DATE_FORMATTER.parse(parser.getValueAsString());
        return LocalDate.of(parsed.get(ChronoField.YEAR), 1, 1);
    }
}
