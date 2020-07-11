package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AtivoValorJaExisteException extends ObjetoJaExisteException {
    public AtivoValorJaExisteException(String codigo, LocalDate data) {
        super(String.format("Já existe uma posição do ativo '%s' para o dia %s", codigo, data.format(DateTimeFormatter.ISO_DATE)));
    }
}
