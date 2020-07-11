package br.com.cepp.maps.financas.resource.handler;

public abstract class ObjetoJaExisteException extends RuntimeException {
    public ObjetoJaExisteException(String message) {
        super(message);
    }
}
