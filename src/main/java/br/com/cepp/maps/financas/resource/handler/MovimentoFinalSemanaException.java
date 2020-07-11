package br.com.cepp.maps.financas.resource.handler;

public class MovimentoFinalSemanaException extends RuntimeException {
    public MovimentoFinalSemanaException() {
        super("Movimentos só podem ser realizados em dias úteis");
    }
}
