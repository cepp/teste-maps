package br.com.cepp.maps.financas.resource.handler;

public class AtivoNaoEncontradoException extends ObjetoNaoEncontradoException {
    public AtivoNaoEncontradoException(String id) {
        super(String.format("Ativo %s não encontrado", id));
    }
}
