package br.com.cepp.maps.financas.resource.handler;

import java.math.BigDecimal;

public class MovimentoValorDiferenteException extends RuntimeException {
    public MovimentoValorDiferenteException(BigDecimal valor) {
        super(String.format("Valor '%f' informado não confere com o valor calculado com a posição do ativo", valor.doubleValue()));
    }
}
