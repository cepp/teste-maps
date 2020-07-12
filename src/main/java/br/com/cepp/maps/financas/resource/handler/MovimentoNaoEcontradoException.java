package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MovimentoNaoEcontradoException extends RuntimeException {
    public MovimentoNaoEcontradoException(LocalDate dataInicio, LocalDate dataFim) {
        super(String.format("Nenhum movimento encontrado no período de '%s' a '%s'", dataInicio.format(DateTimeFormatter.ISO_DATE),
                dataFim.format(DateTimeFormatter.ISO_DATE)));
    }
}
