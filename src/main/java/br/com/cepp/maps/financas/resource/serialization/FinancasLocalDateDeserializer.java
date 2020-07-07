package br.com.cepp.maps.financas.resource.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FinancasLocalDateDeserializer extends StdDeserializer<LocalDate> {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    protected FinancasLocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return LocalDate.parse(jsonParser.readValueAs(String.class), DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
