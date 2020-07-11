package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContaPosicaoJaExisteException extends ObjetoJaExisteException {
    public ContaPosicaoJaExisteException(String codigoUsuario, LocalDate data) {
        super(String.format("Já existe posição da conta '%s' na data '%s'", codigoUsuario, data.format(DateTimeFormatter.ISO_DATE)));
    }
}
