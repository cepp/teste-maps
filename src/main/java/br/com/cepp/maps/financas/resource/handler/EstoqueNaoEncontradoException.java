package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EstoqueNaoEncontradoException extends ObjetoNaoEncontradoException {
    public EstoqueNaoEncontradoException(LocalDate dataPosicao) {
        super(String.format("Estoque não encontrado para a data '%s'", dataPosicao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    }
}
