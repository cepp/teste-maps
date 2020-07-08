package br.com.cepp.maps.financas.resource.handler;

public abstract class ObjetoNaoEncontradoException extends RuntimeException {
    public ObjetoNaoEncontradoException(String message) {
        super(message);
    }
}
