package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContaNaoEncontradaException extends ObjetoNaoEncontradoException {
    public ContaNaoEncontradaException(String codigoUsuario, LocalDate date) {
        super(String.format("Conta Corrente do usuário %s não encontrada para a data %s", codigoUsuario, date.format(DateTimeFormatter.ISO_DATE)));
    }
}
