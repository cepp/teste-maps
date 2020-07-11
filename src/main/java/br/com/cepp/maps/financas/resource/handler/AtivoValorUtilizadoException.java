package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AtivoValorUtilizadoException extends RuntimeException {
    public AtivoValorUtilizadoException(String codigo, LocalDate data) {
        super(String.format("Não foi encontrado o ativo '%s' na data '%s'", codigo, data.format(DateTimeFormatter.ISO_DATE)));
    }
}
