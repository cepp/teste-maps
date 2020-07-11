package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AtivoPeriodoInvalidoException extends RuntimeException {
    public AtivoPeriodoInvalidoException(LocalDate data, String codigo) {
        super(String.format("Data do movimento '%s' está fora do período de validade do ativo '%s'",
                data.format(DateTimeFormatter.ISO_DATE), codigo));
    }
}
