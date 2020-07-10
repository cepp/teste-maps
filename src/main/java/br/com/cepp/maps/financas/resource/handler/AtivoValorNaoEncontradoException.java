package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AtivoValorNaoEncontradoException extends ObjetoNaoEncontradoException {
    public AtivoValorNaoEncontradoException(String codigo, LocalDate data) {
        super(String.format("Posição do ativo '%s' na data %s", codigo, data.format(DateTimeFormatter.ISO_DATE)));
    }
}
