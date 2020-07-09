package br.com.cepp.maps.financas.resource.handler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EstoqueNaoEncontradoException extends ObjetoNaoEncontradoException {
    public EstoqueNaoEncontradoException(String ativo, LocalDate dataPosicao) {
        super(String.format("Estoque do ativo '%s' não encontrado para a data '%s'", ativo, dataPosicao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    }
}
