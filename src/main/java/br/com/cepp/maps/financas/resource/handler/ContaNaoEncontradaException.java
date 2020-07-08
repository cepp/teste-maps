package br.com.cepp.maps.financas.resource.handler;

public class ContaNaoEncontradaException extends ObjetoNaoEncontradoException {
    public ContaNaoEncontradaException(String codigoUsuario) {
        super(String.format("Conta Corrente d usuário %s não encontrada", codigoUsuario));
    }
}
